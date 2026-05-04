package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.CheckoutRequest;
import com.example.demo.dto.InitOrderRequest;
import com.example.demo.dto.OrderConfirmRequest;
import com.example.demo.dto.PaymentInfoResponse;
import com.example.demo.dto.PaymentProofRequest;
import com.example.demo.dto.ShopDTO;
import com.example.demo.entity.DonHang;
import com.example.demo.service.JwtService;
import com.example.demo.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired private OrderService orderService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(@RequestBody CheckoutRequest req) {

        return ResponseEntity.ok(orderService.checkout(req));
    }
 
    // @PostMapping("/init")
    // public List<DonHang> initOrder(@RequestBody InitOrderRequest request,
    //                               HttpServletRequest httpRequest) {
    //     return orderService.initOrder(request.getCartItemIds(), httpRequest);
    // }

    @Autowired private JwtService jwtService;


    @PostMapping("/init")

    public ResponseEntity<List<ShopDTO>> initOrders(@RequestBody Map<String, List<Long>> payload, HttpServletRequest request) {

        UUID userId = UUID.fromString((String) request.getAttribute("userId"));

        return ResponseEntity.ok(orderService.initOrders(userId, payload.get("cartItemIds")));

    }


    @PostMapping("/confirm-info")

    public ResponseEntity<?> confirmInfo(@RequestBody List<OrderConfirmRequest> requests) {

        orderService.confirmOrderInfo(requests);

        return ResponseEntity.ok().build();

    }


    @GetMapping("/payment/info")

    public ResponseEntity<List<PaymentInfoResponse>> getPaymentInfo(@RequestParam List<Long> orderIds) {

        return ResponseEntity.ok(orderService.getPaymentInfos(orderIds));

    }


    @PostMapping("/payment/upload-proof")
    public ResponseEntity<?> uploadProof(
        @RequestBody List<PaymentProofRequest> requests
    ) {
        orderService.uploadPaymentProof(requests);
        return ResponseEntity.ok().build();
    }


} 
