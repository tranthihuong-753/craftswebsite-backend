package com.example.demo.repository;

import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SanPhamCoSanRepository extends JpaRepository<SanPhamCoSan, Long> {
    Page<SanPhamCoSan> findBySanPhamThongTinNguoiBanIdAndTrangThai(
        UUID sellerId,
        TrangThaiSanPhamCoSan trangThai,
        Pageable pageable
    );

    Page<SanPhamCoSan> findBySanPhamTrangThai(TrangThaiSanPham status, Pageable pageable);

@Query("""
SELECT s
FROM SanPhamCoSan s
JOIN s.sanPham sp
WHERE sp.thongTinNguoiBan.id = :sellerId
AND s.trangThai = :status
AND (
    :search IS NULL
    OR s.searchText LIKE CONCAT('%', :search, '%')
)
""")
Page<SanPhamCoSan> searchProducts(
        UUID sellerId,
        TrangThaiSanPhamCoSan status,
        String search,
        Pageable pageable
);

}