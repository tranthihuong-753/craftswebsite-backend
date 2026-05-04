package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProductDTO {  
    private Long cartItemId;
    private Long productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private Boolean checked;
    private String coverUrls;
    private List<String> imageUrls;
    private List<String> videoUrls;
}