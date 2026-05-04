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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DC_VTND_ID", referencedColumnName = "VTND_Id")
    private VaiTroNguoiDung vaiTroNguoiDung; 

    @Column(name = "DC_TinhThanh") 
    private String tinhThanh; // Ví dụ: Thành phố Hà Nội, Tỉnh Nam Định

    @Column(name = "DC_QuanHuyen")
    private String quanHuyen; // Quận, Huyện, Thị xã, Thành phố thuộc tỉnh

    @Column(name = "DC_PhuongXa")
    private String phuongXa; // Phường, Xã, Thị trấn

    @Column(name = "DC_CuThe")
    private String cuThe; // Số nhà, tên đường, ngõ ngách

    // Thêm trường này để dễ dàng hiển thị hoặc in hóa đơn
    @Column(name = "DC_DiaChiDayDu")
    private String diaChiDayDu; 

    @Column(name = "DC_ThietLapMacDinh")
    private Integer thietLapMacDinh; // Dùng Integer/Boolean cho 0-1 sẽ chuẩn hơn Long , 1 là mặc định 

    @Column(name = "DC_Status")
    private Integer status; // 1: Active, 0: Deleted
}

