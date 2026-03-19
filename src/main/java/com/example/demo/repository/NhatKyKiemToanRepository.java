package com.example.demo.repository;

import com.example.demo.entity.NhatKyKiemToan;
import com.example.demo.enums.NKKT_HanhDong;
import com.example.demo.enums.NKKT_LoaiMucTieu;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NhatKyKiemToanRepository extends JpaRepository<NhatKyKiemToan, Long> {

    Optional<NhatKyKiemToan> findTopByIdMucTieuAndLoaiMucTieuOrderByNgayTaoDesc(
        Long idMucTieu,
        NKKT_LoaiMucTieu loaiMucTieu
    );

    Optional<NhatKyKiemToan> findTopByIdMucTieuAndLoaiMucTieuAndHanhDongInOrderByNgayTaoDesc(
        Long idMucTieu,
        NKKT_LoaiMucTieu loaiMucTieu,
        List<NKKT_HanhDong> hanhDongs
    );

    Optional<NhatKyKiemToan> findTopByIdMucTieuAndLoaiMucTieuAndHanhDongOrderByNgayTaoDesc(
        Long idMucTieu,
        NKKT_LoaiMucTieu loaiMucTieu,
        NKKT_HanhDong hanhDong
    );

}