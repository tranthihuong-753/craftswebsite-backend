package com.example.demo.dto;

import lombok.Data;

@Data
public class PaymentProofRequest {
    private Long orderId;
    private String imageUrl;
}