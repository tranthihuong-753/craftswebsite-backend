package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tblAnhVideoSanPham")
@Data
public class AnhVideoSanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AVSP_Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "AVSP_SP_Id", referencedColumnName = "SP_Id")
    @JsonIgnore
    private SanPham sanPham;

    @Column(name = "AVSP_Link")
    private String link;

    @Column(name = "AVSP_ThuTu")
    private Long thuTu;

}