package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchProductDTO {
    private Long id;
    private String tenSanPham;
    private BigDecimal gia;
    private String anhBia;

    // Shop
    private String tenShop;
    private String avatarShop;
    private String tinhThanh;
    private Boolean verified;

    // Rating
    private Double ratingAvg;

    // Sales
    private Long soLuongDaBan;
}