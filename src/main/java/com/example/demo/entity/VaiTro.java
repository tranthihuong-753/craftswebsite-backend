package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tblVaiTro")
@Data
public class VaiTro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VT_Id")
    private Long id;

    @Column(name = "VT_Loai", unique = true, nullable = false)
    private String loai;
}
