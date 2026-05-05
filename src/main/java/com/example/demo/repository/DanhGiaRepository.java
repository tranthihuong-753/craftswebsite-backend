package com.example.demo.repository;

import com.example.demo.entity.DanhGia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DanhGiaRepository extends JpaRepository<DanhGia, Long> {
    // Tìm tất cả đánh giá của một sản phẩm
    List<DanhGia> findBySanPhamId(Long sanPhamId);
}