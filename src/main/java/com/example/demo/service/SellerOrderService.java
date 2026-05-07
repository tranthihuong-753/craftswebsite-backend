package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CancelOrderRequest;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.OrderProductDTO;
import com.example.demo.dto.ShipOrderRequest;
import com.example.demo.entity.AnhVideoSanPham;
import com.example.demo.entity.ChiTietDonHang;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.entity.ThanhToan;
import com.example.demo.entity.VanChuyen;
import com.example.demo.enums.NKKT_HanhDong;
import com.example.demo.enums.NKKT_LoaiMucTieu;
import com.example.demo.enums.NKKT_LoaiTacNhan;
import com.example.demo.exception.AppException;
import com.example.demo.repository.AnhVideoSanPhamRepository;
import com.example.demo.repository.ChiTietDonHangRepository;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.SanPhamCoSanRepository;
import com.example.demo.repository.ThanhToanRepository;
import com.example.demo.repository.ThongTinNguoiBanRepository;
import com.example.demo.repository.VanChuyenRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
    
@Service
@RequiredArgsConstructor
public class SellerOrderService {
    @Autowired
    private DonHangRepository donHangRepo;
    @Autowired
    private ThongTinNguoiBanRepository sellerRepo;
    @Autowired
    private VanChuyenRepository vcRepo;
    @Autowired
    private AuditService auditService;
    @Autowired
    private SanPhamCoSanRepository sanphamcoSanRepo;
    @Autowired
    private ChiTietDonHangRepository chiTietDonHangRepo;
    @Autowired
    private AnhVideoSanPhamRepository anhVideoSanPhamRepo;
    @Autowired
    private NguoiDungRepository nguoidungrepo;
    @Autowired
    private ThanhToanRepository thanhToanRepo;

    // LẤY DANH SÁCH ĐƠN HÀNG VỚI PHÂN TRANG, LỌC, TÌM KIẾM, SẮP XẾP 
    public Page<OrderItemDTO> getOrders(int page, 
        int size, 
        String tabStatus, // tblDonHang.DH_TrangThai
        String processingType, // DH_TrangThai
        String keyword, // DH_MaDon & tblNguoiDung.ND_Ten                                        
        String sortField, // DH_NgayDat, DH_TienPhaiThanhToan
        String sortDir,                                      
        UUID userId
    ) {
        UUID sellerId = sellerRepo.findByNguoiDungId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người bán")).getId();

        // 1. Xử lý Sắp xếp
        Sort sort = Sort.by(sortDir.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                mapSortField(sortField));
        Pageable pageable = PageRequest.of(page, size, sort);

        // 2. Chuẩn hóa Keyword
        String searchKeyword = (keyword == null || keyword.trim().isEmpty()) ? null : keyword.trim();

        // 3. Lấy trạng thái thanh toán và xử lý logic cho ProcessingType
        // ALL: Không lọc trạng thái.
        // XAC_NHAN_THANH_TOAN: DH_TrangThai = 'CHO_XAC_NHAN' AND DH_TrangThaiThanhToan = 'DANG_XU_LY'. (Nghĩa là: Khách đã gửi bill, Seller cần check tiền).
        // XAC_NHAN_LAY_HANG: DH_TrangThai = 'CHO_XAC_NHAN' AND DH_TrangThaiThanhToan = 'DA_THANH_TOAN'. (Nghĩa là: Đã nhận tiền, Seller cần chuẩn bị hàng).
        // XAC_NHAN_GIAO_HANG: DH_TrangThai = 'CHO_LAY_HANG' AND DH_TrangThaiThanhToan = 'DA_THANH_TOAN'. (Nghĩa là: Hàng đã đóng gói xong, chờ giao cho Shipper).
        // DANG_GIAO: DH_TrangThai = 'CHO_GIAO_HANG' AND DH_TrangThaiThanhToan = 'DA_THANH_TOAN'. (Nghĩa là: Hàng đã đóng gói xong, chờ giao cho Shipper).
        // HOAN_THANH: DH_TrangThai = 'DA_GIAO'.
        // DA_HUY: DH_TrangThai = 'DA_HUY'.
        String dh_trangthai = null;
        String dh_trangthaiThanhtoan = null;
        if ("XAC_NHAN_THANH_TOAN".equals(processingType)) {
            dh_trangthai = "CHO_XAC_NHAN";
            dh_trangthaiThanhtoan = "DANG_XU_LY";
        } else if ("XAC_NHAN_LAY_HANG".equals(processingType)) {
            dh_trangthai = "CHO_XAC_NHAN";
            dh_trangthaiThanhtoan = "DA_THANH_TOAN";
        } else if ("XAC_NHAN_GIAO_HANG".equals(processingType)) {
            dh_trangthai = "CHO_LAY_HANG";
            dh_trangthaiThanhtoan = "DA_THANH_TOAN";
        } else if ("DANG_GIAO".equals(processingType)) {
            dh_trangthai = "CHO_GIAO_HANG";
            dh_trangthaiThanhtoan = "DA_THANH_TOAN";
        } else if ("HOAN_THANH".equals(processingType)) {
            dh_trangthai = "DA_GIAO";
        } else if ("DA_HUY".equals(processingType)) {
            dh_trangthai = "DA_HUY";
        }

        // 4. Gọi Repository với đầy đủ các tham số lọc
        Page<DonHang> orders = donHangRepo.findBySellerWithFullFilter(
                sellerId, 
                tabStatus, 
                dh_trangthai, 
                dh_trangthaiThanhtoan, 
                searchKeyword, 
                pageable
        );

        return orders.map(this::mapToDTO);
    }

    private OrderItemDTO mapToDTO(DonHang d) {

        List<OrderProductDTO> items = chiTietDonHangRepo.findByDonHangId(d.getId()).stream()        
                .map(ct -> {
                    Long sanPhamId = ct.getSanPhamId();
                    // List<String> images = anhVideoSanPhamRepo.findBySanPhamIdAndType(sanPhamId, "IMAGE")
                    //     .stream()
                    //     .map(AnhVideoSanPham::getLink)
                    //     .toList();
                    String firstImage = anhVideoSanPhamRepo.findFirstBySanPhamIdAndType(sanPhamId, "IMAGE")
                        .map(AnhVideoSanPham::getLink).orElse("https://media-cdn-v2.laodong.vn/storage/newsportal/2025/8/3/1551389/Maqr.jpg");
                    return OrderProductDTO.builder()
                            .productName(ct.getTenSanPham())
                            .productImage(firstImage)
                            .quantity(ct.getSoLuong())
                            .unitPrice(ct.getDonGia())
                            .build();
                })
                .toList();

        return OrderItemDTO.builder()
                .orderId(d.getId())
                .orderCode(d.getMaDon())
                .orderDate(d.getNgayDat() != null ? d.getNgayDat() : LocalDateTime.now()) // check null cho d.getNgayDat()
                .shippingDeadline(d.getNgayDat() != null ? d.getNgayDat().plusDays(2) : LocalDateTime.now().plusDays(2))
                .buyerName(nguoidungrepo.findFirstByNguoiDungId(d.getNguoiMuaId()).orElseThrow().getTen())
                .buyerPhone(nguoidungrepo.findFirstByNguoiDungId(d.getNguoiMuaId()).orElseThrow().getSdt())
                .totalAmount(d.getTienPhaiThanhToan())
                .orderStatus(d.getTrangThai())
                .paymentStatus(d.getTrangThaiThanhToan())
                .items(items)
                .shippingMethod("STANDARD")
                .isProcessed(!"CHO_XAC_NHAN".equals(d.getTrangThai()))
                .billImages(thanhToanRepo.findByDonHangId(d.getId())
                    .map(ThanhToan::getAnhMinhChungId)
                    .flatMap(anhId -> anhVideoSanPhamRepo.findById(anhId))
                    .map(AnhVideoSanPham::getLink)
                    .orElse("https://media-cdn-v2.laodong.vn/storage/newsportal/2025/8/3/1551389/Maqr.jpg")
                )
                .build();
    }

    // DUYỆT ĐƠN HÀNG, TRỪ TỒN KHO, GIẢI PHÓNG KHO ẢO, CẬP NHẬT TRẠNG THÁI ĐƠN, GHI LOG VỚI TRẠNG THÁI CHO_XAC_NHAN > CHO_LAY_HANG
    @Transactional
    public void approveOrder(
        Long id,  // tblDonHang.DH_Id
        UUID userId, // UUID của người đang đăng nhập
        String clientIp 
    ) {

        // DonHang order = donHangRepo.findById(id).orElseThrow();
        DonHang order = donHangRepo.findByIdForUpdate(id)
                        .orElseThrow(() -> new AppException(
                                    "ORDER_NOT_FOUND",
                                    "Không tìm thấy đơn hàng",
                                    404
                        ));

        if (!"CHO_XAC_NHAN".equals(order.getTrangThai())) {
            throw new AppException(
                    "INVALID_ORDER_STATUS",
                    "Đơn hàng không ở trạng thái chờ xác nhận",
                    400
            );
        }

        // // check kho ảo
        // order.getChiTietDonHangs().forEach(item -> {
        //     SanPhamCoSan sp = sanphamcoSanRepo.findBySanPham_Id(item.getSanPhamId())
        //         .orElseThrow(() -> new RuntimeException("Sản phẩm gốc không tồn tại trong kho"));
        //     int available = sp.getSoLuongHienTai() - sp.getSoLuongTamGiu();
        //     if (available < item.getSoLuong()) {
        //         throw new RuntimeException("Không đủ tồn kho");
        //     }
        //     // Trừ SoLuongHienTai (Xuất kho thật).
        //     // Trừ SoLuongTamGiu (Giải phóng kho ảo).
        //     sp.setSoLuongHienTai(sp.getSoLuongHienTai() - item.getSoLuong());
        //     sp.setSoLuongTamGiu(sp.getSoLuongTamGiu() - item.getSoLuong());
        //     sanphamcoSanRepo.save(sp);
        // });
        for (ChiTietDonHang item : order.getChiTietDonHangs()) {

            SanPhamCoSan sp = sanphamcoSanRepo.findBySanPham_Id(item.getSanPhamId())
                    .orElseThrow(() -> new AppException(
                            "PRODUCT_NOT_FOUND",
                            "Sản phẩm không tồn tại",
                            404
                    ));

            int available = sp.getSoLuongHienTai() - sp.getSoLuongTamGiu();

            if (available < item.getSoLuong()) {
                throw new AppException(
                        "OUT_OF_STOCK",
                        "Sản phẩm " + item.getTenSanPham() + " đã hết hàng",
                        400
                );
            }

            sp.setSoLuongHienTai(sp.getSoLuongHienTai() - item.getSoLuong());
            sp.setSoLuongTamGiu(sp.getSoLuongTamGiu() - item.getSoLuong());
        }        
            
        order.setTrangThai("CHO_LAY_HANG");
        order.setNgayCapNhat(LocalDateTime.now());
        // order.setTrangThaiThanhToan("DA_THANH_TOAN");
        donHangRepo.save(order);
        
        // thanhToanRepo.findByDonHangId(id).ifPresent(tt -> {
        //     tt.setTrangThai("DA_THANH_TOAN");
        //     tt.setNgayXacNhan(LocalDateTime.now());
        //     thanhToanRepo.save(tt);
        // });

        auditService.record(
                NKKT_HanhDong.APPROVE_ORDER,
                NKKT_LoaiTacNhan.SELLER,
                userId,
                NKKT_LoaiMucTieu.DON_HANG,
                id,
                order.getMaDon(),
                clientIp
        );
    }

    // HỦY ĐƠN HÀNG, GIẢI PHÓNG KHO ẢO, CẬP NHẬT TRẠNG THÁI ĐƠN, GHI LOG VỚI TRẠNG THÁI LÀ "DA_HUY"
    @Transactional
    public void cancelOrder(
        Long id, // tblDonHang.DH_Id
        CancelOrderRequest request, // Lý do hủy tblDonHang.DH_LyDoHuy
        UUID userId, 
        String clientIp
    ) {

        // DonHang order = donHangRepo.findById(id).orElseThrow();
        DonHang order = donHangRepo.findById(id).orElseThrow();

        order.setTrangThai("DA_HUY");
        order.setTrangThaiHuy(true);
        order.setLyDoHuy(request.getReason());

        order.getChiTietDonHangs().forEach(item -> {
            SanPhamCoSan sp = sanphamcoSanRepo.findBySanPham_Id(item.getSanPhamId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            sp.setSoLuongTamGiu(
                    sp.getSoLuongTamGiu() - item.getSoLuong()
            );
        });

        donHangRepo.save(order);
        thanhToanRepo.findByDonHangId(id).ifPresent(tt -> {
            tt.setTrangThai("DA_HUY");
            tt.setNgayXacNhan(LocalDateTime.now());
            thanhToanRepo.save(tt);
        });

        auditService.record(
                NKKT_HanhDong.CANCEL_ORDER,
                NKKT_LoaiTacNhan.ADMIN,
                userId,
                NKKT_LoaiMucTieu.DON_HANG,
                id,
                order.getMaDon(),
                clientIp
        );
    }

    // TẠO VẬN ĐƠN, CẬP NHẬT TRẠNG THÁI ĐƠN, GHI LOG VỚI TRẠNG THÁI CHO_LAY_HANG > CHO_GIAO_HANG
    @Transactional
    public void shipOrder(
            Long id,
            ShipOrderRequest request,
            UUID userId,
            String clientIp
    ) {
        try {
            System.out.println("=== START SHIP ORDER ===");
            System.out.println("Order ID: " + id);
            System.out.println("User ID: " + userId);

            // 1. Check order tồn tại
            DonHang order = donHangRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng id = " + id));

            System.out.println("Found order: " + order.getMaDon());

            // 2. Validate request
            if (request == null) {
                throw new RuntimeException("Request null");
            }

            if (request.getCarrier() == null || request.getCarrier().isBlank()) {
                throw new RuntimeException("Carrier không được để trống");
            }

            if (request.getTrackingNumber() == null || request.getTrackingNumber().isBlank()) {
                throw new RuntimeException("Tracking number không được để trống");
            }

            // 3. Tạo vận chuyển
            VanChuyen vc = new VanChuyen();
            vc.setVcDhId(id);
            vc.setVcLoai("BEN_THU_BA");
            vc.setVcNhaCungCap(request.getCarrier());
            vc.setVcMaVanDon(request.getTrackingNumber());
            vc.setVcTrangThai("DA_LAY");
            vc.setVcNgayTao(LocalDateTime.now());

            vcRepo.save(vc);
            System.out.println("Saved VanChuyen");

            // 4. Update order
            order.setTrangThai("CHO_GIAO_HANG");
            donHangRepo.save(order);
            System.out.println("Updated order status");

            // 5. Audit
            auditService.record(
                    NKKT_HanhDong.SHIP_ORDER,
                    NKKT_LoaiTacNhan.ADMIN,
                    userId,
                    NKKT_LoaiMucTieu.DON_HANG,
                    id,
                    order.getMaDon(),
                    clientIp
            );
            System.out.println("Audit done");

            System.out.println("=== END SHIP ORDER ===");

        } catch (Exception e) {
            System.err.println("❌ ERROR SHIP ORDER:");
            e.printStackTrace();

            throw new RuntimeException("Ship order failed: " + e.getMessage(), e);
        }
    }

    // Map các trường sortField từ API sang tên cột trong database
    private String mapSortField(String field) {
        return switch (field) {
            case "ngayDat" -> "ngayDat";
            case "hanGuiHang" -> "ngayHoanThanh";
            case "tongTien" -> "tienPhaiThanhToan";
            default -> "ngayTao";
        };
    }

}
