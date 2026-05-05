package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Entity
@Data
@Table(name = "tbl_van_chuyen")
public class VanChuyen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "VC_Id")
    private Long vcId;

    @Column(name = "VC_DH_Id")
    private Long vcDhId;

    @Column(name = "VC_Loai")
    private String vcLoai; 
    // TU_VAN_CHUYEN | BUU_DIEN | BEN_THU_BA

    @Column(name = "VC_NhaCungCap")
    private String vcNhaCungCap;
    // VNPOST | VIETTEL_POST | GHTK | GHN | NOI_BO

    @Column(name = "VC_VTND_Id_Shipper")
    private Long vcVtndIdShipper;

    @Column(name = "VC_MaVanDon")
    private String vcMaVanDon;

    @Column(name = "VC_DuongDanTheoDoi")
    private String vcDuongDanTheoDoi;

    @Column(name = "VC_TrangThai")
    private String vcTrangThai;
    // TAO | DA_LAY | DA_GIAO | THAT_BAI

    @Column(name = "VC_GhiChu", columnDefinition = "TEXT")
    private String vcGhiChu;

    @Column(name = "VC_NgayTao")
    private LocalDateTime vcNgayTao;

    @Column(name = "VC_NgayCapNhat")
    private LocalDateTime vcNgayCapNhat;

}