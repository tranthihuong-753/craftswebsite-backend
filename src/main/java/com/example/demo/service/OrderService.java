package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.CheckoutRequest;
import com.example.demo.dto.CheckoutResponse;
import com.example.demo.dto.OrderConfirmRequest;
import com.example.demo.dto.PaymentInfoResponse;
import com.example.demo.dto.PaymentProofRequest;
import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.ShopDTO;
import com.example.demo.entity.AnhVideo;
import com.example.demo.entity.AnhVideoSanPham;
import com.example.demo.entity.ChiTietDonHang;
import com.example.demo.entity.DiaChi;
import com.example.demo.entity.DonHang;
import com.example.demo.entity.GioHang;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.entity.TaiKhoanNganHang;
import com.example.demo.entity.ThanhToan;
import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.enums.TrangThaiTaiKhoanNganHang;
import com.example.demo.repository.AnhVideoRepository;
import com.example.demo.repository.AnhVideoSanPhamRepository;
import com.example.demo.repository.ChiTietDonHangRepository;
import com.example.demo.repository.DiaChiRepository;
import com.example.demo.repository.DonHangRepository;
import com.example.demo.repository.GioHangRepository;
import com.example.demo.repository.SanPhamCoSanRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.repository.TaiKhoanNganHangRepository;
import com.example.demo.repository.ThanhToanRepository;
import com.example.demo.repository.ThongTinNguoiBanRepository;
import com.example.demo.repository.VaiTroNguoiDungRepository;
import com.example.demo.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final ThongTinNguoiBanRepository thongTinNguoiBanRepository;
    @Autowired private SanPhamRepository sanPhamRepo;
    @Autowired private TaiKhoanNganHangRepository bankRepo;
    @Autowired private AnhVideoSanPhamRepository anhVideoSanPhamRepo;

    public CheckoutResponse checkout(CheckoutRequest req) {

        // STEP 1: get products
        List<SanPham> products = sanPhamRepo.findByIdIn(req.getCartItemIds());

        if (products.isEmpty()) {
            throw new RuntimeException("No products found");
        }

        // STEP 2: group by seller
        Map<ThongTinNguoiBan, List<SanPham>> bySeller =
                products.stream()
                        .collect(Collectors.groupingBy(SanPham::getThongTinNguoiBan));

        // STEP 3: xử lý từng seller (demo 1 seller đầu tiên)
        Map.Entry<ThongTinNguoiBan, List<SanPham>> entry =
                bySeller.entrySet().iterator().next();

        ThongTinNguoiBan seller = entry.getKey();
        List<SanPham> sellerProducts = entry.getValue();

        // STEP 4: get bank ACTIVE
        TaiKhoanNganHang bank = bankRepo
                .findFirstByTtnbIdAndTrangThai(
                        seller,
                        TrangThaiTaiKhoanNganHang.CON_SU_DUNG
                )
                .orElseThrow(() -> new RuntimeException("No active bank account"));

        // STEP 5: calculate total
        BigDecimal total = sellerProducts.stream()
                .map(p -> {
                    // giả sử giá nằm trong ThongTinNguoiBan hoặc product
                    return BigDecimal.valueOf(100000); // demo
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // STEP 6: tạo order (demo id)
        Long orderId = System.currentTimeMillis();

        // STEP 7: generate QR
        String qr = generateVietQR(bank, total, orderId);

        return new CheckoutResponse(
                orderId,
                total,
                bank.getTenNganHang(),
                bank.getSoTaiKhoan(),
                bank.getTenTaiKhoan(),
                qr
        );
    }

    // ===== VIETQR SIMPLE MOCK =====
    private String generateVietQR(TaiKhoanNganHang bank,
                                  BigDecimal amount,
                                  Long orderId) {

        String content = bank.getMaNganHang()
                + "|" + bank.getSoTaiKhoan()
                + "|" + amount
                + "|ORDER:" + orderId;

        return Base64.getEncoder().encodeToString(content.getBytes());
    }

    @Autowired
    private GioHangRepository gioHangRepo;
    @Autowired
    private DonHangRepository donHangRepo;
    @Autowired
    private ChiTietDonHangRepository chiTietRepo;
    @Autowired
    private SanPhamCoSanRepository spcsRepo;
    @Autowired
    private DiaChiRepository diaChiRepo;
    @Autowired
    private VaiTroNguoiDungRepository vaitroNguoiDungRepo;
    @Autowired
    private JwtService jwtService;
    @Autowired 
    private SanPhamCoSanRepository sanphamcoSanRepo;

    @Transactional 
    public List<DonHang> initOrder(List<Long> cartItemIds, HttpServletRequest request) {

        // ===== 1. Lấy user từ token =====
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        UUID userId =jwtService.extractUserId(token);

        // ===== 2. Lấy cart items =====
        List<GioHang> cartItems = gioHangRepo.findAllById(cartItemIds);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart rỗng");
        }

        // ===== 3. GROUP THEO SELLER =====
        Map<UUID, List<GioHang>> grouped = cartItems.stream()
        .collect(Collectors.groupingBy(gh -> 
                gh.getSanPham().getThongTinNguoiBan().getId()
        ));
                
        List<DonHang> result = new ArrayList<>();

        
        // ===== 4. LOOP TỪNG SHOP =====
        for (Map.Entry<UUID, List<GioHang>> entry : grouped.entrySet()) {

            UUID sellerId = entry.getKey();
            List<GioHang> items = entry.getValue();

            // ===== 5. Tạo đơn =====
            DonHang donHang = new DonHang();
            donHang.setNguoiMuaId(userId);
            donHang.setNguoiBanId(sellerId);

            donHang.setTrangThai("CHO_XAC_NHAN");
            donHang.setTrangThaiThanhToan("CHUA_THANH_TOAN"); 
            donHang.setNgayDat(LocalDateTime.now());

            // ===== 6. SNAPSHOT ADDRESS =====
            DiaChi buyerAddress = diaChiRepo.findDefaultByUser(vaitroNguoiDungRepo.findUserByNguoiDungId(userId)
                    .orElseThrow(() -> new RuntimeException("BUYER role not found"))
                    .getId());
                if (buyerAddress == null) { new RuntimeException("Người mua chưa có địa chỉ mặc định");}
            
            DiaChi sellerAddress = diaChiRepo.findDefaultByUser(vaitroNguoiDungRepo.findSellerByNguoiDungId(userId)
                    .orElseThrow(() -> new RuntimeException("Seller role not found"))
                    .getId()
                );
                if (sellerAddress == null) { new RuntimeException("Người bán chưa có địa chỉ mặc định");}
            if (buyerAddress != null) {
                donHang.setDiaChiNguoiMuaId(buyerAddress.getTinhThanh() + ", " 
                    + buyerAddress.getQuanHuyen() + ", " 
                    + buyerAddress.getPhuongXa() + ", " 
                    + buyerAddress.getCuThe());
            }
            donHang.setDiaChiNguoiBanId(sellerAddress.getTinhThanh() + ", " 
                    + sellerAddress.getQuanHuyen() + ", " 
                    + sellerAddress.getPhuongXa() + ", " 
                    + sellerAddress.getCuThe());

            // ===== 7. FIX TIỀN =====
            donHang.setPhiSan(BigDecimal.ONE);
            donHang.setTienThue(BigDecimal.ONE);
            donHang.setChietKhau(BigDecimal.ONE);

            BigDecimal tongTienHang = BigDecimal.ZERO;

            // ===== 8. SAVE TRƯỚC để có ID =====
            donHangRepo.save(donHang);

            // ===== 9. LOOP ITEM =====
            int index = 1;
            for (GioHang gh : items) {

                SanPham sp = sanPhamRepo.findById(gh.getSanPham().getId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

                SanPhamCoSan spcs = sanphamcoSanRepo.findBySanPham_Id(sp.getId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

                // ===== 🔥 CHECK TỒN KHO =====
                int soLuongKhaDung = spcs.getSoLuongHienTai() - spcs.getSoLuongTamGiu();

                if (soLuongKhaDung < gh.getSoLuong()) {
                throw new RuntimeException(
                        "Sản phẩm " + spcs.getId() + " không đủ hàng. Còn: " + soLuongKhaDung
                );
                }

                BigDecimal thanhTien = gh.getDonGiaSnapshot()
                        .multiply(BigDecimal.valueOf(gh.getSoLuong()));

                tongTienHang = tongTienHang.add(thanhTien);

                // ===== CREATE CHI TIET =====
                ChiTietDonHang ct = new ChiTietDonHang();
                ct.setDonHang(donHang);

                ct.setMaChiTiet(donHang.getMaDon() + "-" + index++);
                ct.setSanPhamId(gh.getSanPham().getId());
                ct.setSoLuong(gh.getSoLuong());

                ct.setTenSanPham("SP-" + gh.getSanPham().getId());
                ct.setDonGia(gh.getDonGiaSnapshot());
                ct.setThanhTien(thanhTien);

                ct.setThueSuat(BigDecimal.ONE);
                ct.setLoaiThue("VAT");
                ct.setTienThue(BigDecimal.ONE);

                chiTietRepo.save(ct);

                // ===== STOCK RESERVATION =====
                spcs.setSoLuongTamGiu(
                        spcs.getSoLuongTamGiu() + gh.getSoLuong()
                );
                spcsRepo.save(spcs);
            }

            // ===== 10. TÍNH TIỀN =====
            donHang.setTongTienHang(tongTienHang);
            donHang.setTienPhaiThanhToan(
                    tongTienHang.add(BigDecimal.valueOf(3))
            );

            donHangRepo.save(donHang);

            result.add(donHang);
        }

        // ===== 11. DELETE CART =====
        gioHangRepo.deleteAll(cartItems);

        return result;
    }


    @Autowired private AnhVideoRepository anhVideoRepo;

    @Autowired private ThanhToanRepository thanhToanRepo;

    @Autowired private NhatKyKiemToanService auditLog;

    OrderService(ThongTinNguoiBanRepository thongTinNguoiBanRepository) {
        this.thongTinNguoiBanRepository = thongTinNguoiBanRepository;
    }


    @Transactional
    public List<ShopDTO> initOrders(UUID buyerId, List<Long> cartItemIds) {
        List<GioHang> cartItems = gioHangRepo.findAllById(cartItemIds);
        if (cartItems.isEmpty()) throw new RuntimeException("Giỏ hàng trống");

        // Nhóm theo Shop
        Map<UUID, List<GioHang>> groups = cartItems.stream()
            .collect(Collectors.groupingBy(gh -> gh.getSanPham().getThongTinNguoiBan().getId()));

        List<ShopDTO> response = new ArrayList<>();

        for (Map.Entry<UUID, List<GioHang>> entry : groups.entrySet()) {
            UUID sellerId = entry.getKey();
            List<GioHang> items = entry.getValue();
            ThongTinNguoiBan sellerInfo = items.get(0).getSanPham().getThongTinNguoiBan();
            
            // 1. Tạo DonHang (TAO)
            DonHang dh = new DonHang();
            dh.setMaDon("ORD-" + System.currentTimeMillis());
            dh.setNguoiMuaId(buyerId);
            dh.setNguoiBanId(sellerId);
            dh.setTrangThai("CHO_XAC_NHAN");
            dh.setTrangThaiThanhToan("CHUA_THANH_TOAN");
            dh.setNgayTao(LocalDateTime.now());
            
            // Snapshot địa chỉ mặc định
            diaChiRepo.findFirstByVaiTroNguoiDung_NguoiDung_IdAndThietLapMacDinh(sellerId, 1)
                .ifPresent(addr -> dh.setDiaChiNguoiBanId(addr.getDiaChiDayDu()));
            diaChiRepo.findFirstByVaiTroNguoiDung_NguoiDung_IdAndThietLapMacDinh(buyerId, 1)
                .ifPresent(addr -> dh.setDiaChiNguoiMuaId(addr.getDiaChiDayDu()));

            dh.setPhiSan(BigDecimal.ONE);
            dh.setTienThue(BigDecimal.ONE);
            dh.setTienShip(BigDecimal.ZERO); 
            donHangRepo.save(dh);

            // 2. Tạo ChiTiet & Giữ hàng
            BigDecimal tongTienHang = BigDecimal.ZERO;
            List<ProductDTO> productDTOs = new ArrayList<>();

            for (GioHang gh : items) {
                SanPham sp = sanPhamRepo.findById(gh.getSanPham().getId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

                SanPhamCoSan spcs = sanphamcoSanRepo.findBySanPham_Id(sp.getId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));
                
                // Giữ hàng ảo
                spcs.setSoLuongTamGiu(spcs.getSoLuongTamGiu() + gh.getSoLuong());
                spcsRepo.save(spcs);

                ChiTietDonHang ct = new ChiTietDonHang();
                ct.setDonHang(dh);
                ct.setSanPhamId(gh.getSanPham().getId());
                ct.setTenSanPham(gh.getSanPham().getTen());
                ct.setSoLuong(gh.getSoLuong());
                ct.setDonGia(gh.getDonGiaSnapshot());
                ct.setThanhTien(gh.getDonGiaSnapshot().multiply(BigDecimal.valueOf(gh.getSoLuong())));
                chiTietRepo.save(ct);

                tongTienHang = tongTienHang.add(ct.getThanhTien());
                
                // Chuyển sang DTO để React hiển thị
                ProductDTO pDto = new ProductDTO();
                pDto.setCartItemId(gh.getId());
                pDto.setProductId(gh.getSanPham().getId());
                pDto.setName(ct.getTenSanPham());
                pDto.setPrice(ct.getDonGia());
                pDto.setQuantity(ct.getSoLuong());
                List<AnhVideoSanPham> images = anhVideoSanPhamRepo.findBySanPhamIdAndType(gh.getSanPham().getId(), "IMAGE");
                List<String> imageUrls = new ArrayList<>();
                if (images != null && !images.isEmpty()) {
                    imageUrls.add(images.get(0).getLink()); 
                }
                if (!imageUrls.isEmpty()) {
                    pDto.setCoverUrls(imageUrls.get(0));
                    // bo phan tu tu trong setCoverUrls khoi imageUrls
                    imageUrls.remove(0);
                }
                List<AnhVideoSanPham> videos = anhVideoSanPhamRepo.findBySanPhamIdAndType(gh.getSanPham().getId(), "VIDEO");
                List<String> videoUrls = new ArrayList<>();
                if (videos != null && !videos.isEmpty()) {
                    videoUrls.add(videos.get(0).getLink());
                }
                pDto.setImageUrls(imageUrls);
                pDto.setVideoUrls(videoUrls);
                // Giả sử có mapper để lấy cover/imageUrls từ sanPham...
                productDTOs.add(pDto);
            }

            dh.setTongTienHang(tongTienHang);
            dh.setTienPhaiThanhToan(tongTienHang.add(dh.getPhiSan()).add(dh.getTienThue()));
            donHangRepo.save(dh);

            // 3. Đóng gói vào ShopDTO
            ShopDTO sDto = new ShopDTO();
            sDto.setOrderId(dh.getId()); // Thêm field orderId vào ShopDTO để React dùng
            sDto.setShopId(sellerId);
            sDto.setShopName(sellerInfo.getNguoiDung().getTen());
            sDto.setProducts(productDTOs);
            if(dh.getTienPhaiThanhToan() != null) sDto.setTienPhaiThanhToan(dh.getTienPhaiThanhToan());
            else sDto.setTienPhaiThanhToan(BigDecimal.ZERO);
            if(dh.getTienShip() != null) sDto.setPhiVanChuyen(dh.getTienShip());
            else sDto.setPhiVanChuyen(BigDecimal.ZERO);
            // sDto.setChecked(true); // Mặc định chọn
            sDto.setAvatar(sellerInfo.getNguoiDung().getAnhVideo_anhChanDung().getLink());
            sDto.setDiaChi(
                vaitroNguoiDungRepo
                    .findSellerByNguoiDungId(
                        sellerInfo.getNguoiDung().getId()
                    )
                    .flatMap(vtnd -> diaChiRepo.findDefaultByVaiTroNguoiDungId(vtnd.getId()))
                    .map(DiaChi::getDiaChiDayDu)
                    .orElse("Chưa có địa chỉ")
            );
            response.add(sDto);
        }
        
        gioHangRepo.deleteAll(cartItems);
        return response;
    }

    @Transactional

    public void confirmOrderInfo(List<OrderConfirmRequest> requests) {

        for (OrderConfirmRequest req : requests) {

            DonHang dh = donHangRepo.findById(req.getOrderId()).orElseThrow();

            

            // Snapshot địa chỉ người mua mới

            DiaChi addr = diaChiRepo.findById(req.getAddressId()).orElseThrow();

            dh.setDiaChiNguoiMuaId(addr.getDiaChiDayDu());

            dh.setLoiNhanChoShop(req.getMessageToShop());

            

            // Tính phí ship mặc định 15k

            dh.setTienShip(new BigDecimal("15000"));

            dh.setTienPhaiThanhToan(dh.getTongTienHang().add(dh.getTienShip()).add(dh.getPhiSan()).add(dh.getTienThue()));

            

            donHangRepo.save(dh);

        }

    }



    public List<PaymentInfoResponse> getPaymentInfos(List<Long> orderIds) {

        List<DonHang> orders = donHangRepo.findAllById(orderIds);

        return orders.stream().map(dh -> {

            TaiKhoanNganHang bank = bankRepo.findFirstByTtnbId_IdAndTrangThai(dh.getNguoiBanId(), TrangThaiTaiKhoanNganHang.CON_SU_DUNG)

                .orElseThrow();

            return new PaymentInfoResponse(

                dh.getId(),

                dh.getNguoiBanId().toString(), // Shop name logic

                bank.getTenNganHang(),

                bank.getSoTaiKhoan(),

                bank.getTenTaiKhoan(),

                dh.getTienPhaiThanhToan(),

                "Thanh toan don hang " + dh.getMaDon()

            );

        }).collect(Collectors.toList());

    }

    @Transactional
    public void uploadPaymentProof(List<PaymentProofRequest> requests) {

        for (PaymentProofRequest req : requests) {

            DonHang dh = donHangRepo.findById(req.getOrderId()).orElseThrow();

            // 1. Lưu ảnh (KHÔNG cần save file nữa)
            AnhVideo proofImg = new AnhVideo();
            proofImg.setLink(req.getImageUrl());
            proofImg.setType("IMAGE");

            anhVideoRepo.save(proofImg); // ⚠️ QUAN TRỌNG (bạn đang thiếu đoạn này)

            // 2. Tạo ThanhToan
            ThanhToan tt = new ThanhToan();
            tt.setDonHang(dh);
            tt.setSoTien(dh.getTienPhaiThanhToan());
            tt.setTrangThai("DANG_XU_LY");
            tt.setAnhMinhChungId(proofImg.getId());
            tt.setNgayTao(LocalDateTime.now());

            thanhToanRepo.save(tt);

            // 3. Update đơn hàng
            dh.setTrangThaiThanhToan("DANG_XU_LY");
            dh.setNgayDat(LocalDateTime.now());

            donHangRepo.save(dh);
        }
    }

}
