package com.example.demo.repository;

import com.example.demo.entity.ChiTietDonHang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChiTietDonHangRepository extends JpaRepository<ChiTietDonHang, Long> {
    // Lay danh sach chi tiet don hang theo id don hang
    List<ChiTietDonHang> findByDonHangId(Long donHangId);
}