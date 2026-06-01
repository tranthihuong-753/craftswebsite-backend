package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ThanhToan;

public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {
    // Tìm ThanhToan theo DonHang ID
    Optional<ThanhToan> findByDonHangId(Long donHangId);
    
}