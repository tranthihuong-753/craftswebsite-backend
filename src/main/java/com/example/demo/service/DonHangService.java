package com.example.demo.service;

import com.example.demo.dto.OrderResponse;
import com.example.demo.entity.DonHang;
import com.example.demo.repository.DonHangRepository;

import jakarta.transaction.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DonHangService {

    @Autowired
    private DonHangRepository donHangRepository;
    
    @Transactional
    public OrderResponse confirmReceived(Long orderId, String username) {
        // 1. Tìm đơn hàng
        DonHang order = donHangRepository.findById(orderId).orElse(null);

        // 3. Kiểm tra trạng thái đơn hàng
        if (!"DANG_GIAO".equals(order.getTrangThai())) {
            throw new IllegalStateException("Đơn hàng phải ở trạng thái Đang giao mới có thể xác nhận");
        }

        // 4. Update trạng thái
        order.setTrangThai("HOAN_THANH");
        order.setNgayHoanThanh(LocalDateTime.now());
        donHangRepository.save(order);

        return new OrderResponse(orderId, "HOAN_THANH", order.getNgayHoanThanh(), "Xác nhận thành công!");
    }
    
}