package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;

import com.example.demo.enums.LoaiSP;

@Data
public class SellerProductDTO {

    private Long id;

    private LoaiSP loaiSanPham;

    private BigDecimal gia;

    private String moTa;

    private int soLuongBanDau;

    private Long chungChiId;

    private String image;
}