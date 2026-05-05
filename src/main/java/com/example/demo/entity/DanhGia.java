package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_danh_gia")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DanhGia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dg_id")
    private Long id;

    @Column(name = "dg_dh_id")
    private Long donHangId;

    @Column(name = "dg_sp_id")
    private Long sanPhamId;

    @Column(name = "dg_vtnd_id_nguoi_mua")
    private Long nguoiMuaId;

    @Column(name = "dg_loai")
    private String loai; // SAN_PHAM_CO_SAN | SAN_PHAM_DAT_LAM

    @Column(name = "dg_diem")
    private Integer diem;

    @Column(name = "dg_mo_ta", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "dg_ngay_tao")
    private LocalDateTime ngayTao;
}