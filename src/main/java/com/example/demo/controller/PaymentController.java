package com.example.demo.controller;

import com.example.demo.dto.PaymentShopDTO;
import com.example.demo.service.PaymentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    // @PostMapping("/info")
    // public Object getPayment(@RequestBody List<Long> productIds) {
    //     return service.buildPayment(productIds);
    // }

    @PostMapping("/info")
    public ResponseEntity<List<PaymentShopDTO>> getPaymentInfo(
            @RequestBody List<Long> orderIds
    ) { 
        return ResponseEntity.ok(
                service.getPaymentInfo(orderIds)
        );
    }
}