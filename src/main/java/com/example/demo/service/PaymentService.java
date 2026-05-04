package com.example.demo.service;

import com.example.demo.dto.PaymentShopDTO;
import com.example.demo.entity.*;
import com.example.demo.enums.TrangThaiTaiKhoanNganHang;
import com.example.demo.repository.*;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

}