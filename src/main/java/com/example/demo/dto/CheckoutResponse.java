package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutResponse {

    private Long orderId;
    private BigDecimal totalAmount;

    private String bankName;
    private String accountNumber;
    private String accountName;

    private String qrImage;
}
