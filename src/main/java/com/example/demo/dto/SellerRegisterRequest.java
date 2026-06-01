// package com.example.demo.dto;

// import lombok.Data;

// import java.math.BigDecimal;
// import java.util.UUID;

// @Data
// public class SellerRegisterRequest {

//     private UUID nguoiDungId;
//     private BigDecimal tienNhanCong;
//     private BigDecimal tienThuongHieu;
//     private String maSoThue;

//     private NganHangDTO nganHang;

//     @Data
//     public static class NganHangDTO {
//         private String maNganHang;
//         private String tenNganHang;
//         private String soTaiKhoan;
//         private String tenTaiKhoan;
//     }
// }

package com.example.demo.dto;

import lombok.Data;

@Data
public class SellerRegisterRequest {

    // ===== THÔNG TIN BÁN HÀNG =====
    private String tienNhanCong;
    private String tienThuongHieu;
    private String maSoThue;

    // ===== ĐỊA CHỈ =====
    private String province;
    private String district;
    private String ward;
    private String cuThe;

    // ===== NGÂN HÀNG =====
    private String maNganHang;
    private String tenNganHang;
    private String soTaiKhoan;
    private String tenTaiKhoan;
}