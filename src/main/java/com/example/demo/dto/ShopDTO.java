package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ShopDTO {
    private UUID shopId;
    private String shopName;
    private Boolean checked;
    private List<ProductDTO> products;
}