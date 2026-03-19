package com.example.demo.service;

import com.example.demo.entity.NhatKyKiemToan;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.enums.NKKT_HanhDong;
import com.example.demo.enums.NKKT_KetQua;
import com.example.demo.enums.NKKT_LoaiMucTieu;
import com.example.demo.enums.NKKT_LoaiTacNhan;
import com.example.demo.repository.NhatKyKiemToanRepository;
import com.example.demo.repository.SanPhamCoSanRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NhatKyKiemToanService {

    @Autowired
    private NhatKyKiemToanRepository repository;

    private final SanPhamCoSanService sanPhamCoSanService;

    public NhatKyKiemToan save(NhatKyKiemToan log) {

        log.setNgayTao(LocalDateTime.now());

        return repository.save(log);
    }

    public List<NhatKyKiemToan> getAll() {
        return repository.findAll();
    }

    public NhatKyKiemToan getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void duyetSanPham(Long sanPhamId, String lyDo, UUID adminId) {

        try {

            NhatKyKiemToan log = new NhatKyKiemToan();

            log.setLoaiTacNhan(NKKT_LoaiTacNhan.ADMIN);
            log.setHanhDong(NKKT_HanhDong.TAO_SAN_PHAM);
            log.setLoaiMucTieu(NKKT_LoaiMucTieu.SAN_PHAM);
            log.setKetQua(NKKT_KetQua.THANH_CONG);

            log.setIdTacNhan(adminId);
            log.setIdMucTieu(sanPhamId);

            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(
                    Map.of("ly_do", lyDo)
            );

            log.setSieuDuLieu(json);

            log.setNgayTao(LocalDateTime.now());

            NhatKyKiemToan logsaved = save(log);

            // chuyen trang thai san pham 
            sanPhamCoSanService.updateSanPhamCoSanTrangThai(sanPhamId, "DANG_BAN");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
        
    public void viPhamSanPham(Long sanPhamId, String lyDo, UUID adminId) {

        try {

            NhatKyKiemToan log = new NhatKyKiemToan();

            log.setLoaiTacNhan(NKKT_LoaiTacNhan.ADMIN);

            log.setHanhDong(NKKT_HanhDong.XOA_SAN_PHAM);

            log.setLoaiMucTieu(NKKT_LoaiMucTieu.SAN_PHAM);

            log.setKetQua(NKKT_KetQua.THANH_CONG);

            log.setIdTacNhan(adminId);

            log.setIdMucTieu(sanPhamId);

            ObjectMapper mapper = new ObjectMapper();

            String json = mapper.writeValueAsString(
                    Map.of("ly_do", lyDo)
            );

            log.setSieuDuLieu(json);

            log.setNgayTao(LocalDateTime.now());

            NhatKyKiemToan logsaved = save(log);

            // chuyển trạng thái sản phẩm thành VI_PHAM
            sanPhamCoSanService.updateSanPhamCoSanTrangThai(
                    sanPhamId,
                    "VI_PHAM"
            );

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}