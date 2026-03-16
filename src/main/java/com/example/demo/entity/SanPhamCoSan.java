package com.example.demo.entity;

import com.example.demo.enums.TrangThaiSanPhamCoSan;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.demo.utils.TextUtils;

@Entity
@Table(name = "tblSanPhamCoSan")
@Data
public class SanPhamCoSan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPCS_Id")
    private Long id;

    // Liên kết sản phẩm
    @ManyToOne
    @JoinColumn(name = "SPCS_SP_Id", referencedColumnName = "SP_Id")
    private SanPham sanPham;

    @Column(name = "SPCS_MoTa", columnDefinition = "text")
    private String moTa;

    @Column(name = "SPCS_Gia")
    private BigDecimal gia;

    @Column(name = "SPCS_CanNang")
    private Double canNang;

    @Column(name = "SPCS_KichThuoc_ChieuDai")
    private Double chieuDai;

    @Column(name = "SPCS_KichThuoc_ChieuRong")
    private Double chieuRong;

    @Column(name = "SPCS_KichThuoc_ChieuCao")
    private Double chieuCao;

    @Column(name = "SPCS_GiaGoc")
    private BigDecimal giaGoc;

    @Column(name = "SPCS_SoLuongBanDau")
    private Long soLuongBanDau;

    @Column(name = "SPCS_SoLuongHienTai")
    private Long soLuongHienTai;

    @Column(name = "SPCS_ThoiGianKetThuc")
    private LocalDateTime thoiGianKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(name = "SPCS_TrangThai")
    private TrangThaiSanPhamCoSan trangThai;

    @Column(name = "SPCS_SearchText")
    private String searchText;

    @PrePersist
    @PreUpdate
    public void buildSearchText() {

        String text =
                (moTa == null ? "" : moTa) +
                (gia == null ? "" : gia.toString()) +
                (soLuongBanDau == null ? "" : soLuongBanDau.toString());

        this.searchText = TextUtils.normalize(text);
    }

}
