package com.example.demo.dto;

import java.util.List;

import lombok.Data;

@Data
public class ProductDTO {
    private Long cartItemId;
    private Long productId;
    private String name;
    private Double price;
    private Integer quantity;
    private Boolean checked;
    private List<MediaDTO> media;
}