package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

import com.example.demo.enums.TTNB_TrangThai;

@Entity
@Table(name = "tblThongTinNguoiBan")
@Data
public class ThongTinNguoiBan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "TTNB_Id")
    private UUID id;

    // Người bán (liên kết với bảng NguoiDung)
    @ManyToOne
    @JoinColumn(name = "TTNB_ND_Id_VaiTroNguoiMua", referencedColumnName = "ND_Id")
    private NguoiDung nguoiDung;

    @Column(name = "TTNB_TienNhanCong")
    private BigDecimal tienNhanCong;

    @Column(name = "TTNB_TienThuongHieu")
    private BigDecimal tienThuongHieu;

    @Enumerated(EnumType.STRING)
    @Column(name = "TTNB_TrangThai")
    private TTNB_TrangThai trangThai;

    // ma so thue 
    @Column(name = "TTNB_MaSoThue")
    private String maSoThue;

    // Banner
    @ManyToOne
    @JoinColumn(name = "TTNB_AV_Id_Banner", referencedColumnName = "AV_Id")
    private AnhVideo AV_banner;

    // Hình nền
    @ManyToOne
    @JoinColumn(name = "TTNB_AV_Id_HinhNen", referencedColumnName = "AV_Id")
    private AnhVideo AV_hinhNen;
}