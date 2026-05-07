package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDTO {
    private Long id;                // ID đơn hàng
    private String maDon;
    private String shopName;         // Tên shop (lấy từ người bán)
    private String status;           // DH_TrangThai
    private LocalDateTime orderDate; // DH_NgayDat
    
    // Dữ liệu từ ChiTietDonHang (Snapshot)
    private String productName;      // CTHD_TenSanPhamSnapshot
    private String productImg;       // Ảnh (Nàng có thể lấy từ trường ảnh snapshot nếu có)
    private Integer quantity;        // CTHD_SoLuong
    private BigDecimal unitPrice;    // CTHD_DonGiaSnapshot
    private BigDecimal totalPrice;   // DH_TienPhaiThanhToan
}
