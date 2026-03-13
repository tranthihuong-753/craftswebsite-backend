package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tblDiaChi")
@Data
public class DiaChi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DC_ID")
    private Long id;

    // @Column(name = "DC_VTND_ID")
    // private UUID userId;
    @ManyToOne
    @JoinColumn(name = "DC_VTND_ID", referencedColumnName = "VTND_Id")
    private VaiTroNguoiDung vaiTroNguoiDung;

    @Column(name = "DC_ThanhPho")
    private String thanhPho;

    @Column(name = "DC_Huyen")
    private String huyen;

    @Column(name = "DC_Xa")
    private String xa;

    @Column(name = "DC_CuThe")
    private String cuThe;

    @Column(name = "DC_ThietLapMacDinh")
    private Integer thietLapMacDinh; // 1 la mac dinh, 0 la khong mac dinh

    @Column(name = "DC_Status")
    private Integer status; //1 = con su dung , 0 = khong con su dung
}