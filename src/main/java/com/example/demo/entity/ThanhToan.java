package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tblThanhToan")
@Data
public class ThanhToan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TT_Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TT_DH_Id")
    private DonHang donHang;

    @Column(name = "TT_SoTien")
    private BigDecimal soTien;

    @Column(name = "TT_MaNganHang")
    private String maNganHang;

    @Column(name = "TT_SoTaiKhoan")
    private String soTaiKhoan;

    @Column(name = "TT_TenTaiKhoan")
    private String tenTaiKhoan;

    @Column(name = "TT_ChuoiVietQR", columnDefinition = "TEXT")
    private String chuoiVietQR;
 
    @Column(name = "TT_TrangThai")
    private String trangThai; // CHO_THANH_TOAN, DA_THANH_TOAN, DANG_XU_LY, DA_HOAN 

    @Column(name = "TT_NguoiXacNhan")
    private Long nguoiXacNhan; 

    @Column(name = "TT_AV_Id_AnhMinhChung")
    private Long anhMinhChungId;

    @Column(name = "TT_GhiChu", columnDefinition = "TEXT")
    private String ghiChu;

    @Column(name = "TT_NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "TT_NgayXacNhan")
    private LocalDateTime ngayXacNhan;
}