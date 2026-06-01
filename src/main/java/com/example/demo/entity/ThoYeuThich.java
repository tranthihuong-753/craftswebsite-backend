package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tblThoYeuThich")
@Data
public class ThoYeuThich {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TYT_Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TYT_VTND_Id")
    private VaiTroNguoiDung vaiTroNguoiDung;

    @ManyToOne
    @JoinColumn(name = "TYT_TTNB_Id")
    private ThongTinNguoiBan thongTinNguoiBan;
}