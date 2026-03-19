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

// @Query(value = """
// SELECT 
//     spcs.id as spcsId,
//     nd.ten_dang_nhap as tenSeller,
//     av.link as anhSanPham,
//     sp.ngay_tao as ngayTao,

//     log.ngay_tao as ngayXuLy,
//     log.id_tac_nhan as adminId,
//     log.sieu_du_lieu as sieuDuLieu

// FROM tbl_san_pham_co_san spcs

// JOIN tbl_san_pham sp
//     ON spcs.san_pham_id = sp.id

// JOIN tbl_thong_tin_nguoi_ban tt
//     ON sp.ttnb_id = tt.id

// JOIN tbl_nguoi_dung nd
//     ON tt.nd_id = nd.id

// LEFT JOIN tbl_anh_video_san_pham av
//     ON av.san_pham_id = sp.id
//     AND av.thu_tu = 1

// LEFT JOIN tbl_nhat_ky_kiem_toan log
//     ON log.id_muc_tieu = sp.id

// WHERE sp.trang_thai = :trangThai
// AND (
//     :search IS NULL OR :search = '' OR

//     CONCAT(
//         LOWER(
//             REPLACE(
//                 REPLACE(nd.ten_dang_nhap, ' ', ''),
//                 'đ', 'd'
//             )
//         ),
//         DATE_FORMAT(sp.ngay_tao, '%d%m%Y')
//     )
//     LIKE CONCAT('%', :search, '%')
// )

// ORDER BY sp.ngay_tao DESC
// """, nativeQuery = true)
// Page<SanPhamModerationProjection> getModerationProductsFull(
//     @Param("trangThai") String trangThai,
//     @Param("search") String search,
//     Pageable pageable
// );

}