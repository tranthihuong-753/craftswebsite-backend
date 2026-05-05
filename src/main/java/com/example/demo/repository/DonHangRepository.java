package com.example.demo.repository;

import com.example.demo.entity.DonHang;

import jakarta.persistence.LockModeType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface DonHangRepository extends JpaRepository<DonHang, Long> {
    List<DonHang> findAllByIdIn(List<Long> ids);

    @Query("""
        SELECT d FROM DonHang d
        WHERE d.nguoiBanId = :sellerId
    """)
    Page<DonHang> findBySeller(@Param("sellerId") UUID sellerId, Pageable pageable);

    @Query("""
        SELECT d FROM DonHang d
        WHERE d.nguoiBanId = :sellerId
        AND (:tabStatus = 'ALL' OR d.trangThai = :tabStatus)
        AND (:dhTrangThai IS NULL OR d.trangThai = :dhTrangThai)
        AND (:dhTrangThaiThanhToan IS NULL OR d.trangThaiThanhToan = :dhTrangThaiThanhToan)
        AND (
            :keyword IS NULL OR
            LOWER(d.maDon) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(d.diaChiNguoiMuaId) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<DonHang> findBySellerWithFullFilter(
            @Param("sellerId") UUID sellerId,
            @Param("tabStatus") String tabStatus,
            @Param("dhTrangThai") String dhTrangThai,
            @Param("dhTrangThaiThanhToan") String dhTrangThaiThanhToan,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT o FROM DonHang o WHERE o.id = :id")
    Optional<DonHang> findByIdForUpdate(@Param("id") Long id);
    
}
