package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;
import com.example.demo.enums.*;

@Entity
@Table(name = "tblNhatKyKiemToan")
@Data
public class NhatKyKiemToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NKKT_Id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "NKKT_LoaiTacNhan")
    private NKKT_LoaiTacNhan loaiTacNhan;

    @Enumerated(EnumType.STRING)
    @Column(name = "NKKT_HanhDong")
    private NKKT_HanhDong hanhDong;

    @Enumerated(EnumType.STRING)
    @Column(name = "NKKT_LoaiMucTieu")
    private NKKT_LoaiMucTieu loaiMucTieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "NKKT_KetQua")
    private NKKT_KetQua ketQua;

    @Column(name = "NKKT_IdTacNhan")
    private UUID idTacNhan;

    @Column(name = "NKKT_IdMucTieu")
    private Long idMucTieu;

    @Column(name = "NKKT_DuLieuCu", columnDefinition = "json")
    private String duLieuCu;

    @Column(name = "NKKT_DuLieuMoi", columnDefinition = "json")
    private String duLieuMoi;

    // khoang_cach, ly_do, diem...
    @Column(name = "NKKT_SieuDuLieu", columnDefinition = "json")
    private String sieuDuLieu;

    @Column(name = "NKKT_IP")
    private String ip;

    @Column(name = "NKKT_DauVanTayThietBi")
    private String dauVanTayThietBi;

    @Column(name = "NKKT_TrinhDuyet")
    private String trinhDuyet;

    @Column(name = "NKKT_ViTri")
    private String viTri;

    @Column(name = "NKKT_NgayTao")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        this.ngayTao = LocalDateTime.now();
    }
}