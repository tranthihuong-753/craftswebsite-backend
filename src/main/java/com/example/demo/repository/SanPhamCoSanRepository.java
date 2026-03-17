package com.example.demo.repository;

import com.example.demo.dto.SanPhamModerationDTO;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;


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
            OR s.timKiem LIKE CONCAT('%', :search, '%')
        )
    """)
    Page<SanPhamCoSan> searchProducts(
            UUID sellerId,
            TrangThaiSanPhamCoSan status,
            String search,
            Pageable pageable
    );

    // @Query("SELECT spcs.id as id, " +
    //        "avsp.url as anhSanPham, " +
    //        "nd.ten as tenSeller, " +
    //        "sp.ngayTao as ngayTao " +
    //        "FROM SanPhamCoSan spcs " +
    //        "JOIN spcs.sanPham sp " +
    //        "JOIN sp.thongTinNguoiBan ttnb " +
    //        "JOIN ttnb.nguoiDung nd " +
    //        "LEFT JOIN AnhVideoSanPham avsp ON avsp.sanPham = sp. id AND avsp.isMain = true " +
    //        "WHERE spcs.trangThai = :status " +
    //        "AND (:search IS NULL OR :search = '' OR spcs.timKiem LIKE %:search%)")
    // Page<SanPhamModerationDTO> findModerationProducts(
    //         @Param("status") TrangThaiSanPhamCoSan status,
    //         @Param("search") String search,
    //         Pageable pageable);

    Page<SanPhamCoSan> findBySanPhamTrangThaiAndTimKiemContaining(
        TrangThaiSanPham trangThai,
        String search,
        Pageable pageable
    );

}