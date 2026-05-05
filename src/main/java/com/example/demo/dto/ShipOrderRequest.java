package com.example.demo.dto;

import lombok.Data;

@Data
public class ShipOrderRequest {
    private String carrier;
    private String trackingNumber;
}
