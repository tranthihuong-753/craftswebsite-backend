package com.example.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SanPhamModerationDTO {

    private Long idSanPhamCoSan;
    private String anhSanPham;
    private String tenSeller;
    private LocalDateTime ngayTao;

    public SanPhamModerationDTO(Long idSanPhamCoSan,
                                String anhSanPham,
                                String tenSeller,
                                LocalDateTime ngayTao) {
        this.idSanPhamCoSan = idSanPhamCoSan;
        this.anhSanPham = anhSanPham;
        this.tenSeller = tenSeller;
        this.ngayTao = ngayTao;
    }
}