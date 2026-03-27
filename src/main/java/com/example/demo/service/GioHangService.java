package com.example.demo.service;

import com.example.demo.entity.GioHang;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.entity.VaiTroNguoiDung;
import com.example.demo.repository.GioHangRepository;
import com.example.demo.repository.SanPhamCoSanRepository;
import com.example.demo.repository.VaiTroNguoiDungRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GioHangService {

    @Autowired
    private GioHangRepository repository;

    @Autowired
    private VaiTroNguoiDungRepository vaiTroNguoiDungRepository;
    
    @Autowired
    private SanPhamCoSanRepository sanPhamCoSanRepository;
    
    @Autowired
    private GioHangRepository gioHangRepository;
    

    public GioHang add(GioHang gh) {
        gh.setNgayTao(LocalDateTime.now());
        gh.setNgayCapNhat(LocalDateTime.now());
        return repository.save(gh);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public GioHang addToCart(Long spcsId, UUID userId) {

        // lấy VaiTroNguoiDung (user)
        VaiTroNguoiDung vtnd = vaiTroNguoiDungRepository
                .findByNguoiDung_Id(userId)
                .stream()
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Không tìm thấy user"));

        // lấy sản phẩm
        SanPhamCoSan spcs = sanPhamCoSanRepository
                .findById(spcsId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // check đã có trong giỏ chưa
        Optional<GioHang> existing = gioHangRepository
                .findByVaiTroNguoiDung_IdAndSanPham_Id(
                        vtnd.getId(),
                        spcs.getSanPham().getId()
                );

        if (existing.isPresent()) {
            GioHang gh = existing.get();
            gh.setSoLuong(gh.getSoLuong() + 1);
            return gioHangRepository.save(gh);
        }

        // tạo mới
        GioHang gh = new GioHang();
        gh.setVaiTroNguoiDung(vtnd);
        gh.setSanPham(spcs.getSanPham());
        gh.setSoLuong(1);
        gh.setDonGiaSnapshot(spcs.getGia().doubleValue());
        gh.setDuocChon(true);

        return gioHangRepository.save(gh);
    }

}