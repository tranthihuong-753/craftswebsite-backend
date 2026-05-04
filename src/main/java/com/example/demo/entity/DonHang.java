package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tblDonHang")
@Data
public class DonHang {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DH_Id")
    private Long id;
 
    @Column(name = "DH_MaDon")
    private String maDon;

    // Người mua
    @Column(name = "DH_VTND_Id_NguoiMua")
    private UUID nguoiMuaId;

    // Người bán
    @Column(name = "DH_VTND_Id_NguoiBan")
    private UUID nguoiBanId;

    // Address snapshot
    @Column(name = "DH_DC_NguoiBan")
    private String diaChiNguoiBanId;

    @Column(name = "DH_DC_NguoiMua")
    private String diaChiNguoiMuaId;

    // Money
    @Column(name = "DH_TongTienHang")
    private BigDecimal tongTienHang;

    @Column(name = "DH_ChietKhau")
    private BigDecimal chietKhau;

    @Column(name = "DH_PhiSanSnapshot")
    private BigDecimal phiSan;

    @Column(name = "DH_TyLePhiSanSnapshot")
    private BigDecimal tyLePhiSan;

    @Column(name = "DH_TienThue")
    private BigDecimal tienThue;

    @Column(name = "DH_TienShip")
    private BigDecimal tienShip;

    @Column(name = "DH_TienPhaiThanhToan")
    private BigDecimal tienPhaiThanhToan;

    // Status
    @Column(name = "DH_TrangThai")
    private String trangThai;

    @Column(name = "DH_TrangThaiThanhToan")
    private String trangThaiThanhToan; // CHO_THANH_TOAN hoac DA_THANH_TOAN 

    // Time
    @Column(name = "DH_NgayDat")
    private LocalDateTime ngayDat;

    @Column(name = "DH_NgayHoanThanh")
    private LocalDateTime ngayHoanThanh;

    @Column(name = "DH_GhiChu")
    private String ghiChu;

    
    // Cancel
    @Column(name = "DH_LyDoHuy")
    private String lyDoHuy;

    @Column(name = "DH_VTND_Id_NguoiHuy")
    private Long nguoiHuyId;

    @Column(name = "DH_TrangThaiHuy")
    private Boolean trangThaiHuy;

    @Column(name = "DH_LoiNhanChoShop")
    private String loiNhanChoShop;

    // Audit
    @Column(name = "DH_NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "DH_NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    // Chi tiết đơn hàng
    @OneToMany(mappedBy = "donHang", cascade = CascadeType.ALL)
    private List<ChiTietDonHang> chiTietDonHangs;

    // METHOD
    public BigDecimal calculateTotal() {
        if (tongTienHang == null) tongTienHang = BigDecimal.ZERO;
        if (phiSan == null) phiSan = BigDecimal.ZERO;
        if (tienThue == null) tienThue = BigDecimal.ZERO;

        return tongTienHang.add(phiSan).add(tienThue);
    }

    public void confirm() {
        // snapshot lock logic (placeholder)
        this.ngayCapNhat = LocalDateTime.now();
    }
}