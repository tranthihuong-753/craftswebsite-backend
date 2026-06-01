package com.example.demo.entity;

import com.example.demo.enums.LoaiChungChi;
import com.example.demo.enums.LoaiMucTieuChungChi;
import com.example.demo.enums.TrangThaiChungChi;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tblChungChi")
@Data
public class ChungChi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CC_Id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "CC_Loai")
    private LoaiChungChi loai;

    @Enumerated(EnumType.STRING)
    @Column(name = "CC_LoaiMucTieu")
    private LoaiMucTieuChungChi loaiMucTieu;

    // ID của đối tượng mục tiêu (Sản phẩm hoặc Người bán)
    @Column(name = "CC_IdMucTieu")
    private Long idMucTieu;

    @Column(name = "CC_DiemTrungBinh")
    private Float diemTrungBinh;

    @Column(name = "CC_TongDanhGia")
    private Long tongDanhGia;

    @Enumerated(EnumType.STRING)
    @Column(name = "CC_TrangThai")
    private TrangThaiChungChi trangThai;

    @Column(name = "CC_NgayTao")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        this.ngayTao = LocalDateTime.now();
    }

}