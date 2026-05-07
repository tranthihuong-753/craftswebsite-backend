package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PaymentShopDTO;
import com.example.demo.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID; 

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired 
    private PaymentService paymentService;

    // @PostMapping("/info")
    // public Object getPayment(@RequestBody List<Long> productIds) {
    //     return service.buildPayment(productIds);
    // }

    @PostMapping("/info")
    public ResponseEntity<List<PaymentShopDTO>> getPaymentInfo(
            @RequestBody List<Long> orderIds
    ) { 
        return ResponseEntity.ok(
                paymentService.getPaymentInfo(orderIds)
        );
    }

 
    @PostMapping("/{id}/confirm-payment")
    public ResponseEntity<ApiResponse<Void>> confirmPayment(@PathVariable Long id, HttpServletRequest request) {
        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(
                    "UNAUTHORIZED",
                    null,
                    null
            ));
        }

        UUID userId = UUID.fromString(userIdStr);
        String clientIp = request.getRemoteAddr();
        
        paymentService.confirmPaymentReceived(id, userId, clientIp);
        
        return ResponseEntity.ok(new ApiResponse<>(
                "SUCCESS",
                null,
                null
        ));
    }
    
}