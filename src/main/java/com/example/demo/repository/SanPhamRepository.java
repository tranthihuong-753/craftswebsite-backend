package com.example.demo.repository;

import com.example.demo.dto.SanPhamModerationDTO;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.TaiKhoanNganHang;
import com.example.demo.enums.TrangThaiSanPham;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@EnableJpaRepositories
public interface SanPhamRepository extends JpaRepository<SanPham, Long> {

    long countByThongTinNguoiBan_IdAndTrangThai(
        UUID thongTinNguoiBanId,
        TrangThaiSanPham trangThai
    );

    /// 
    List<SanPham> findByIdIn(List<Long> ids);

}