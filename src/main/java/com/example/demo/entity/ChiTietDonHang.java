package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tblChiTietDonHang")
@Data
public class ChiTietDonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CTHD_Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "CTHD_DH_Id")
    private DonHang donHang;

    @Column(name = "CTHD_MaChiTiet")
    private String maChiTiet;

    // Product snapshot
    @Column(name = "CTHD_SP_Id")
    private Long sanPhamId;

    @Column(name = "CTHD_LSP_Id")
    private Integer loaiSanPham;

    @Column(name = "CTHD_TenSanPhamSnapshot")
    private String tenSanPham;

    @Column(name = "CTHD_SoLuong")
    private Integer soLuong;

    // Price snapshot
    @Column(name = "CTHD_DonGiaSnapshot")
    private BigDecimal donGia;

    @Column(name = "CTHD_ChiPhiVatLieuSnapshot")
    private BigDecimal chiPhiVatLieu;

    @Column(name = "CTHD_ChiPhiNhanCongSnapshot")
    private BigDecimal chiPhiNhanCong;

    @Column(name = "CTHD_ChiPhiThuongHieuSnapshot")
    private BigDecimal chiPhiThuongHieu;

    @Column(name = "CTHD_ThanhTien")
    private BigDecimal thanhTien;

    // Tax snapshot
    @Column(name = "CTHD_ThueSuatSnapshot")
    private BigDecimal thueSuat;

    @Column(name = "CTHD_LoaiThueSnapshot")
    private String loaiThue;

    @Column(name = "CTHD_TienThue")
    private BigDecimal tienThue;

    // Audit
    @Column(name = "CTHD_NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "CTHD_NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    // METHOD
    public BigDecimal tinhThanhTien() {
        if (donGia == null || soLuong == null) return BigDecimal.ZERO;
        this.thanhTien = donGia.multiply(BigDecimal.valueOf(soLuong));
        return thanhTien;
    }
}