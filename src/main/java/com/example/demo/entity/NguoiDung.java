package com.example.demo.entity;

import com.example.demo.enums.ND_Trangthaixacthuc;
import com.example.demo.model.ND_CCCD;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tblNguoiDung")
@Data
public class NguoiDung {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ND_Id")
    private UUID id;

    // @ManyToOne
    // @JoinColumn(name = "ND_VTND_Id", referencedColumnName = "VTND_Id")
    // private VaiTroNguoiDung vaiTroNguoiDung;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ND_CCCD")
    private ND_CCCD cccd;

    @Column(name = "ND_SDT")
    private String sdt;

    @Column(name = "ND_Ten")
    private String ten;

    @Enumerated(EnumType.STRING)
    @Column(name = "ND_TrangThaiXacThuc")
    private ND_Trangthaixacthuc trangThaiXacThuc;

    // @Column(name = "ND_AV_Id_AnhChanDung")
    // private Integer AV_Id_anhChanDung;

    @ManyToOne
    @JoinColumn(name = "ND_AV_Id_AnhChanDung", referencedColumnName = "AV_Id")
    private AnhVideo anhVideo_anhChanDung;

    // @Column(name = "ND_DC_Id")
    // private Integer DC_Id;

    @ManyToOne
    @JoinColumn(name = "ND_DC_Id", referencedColumnName = "DC_Id")
    private DiaChi diaChi;

    @Column(name = "ND_TenDangNhap")
    private String tenDangNhap;

    @Column(name = "ND_MatKhau")
    private String matKhau;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "ND_VectorCCCD")
    private List<Float> vectorCCCD;

    // @Column(name = "ND_AV_ID_AnhCCCD")
    // private Integer AV_ID_anhCCCD;

    @ManyToOne
    @JoinColumn(name = "ND_AV_ID_AnhCCCD", referencedColumnName = "AV_Id")
    private AnhVideo anhVideo_anhCCCD;
}