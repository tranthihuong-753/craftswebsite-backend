package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class ShopDTO {
    private UUID shopId; 
    private String shopName;
    private String avatar;
    private String diaChi;
    private Boolean checked;
    private List<ProductDTO> products;
    private Long orderId;
    private BigDecimal tienPhaiThanhToan;
    private BigDecimal phiVanChuyen;
}