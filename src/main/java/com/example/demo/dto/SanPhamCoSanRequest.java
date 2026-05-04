package com.example.demo.dto;

import com.example.demo.enums.TrangThaiChungChi;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class SanPhamCoSanRequest {

    private String ten;

    private String moTa;

    private BigDecimal gia;

    private Double canNang;

    private Double chieuDai;

    private Double chieuRong;

    private Double chieuCao;

    private BigDecimal giaGoc;

    private Integer soLuongBanDau;

    private Integer soLuongTamGiu;

    private Integer soLuongHienTai;

    private TrangThaiSanPhamCoSan trangThaiSPCS;

    private UUID sellerId;

    private Long danhMucId;

    private TrangThaiSanPham trangThaiSanPham;

    private Long soGioLamViecUocTinh;

    private TrangThaiChungChi trangThaiChungChi;

    private String coverUrls;

    private List<String> imageUrls;

    private List<String> videoUrls;
}