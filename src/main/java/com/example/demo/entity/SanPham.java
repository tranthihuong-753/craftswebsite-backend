package com.example.demo.entity;

import com.example.demo.enums.TrangThaiSanPham;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tblSanPham")
@Data
public class SanPham {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SP_Id")
    private Long id;

    // Thông tin người bán
    @ManyToOne
    @JoinColumn(name = "SP_TTNB_Id", referencedColumnName = "TTNB_Id")
    private ThongTinNguoiBan thongTinNguoiBan;

    // Loại sản phẩm
    @ManyToOne
    @JoinColumn(name = "SP_LSP_Id", referencedColumnName = "LSP_Id")
    private LoaiSanPham loaiSanPham;

    // Danh mục
    @ManyToOne
    @JoinColumn(name = "SP_DM_Id", referencedColumnName = "DM_Id")
    private DanhMuc danhMuc;

    // Trạng thái
    @Enumerated(EnumType.STRING)
    @Column(name = "SP_TrangThai")
    private TrangThaiSanPham trangThai;

    // số giờ làm việc ước tính (cho sản phẩm đặt làm)
    @Column(name = "SP_SoGioLamViecUocTinh")
    private Long soGioLamViecUocTinh;

    // chứng chỉ
    @ManyToOne
    @JoinColumn(name = "SP_CC_Id", referencedColumnName = "CC_Id")
    private ChungChi chungChi;

    @Column(name = "SP_NgayTao")
    private LocalDateTime ngayTao;

    @Column(name = "SP_NgayCapNhat")
    private LocalDateTime ngayCapNhat;

    @OneToMany(mappedBy = "sanPham")
    private List<AnhVideoSanPham> anhVideos;

    @PrePersist
    public void prePersist() {
        this.ngayTao = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.ngayCapNhat = LocalDateTime.now();
    }
    

}