package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemDTO {
    private Long orderId;
    private String orderCode;
    private LocalDateTime orderDate;
    private LocalDateTime shippingDeadline;

    private String buyerName;
    private String buyerPhone;

    private BigDecimal totalAmount;

    private String orderStatus;
    private String paymentStatus;

    private List<OrderProductDTO> items;

    private String shippingMethod;

    private Boolean isProcessed;
    // ảnh minh chứng 
    private String billImages;
}
