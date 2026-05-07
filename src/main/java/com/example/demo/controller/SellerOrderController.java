package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CancelOrderRequest;
import com.example.demo.dto.OrderItemDTO;
import com.example.demo.dto.ShipOrderRequest;
import com.example.demo.service.SellerOrderService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/seller/orders")
@RequiredArgsConstructor
public class SellerOrderController {

    private final SellerOrderService service;

    @GetMapping  
    public ApiResponse<Page<OrderItemDTO>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "ALL") String tabStatus, // tblDonHang.DH_TrangThai
            @RequestParam(defaultValue = "ALL") String processingType, //DH_TrangThai
            @RequestParam(required = false) String keyword, // DH_MaDon & tblNguoiDung.ND_Ten
            @RequestParam(defaultValue = "ngayDat") String sortField, // DH_NgayDat, DH_TienPhaiThanhToan
            @RequestParam(defaultValue = "desc") String sortDir,
            HttpServletRequest request
    ) {
        String userIdStr = (String) request.getAttribute("userId");

        if (userIdStr == null) {
            return new ApiResponse<>(
                    "UNAUTHORIZED",
                    null,
                    LocalDateTime.now()
            );
        }

        UUID userId = UUID.fromString(userIdStr);
        
        return new ApiResponse<>(
                "SUCCESS",
                service.getOrders(page, size, tabStatus, processingType, keyword, sortField, sortDir, userId),
                LocalDateTime.now()
        );
    }

    @PostMapping("/{id}/approve")
    public ApiResponse<Void> approve(@PathVariable Long id,
                                     HttpServletRequest request) {
        try{
            String userIdStr = (String) request.getAttribute("userId");

            if (userIdStr == null) {
                return new ApiResponse<>(
                        "UNAUTHORIZED",
                        null,
                        LocalDateTime.now()
                );
                
            }

            UUID userId = UUID.fromString(userIdStr);

            String clientIp = request.getRemoteAddr();
            service.approveOrder(id, userId, clientIp);

            return new ApiResponse<>("APPROVED", null, LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("❌ ERROR CONTROLLER:");
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancel(@PathVariable Long id,
                                    @RequestBody CancelOrderRequest request,
                                    HttpServletRequest requestt
                                ) {

        String userIdStr = (String) requestt.getAttribute("userId");

        if (userIdStr == null) {
            return new ApiResponse<>(
                    "UNAUTHORIZED",
                    null,
                    LocalDateTime.now()
            );
        }

        UUID userId = UUID.fromString(userIdStr);

        String clientIp = requestt.getRemoteAddr();
        service.cancelOrder(id, request, userId, clientIp);

        return new ApiResponse<>("CANCELLED", null, LocalDateTime.now());
    }

    @PostMapping("/{id}/ship")
    public ApiResponse<Void> ship(@PathVariable Long id,
                                  @RequestBody ShipOrderRequest request,
                                  HttpServletRequest requestt) {

        try{
            String userIdStr = (String) requestt.getAttribute("userId");

            if (userIdStr == null) {
                return new ApiResponse<>(
                        "UNAUTHORIZED",
                        null,
                        LocalDateTime.now()
                );
            }

            UUID userId = UUID.fromString(userIdStr);

            System.out.println("SHIP ORDER REQUEST: " + request);
            String clientIp = requestt.getRemoteAddr();
            System.out.println("Client IP: " + clientIp);
            service.shipOrder(id, request, userId, clientIp);

            return new ApiResponse<>("SHIPPED", null, LocalDateTime.now());
        } catch (Exception e) {
            System.err.println("❌ ERROR CONTROLLER:");
            e.printStackTrace();
            throw e;
        }

    }

}
