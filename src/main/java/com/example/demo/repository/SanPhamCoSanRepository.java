package com.example.demo.repository;

import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SanPhamCoSanRepository extends JpaRepository<SanPhamCoSan, Long> {
    Page<SanPhamCoSan> findBySanPhamThongTinNguoiBanIdAndTrangThai(
        UUID sellerId,
        TrangThaiSanPhamCoSan trangThai,
        Pageable pageable
    );

    Page<SanPhamCoSan> findBySanPhamTrangThai(TrangThaiSanPham status, Pageable pageable);
}