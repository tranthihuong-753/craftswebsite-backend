package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.example.demo.enums.PhuongThucHttp;

@Entity
@Table(name = "tblApi")
@Data
public class Api {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "API_Id")
    private Long id;

    @Column(name = "API_Ten")
    private String ten;

    @Enumerated(EnumType.STRING)
    @Column(name = "API_PhuongThuc")
    private PhuongThucHttp phuongThuc; // GET,POST,PUT,DELETE,PATCH

    @Column(name = "API_DuongDan")
    private String duongDan;

    @Column(name = "API_HoatDong")
    private Boolean hoatDong = true;

    @Column(name = "API_NgayTao")
    private LocalDateTime ngayTao;

    public void activate() {
        this.hoatDong = true;
    }

    public void deactivate() {
        this.hoatDong = false;
    }

    @PrePersist
    public void prePersist() {
        this.ngayTao = LocalDateTime.now();
    }
}