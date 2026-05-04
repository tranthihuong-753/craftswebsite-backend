package com.example.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentInfoResponse {
    private Long orderId;
    private String shopName;
    private String bankName;
    private String accountNumber;
    private String accountName;
    private BigDecimal amount;
    private String orderSummary;
}
