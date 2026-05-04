package com.example.demo.dto;

import lombok.Data;

@Data
public class OrderConfirmRequest {
    private Long orderId;
    private Long addressId;
    private String messageToShop;
}