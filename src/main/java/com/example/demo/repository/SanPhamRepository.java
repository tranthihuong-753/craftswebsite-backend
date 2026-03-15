package com.example.demo.repository;

import com.example.demo.entity.SanPham;
import com.example.demo.enums.TrangThaiSanPham;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {

    long countByThongTinNguoiBan_IdAndTrangThai(
        UUID thongTinNguoiBanId,
        TrangThaiSanPham trangThai
    );
}