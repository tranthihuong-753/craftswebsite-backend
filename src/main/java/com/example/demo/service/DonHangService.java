package com.example.demo.service;

import com.example.demo.dto.OrderResponse;
import com.example.demo.dto.ReviewRequest;
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
    public void confirmAndReview(Long orderId, UUID userId, ReviewRequest review) {
        // 1. Tìm đơn hàng
        DonHang order = donHangRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        // 2. Kiểm tra quyền (Chỉ người mua mới được chốt)
        if (!order.getNguoiMuaId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền thực hiện hành động này");
        }

        // 3. Cập nhật trạng thái
        order.setTrangThai("DA_GIAO"); // Chuyển từ CHO_GIAO_HANG sang DA_GIAO
        order.setNgayHoanThanh(LocalDateTime.now());

        // 4. Lưu đánh giá vào trường Ghi chú hoặc bảng Review
        // Ở đây mình tận dụng trường DH_GhiChu để demo cho nhanh nếu Nàng chưa có bảng Review riêng
        String reviewLog = String.format("[Rating: %d sao] - Nội dung: %s", 
                                        review.getRating(), review.getComment());
        order.setGhiChu(reviewLog);

        donHangRepository.save(order);
    }

    public List<DonHang> findByNguoiMuaId(UUID userid) {
        return donHangRepository.findByNguoiMuaId(userid);
    }

    public List<DonHang> findByNguoiMuaIdAndStatus(UUID userid, String status) {
        return donHangRepository.findByNguoiMuaIdAndTrangThai(userid, status);
    }
}