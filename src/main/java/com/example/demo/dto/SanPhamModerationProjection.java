package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public interface SanPhamModerationProjection {

    Long getSpcsId();
    String getTenSeller();
    String getAnhSanPham();
    LocalDateTime getNgayTao();

    LocalDateTime getNgayXuLy();
    UUID getAdminId();
    String getSieuDuLieu();
}