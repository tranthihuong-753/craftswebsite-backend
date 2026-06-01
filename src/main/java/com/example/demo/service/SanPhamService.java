package com.example.demo.service;

import com.example.demo.dto.SanPhamModerationDTO;
import com.example.demo.entity.SanPham;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.repository.SanPhamRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class SanPhamService {

    @Autowired
    private SanPhamRepository repository;

    @Autowired
    private ThongTinNguoiBanService thongTinNguoiBanService;

    public List<SanPham> getAll() {
        return repository.findAll();
    }

    public SanPham getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public SanPham create(SanPham sanPham) {
        return repository.save(sanPham);
    }

    @Transactional
    public SanPham update(Long id, SanPham sanPham) {

        SanPham old = repository.findById(id).orElseThrow();

        old.setThongTinNguoiBan(sanPham.getThongTinNguoiBan());
        old.setLoaiSanPham(sanPham.getLoaiSanPham());
        old.setDanhMuc(sanPham.getDanhMuc());
        old.setTrangThai(sanPham.getTrangThai());
        old.setSoGioLamViecUocTinh(sanPham.getSoGioLamViecUocTinh());
        old.setChungChi(sanPham.getChungChi());

        return repository.save(old);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public long demSanPhamDangBanByUserId(UUID userId) {

        UUID sellerId = thongTinNguoiBanService.getSellerIdByUserId(userId);

        if (sellerId == null) {
            throw new RuntimeException("Bạn chưa là người bán");
        }

        return repository.countByThongTinNguoiBan_IdAndTrangThai(
                sellerId,
                TrangThaiSanPham.DANG_BAN
        );
    }

}