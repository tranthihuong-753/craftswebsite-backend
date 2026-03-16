package com.example.demo.entity;

import com.example.demo.enums.TrangThaiDanhMuc;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "tblDanhMuc")
@Data
public class DanhMuc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DM_Id")
    private Long id;

    @Column(name = "DM_Ten")
    private String ten;

    @Column(name = "DM_MoTa")
    private String moTa;

    @Enumerated(EnumType.STRING)
    @Column(name = "DM_TrangThai")
    private TrangThaiDanhMuc trangThai;

    // % thuế
    @Column(name = "DM_ThueSuatHienTai")
    private BigDecimal thueSuatHienTai;

}