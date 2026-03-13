package com.example.demo.entity;

import com.example.demo.enums.TrangThaiVaiTro;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tblVaiTroNguoiDung")
@Data
public class VaiTroNguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "VTND_Id")
    private UUID id;

    // @Column(name = "VTND_ND_Id")
    // private UUID ND_Id;

    @ManyToOne
    @JoinColumn(name = "VTND_ND_Id", referencedColumnName = "ND_Id") // ND_Id tu ND_Id trong bang NguoiDung
    private NguoiDung nguoiDung; // ND_Id tu VTND_ND_Id

    // @Column(name = "VTND_VT_Id")
    // private Long VT_Id;

    @ManyToOne
    @JoinColumn(name = "VTND_VT_Id", referencedColumnName = "VT_Id")
    private VaiTro vaiTro;

    @Enumerated(EnumType.STRING)
    @Column(name = "VTND_TrangThai")
    private TrangThaiVaiTro trangThai;

    @Column(name = "VTND_NgayDuyet")
    private LocalDateTime ngayDuyet;

    // @Column(name = "VTND_VTND_Id_NguoiDuyet")
    // private UUID VTND_Id_NguoiDuyet;

    @ManyToOne
    @JoinColumn(name = "VTND_VTND_Id_NguoiDuyet", referencedColumnName = "VTND_Id")
    private VaiTroNguoiDung VTND_nguoiDuyet;

    @ManyToOne
    @JoinColumn(name = "VTND_AV_Id_HinhNen", referencedColumnName = "AV_Id")
    private AnhVideo AV_hinhNen;

    // @OneToMany(mappedBy = "vaiTroNguoiDung")
    // private List<DiaChi> diaChis;
}