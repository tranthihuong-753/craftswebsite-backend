package com.example.demo.repository;

import com.example.demo.dto.SanPhamModerationDTO;
import com.example.demo.dto.SanPhamModerationProjection;
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

    Page<SanPhamCoSan> findBySanPhamTrangThaiAndTimKiemContaining(
        TrangThaiSanPham trangThai,
        String search,
        Pageable pageable
    );

    @Query("""
    SELECT 
        spcs.id as spcsId,
        nd.tenDangNhap as tenSeller,
        av.link as anhSanPham,
        sp.ngayTao as ngayTao,

        log.ngayTao as ngayXuLy,
        log.idTacNhan as adminId,
        log.sieuDuLieu as sieuDuLieu

    FROM SanPhamCoSan spcs

    JOIN spcs.sanPham sp
    JOIN sp.thongTinNguoiBan tt
    JOIN tt.nguoiDung nd

    LEFT JOIN AnhVideoSanPham av 
        ON av.sanPham.id = sp.id
        AND av.thuTu = 1

    LEFT JOIN NhatKyKiemToan log
        ON log.idMucTieu = sp.id
        AND log.loaiMucTieu = com.example.demo.enums.NKKT_LoaiMucTieu.SAN_PHAM
        AND (
            (:trangThai = com.example.demo.enums.TrangThaiSanPham.DANG_BAN 
                AND log.hanhDong = com.example.demo.enums.NKKT_HanhDong.TAO_SAN_PHAM)
            OR
            (:trangThai = com.example.demo.enums.TrangThaiSanPham.VI_PHAM 
                AND log.hanhDong = com.example.demo.enums.NKKT_HanhDong.XOA_SAN_PHAM)
        )

    WHERE sp.trangThai = :trangThai
    AND (
        :search IS NULL OR :search = '' OR
        LOWER(nd.tenDangNhap) LIKE LOWER(CONCAT('%', :search, '%'))
    )
    """)
    Page<SanPhamModerationProjection> getModerationProductsFull(
        @Param("trangThai") TrangThaiSanPham trangThai,
        @Param("search") String search,
        Pageable pageable
    );

    // TIM KIEM SAN PHAM BEN USER 
    @Query("""
        SELECT spcs FROM SanPhamCoSan spcs
        JOIN spcs.sanPham sp
        JOIN sp.thongTinNguoiBan ttnb
        JOIN ttnb.nguoiDung nd
        WHERE sp.trangThai = 'DANG_BAN'
        AND (
            LOWER(spcs.timKiem) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(spcs.moTa) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(nd.ten) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<SanPhamCoSan> search(@Param("keyword") String keyword, Pageable pageable);

    // TIM KIEM SAN PHAM BEN USER 
    @Query("""
        SELECT spcs FROM SanPhamCoSan spcs
        JOIN spcs.sanPham sp
        WHERE sp.trangThai = 'DANG_BAN'
    """)
    Page<SanPhamCoSan> findAllByTrangThaiDangBan(Pageable pageable);
}