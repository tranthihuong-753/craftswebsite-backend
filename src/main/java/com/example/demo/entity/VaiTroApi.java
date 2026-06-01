package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tblVaiTroApi")
@Data
public class VaiTroApi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VTAPI_Id")
    private Long id;

    // Vai trò
    @ManyToOne
    @JoinColumn(name = "VTAPI_VT_Id", referencedColumnName = "VT_Id")
    private VaiTro vaiTro;

    // API
    @ManyToOne
    @JoinColumn(name = "VTAPI_API_Id", referencedColumnName = "API_Id")
    private Api api;

    @Column(name = "VTAPI_ChoPhep")
    private Boolean choPhep;

    @Column(name = "VTAPI_NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "VTAPI_NgayCapNhat")
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