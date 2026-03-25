package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;

@Entity
@Table(name = "tblGioHang")
@Data
public class GioHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "GH_Id")
    private Long id;

    // VaiTroNguoiDung (user role)
    @ManyToOne
    @JoinColumn(name = "GH_VTND_Id")
    private VaiTroNguoiDung vaiTroNguoiDung;

    // SanPham
    @ManyToOne
    @JoinColumn(name = "GH_SP_Id")
    private SanPham sanPham;

    @Column(name = "GH_SoLuong")
    private Integer soLuong;

    @Column(name = "GH_DonGiaSnapshot")
    private Double donGiaSnapshot;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "GH_QuyTacThueSnapshot")
    private Object quyTacThueSnapshot;

    @Column(name = "GH_DuocChon")
    private Boolean duocChon;

    @Column(name = "GH_NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "GH_NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @PrePersist
    public void prePersist() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
}