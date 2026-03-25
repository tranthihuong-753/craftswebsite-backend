package com.example.demo.entity;

import java.time.LocalDateTime;

import com.example.demo.enums.TrangThaiTaiKhoanNganHang;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tblTaiKhoanNganHang")
@Data
public class TaiKhoanNganHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TKNHNB_Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TKNH_TTNB_Id", referencedColumnName = "TTNB_Id")
    private ThongTinNguoiBan ttnbId;

    @Column(name = "TKNH_MaNganHang")
    private String maNganHang;

    @Column(name = "TKNH_TenNganHang")
    private String tenNganHang;

    @Column(name = "TKNH_SoTaiKhoan")
    private String soTaiKhoan;

    @Column(name = "TKNH_TenTaiKhoan")
    private String tenTaiKhoan;

    @Enumerated(EnumType.STRING)
    @Column(name = "TKNH_TrangThai")
    private TrangThaiTaiKhoanNganHang trangThai;

    @Column(name = "TKNH_NgayTao")
    private LocalDateTime ngayTao;

    @PrePersist
    public void prePersist() {
        this.ngayTao = LocalDateTime.now();
    }

}