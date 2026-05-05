package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SearchRequest {
    private String q;
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String province;
    private Double rating;
    private String type;
    private String sortBy;

    private int page = 0;
    private int size = 20;
}
    
