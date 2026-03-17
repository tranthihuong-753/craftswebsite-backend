package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SanPhamModerationDTO {

    private Long idSanPhamCoSan;
    private String anhSanPham;
    private String tenSeller;
    private LocalDateTime ngayTao;

    private LocalDateTime ngayXuLy;
    private UUID adminId;
    private String lyDo;

    // constructor ngắn (cho tab chưa xử lý)
    public SanPhamModerationDTO(Long idSanPhamCoSan,
                                String anhSanPham,
                                String tenSeller,
                                LocalDateTime ngayTao) {
        this.idSanPhamCoSan = idSanPhamCoSan;
        this.anhSanPham = anhSanPham;
        this.tenSeller = tenSeller;
        this.ngayTao = ngayTao;
    }

    // constructor full (cho tab đã xử lý)
    public SanPhamModerationDTO(Long idSanPhamCoSan,
                                String anhSanPham,
                                String tenSeller,
                                LocalDateTime ngayTao,
                                LocalDateTime ngayXuLy,
                                UUID adminId,
                                String lyDo) {
        this.idSanPhamCoSan = idSanPhamCoSan;
        this.anhSanPham = anhSanPham;
        this.tenSeller = tenSeller;
        this.ngayTao = ngayTao;
        this.ngayXuLy = ngayXuLy;
        this.adminId = adminId;
        this.lyDo = lyDo;
    }
}