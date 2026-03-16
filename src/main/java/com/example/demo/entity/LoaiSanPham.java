package com.example.demo.entity;

import com.example.demo.enums.TrangThaiLoaiSanPham;
import com.example.demo.enums.LoaiSP;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tblLoaiSanPham")
@Data
public class LoaiSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LSP_Id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "LSP_Loai")
    private LoaiSP loai;

    @Column(name = "LSP_MoTa")
    private String moTa;

    @Enumerated(EnumType.STRING)
    @Column(name = "LSP_TrangThai")
    private TrangThaiLoaiSanPham trangThai;

}