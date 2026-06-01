package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderProductDTO {
    private String productName;
    private String productImage;
    private Integer quantity;
    private BigDecimal unitPrice;
}
