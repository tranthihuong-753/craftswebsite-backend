package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tblSanPhamDatLam")
@Data
public class SanPhamDatLam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPDL_Id")
    private Long id;

    // Sản phẩm
    @ManyToOne
    @JoinColumn(name = "SPDL_SP_Id", referencedColumnName = "SP_Id")
    private SanPham sanPham;

    // Người mua
    @ManyToOne
    @JoinColumn(name = "SPDL_VTND_Id_NguoiMua", referencedColumnName = "VTND_Id")
    private VaiTroNguoiDung nguoiMua;

    @Column(name = "SPDL_MoTa", columnDefinition = "text")
    private String moTa;

}