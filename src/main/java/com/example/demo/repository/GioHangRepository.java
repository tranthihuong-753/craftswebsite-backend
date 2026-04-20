package com.example.demo.repository;

import com.example.demo.entity.GioHang;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GioHangRepository extends JpaRepository<GioHang, Long> {

    // THEM SP VAO GIO HANG 
    Optional<GioHang> findByVaiTroNguoiDung_IdAndSanPham_Id(UUID vtndId, Long spId);
    
    // 
    List<GioHang> findByVaiTroNguoiDung_Id(UUID vtndId);

    //
    List<GioHang> findByVaiTroNguoiDung_NguoiDung_Id(UUID userId);

}