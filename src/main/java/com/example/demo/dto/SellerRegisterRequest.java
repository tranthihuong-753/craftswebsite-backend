package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SellerRegisterRequest {

    private UUID nguoiDungId;
    private BigDecimal tienNhanCong;
    private BigDecimal tienThuongHieu;
    private String maSoThue;

    private NganHangDTO nganHang;

    @Data
    public static class NganHangDTO {
        private String maNganHang;
        private String tenNganHang;
        private String soTaiKhoan;
        private String tenTaiKhoan;
    }
}