package com.example.demo.service;

import com.example.demo.dto.PaymentShopDTO;
import com.example.demo.entity.*;
import com.example.demo.enums.NKKT_HanhDong;
import com.example.demo.enums.NKKT_LoaiMucTieu;
import com.example.demo.enums.NKKT_LoaiTacNhan;
import com.example.demo.enums.TrangThaiTaiKhoanNganHang;
import com.example.demo.exception.AppException;
import com.example.demo.repository.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

        @Autowired 
        private DonHangRepository donHangRepository;

        @Autowired
    private SanPhamRepository sanPhamRepo;

    @Autowired
    private TaiKhoanNganHangRepository taiKhoanNganHangRepository;

    @Autowired
    private ThongTinNguoiBanRepository thongtinNguoiBanRepository;

    public List<PaymentShopDTO> buildPayment(List<Long> productIds) {

        List<SanPham> products = sanPhamRepo.findByIdIn(productIds);

        Map<ThongTinNguoiBan, List<SanPham>> grouped =
                products.stream()
                        .collect(Collectors.groupingBy(SanPham::getThongTinNguoiBan));

        List<PaymentShopDTO> result = new ArrayList<>();

        for (var entry : grouped.entrySet()) {

            ThongTinNguoiBan seller = entry.getKey();
            List<SanPham> list = entry.getValue();

            // lấy bank active
            TaiKhoanNganHang bank = taiKhoanNganHangRepository
                    .findFirstByTtnbIdAndTrangThai(
                            seller,
                            TrangThaiTaiKhoanNganHang.CON_SU_DUNG
                    )
                    .orElseThrow(() -> new RuntimeException("No bank found"));

            // tính tiền
            BigDecimal total = list.stream()
                    .map(sp -> BigDecimal.valueOf(100000)) // thay bằng giá thật
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            PaymentShopDTO dto = new PaymentShopDTO();
            dto.setShopId(seller.getId());
            dto.setShopName(seller.getNguoiDung().getTen());

            dto.setBankName(bank.getTenNganHang());
            dto.setAccountNumber(bank.getSoTaiKhoan());
            dto.setAccountName(bank.getTenTaiKhoan());

            dto.setAmount(total);
            dto.setOrderSummary("Thanh toán " + list.size() + " sản phẩm");

            result.add(dto);
        }

        return result;
    }

    // LẤY THÔNG TIN THANH TOÁN THEO ORDER ID
    public List<PaymentShopDTO> getPaymentInfo(List<Long> orderIds) {

        List<DonHang> orders = donHangRepository.findAllByIdIn(orderIds);
 
        if (orders.isEmpty()) {
            throw new RuntimeException("Không tìm thấy đơn hàng");
        }

        List<PaymentShopDTO> result = new ArrayList<>();

        for (DonHang order : orders) {

            UUID shopId = order.getNguoiBanId();

            // ===== 1. lấy thông tin người bán =====
            // (TTNB_Id == nguoiBanId)
            // Không cần query lại nếu bạn đã map entity chuẩn
            // nhưng ở đây an toàn thì query bank theo TTNB

            ThongTinNguoiBan ttnd = thongtinNguoiBanRepository.findById(shopId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người bán"));

            TaiKhoanNganHang bank = taiKhoanNganHangRepository
                    .findFirstByTtnbIdAndTrangThai(
                            ttnd,
                            TrangThaiTaiKhoanNganHang.CON_SU_DUNG
                    )
                    .orElseThrow(() -> new RuntimeException(
                            "Không tìm thấy tài khoản ngân hàng cho shop: " + shopId
                    ));

            // ===== 2. build DTO =====
            PaymentShopDTO dto = new PaymentShopDTO();

            dto.setShopId(order.getNguoiBanId()); 
            dto.setShopName(bank.getTtnbId().getNguoiDung().getTen()); 

            dto.setBankName(bank.getTenNganHang());
            dto.setAccountNumber(bank.getSoTaiKhoan());
            dto.setAccountName(bank.getTenTaiKhoan());

            dto.setAmount(order.getTienPhaiThanhToan());

            dto.setOrderSummary("DH" + order.getMaDon());

            dto.setMaNganHang(bank.getMaNganHang()); 

            dto.setOrderId(order.getId());

            result.add(dto);
        }

        return result;
    }

    @Autowired private DonHangRepository donHangRepo;
    @Autowired private ThanhToanRepository thanhToanRepo;
    @Autowired private SanPhamCoSanRepository spcsRepo;
    @Autowired private AuditService auditService; // Vùng 4: Log

    @Transactional 
    public void confirmPaymentReceived(Long orderId, UUID sellerId, String clientIp) {
        // 1. Tìm đơn hàng
        // DonHang order = donHangRepo.findById(orderId)
        //         .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        DonHang order = donHangRepo.findByIdForUpdate(orderId)
            .orElseThrow(() -> new AppException(
                    "ORDER_NOT_FOUND",
                    "Không tìm thấy đơn hàng",
                    404
            ));

        // 2. Tìm bản ghi thanh toán tương ứng
        // ThanhToan payment = thanhToanRepo.findByDonHangId(orderId)
        //         .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin thanh toán"));
        ThanhToan payment = thanhToanRepo.findByDonHangId(orderId)
        .orElseThrow(() -> new AppException(
                "PAYMENT_NOT_FOUND",
                "Không tìm thấy thanh toán",
                404
        ));

        // 3. Logic nghiệp vụ: Chỉ xác nhận khi đang ở trạng thái DANG_XU_LY (đã có bill)
        // if (!"DANG_XU_LY".equals(payment.getTrangThai())) {
        //     System.out.println("DB payment status: " + payment.getTrangThai());
        //     throw new RuntimeException("Trạng thái thanh toán không hợp lệ để xác nhận");
        // }
        if (!"DANG_XU_LY".equals(payment.getTrangThai())) {
                throw new AppException(
                        "PAYMENT_INVALID_STATE",
                        "Chỉ xác nhận khi khách đã gửi bill",
                        400
                );
        }

        // 4. Cập nhật bảng ThanhToan
        payment.setTrangThai("DA_THANH_TOAN");
        payment.setNgayXacNhan(LocalDateTime.now());
        // payment.setNguoiXacNhan(sellerId); // Nếu bạn dùng Long cho ID người xác nhận thì cần convert UUID
        thanhToanRepo.save(payment);

        // 5. Cập nhật bảng DonHang
        order.setTrangThaiThanhToan("DA_THANH_TOAN");
        order.setNgayCapNhat(LocalDateTime.now());
        donHangRepo.save(order);

        // 6. Xử lý kho (Giải phóng kho ảo, trừ kho thật)
        order.getChiTietDonHangs().forEach(item -> {
            SanPhamCoSan sp = spcsRepo.findBySanPham_Id(item.getSanPhamId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại trong kho"));
            
            sp.setSoLuongHienTai(sp.getSoLuongHienTai() - item.getSoLuong()); // Trừ kho thật
            sp.setSoLuongTamGiu(sp.getSoLuongTamGiu() - item.getSoLuong()); // Trừ kho ảo
            spcsRepo.save(sp);
        });

        // 7. Ghi Log hệ thống (Vùng 4)
        auditService.record(NKKT_HanhDong.CONFIRM_PAYMENT, NKKT_LoaiTacNhan.SELLER, sellerId, NKKT_LoaiMucTieu.DON_HANG, orderId, "Tiền đã về tài khoản", clientIp);
    }
 
}