package com.example.demo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;
 
@Data
public class PaymentShopDTO { 
    private UUID shopId; //

    private Long orderId; //
    private String shopName;

    private String bankName; // 
    private String accountNumber; // 
    private String accountName; // 

    private BigDecimal amount; // 
    private String orderSummary; // 

    private String maNganHang; // Mã ngân hàng
}