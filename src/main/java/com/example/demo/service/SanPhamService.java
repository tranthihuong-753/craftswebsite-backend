package com.example.demo.service;

import com.example.demo.entity.SanPham;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.repository.SanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SanPhamService {

    private final SanPhamRepository repository;

    public List<SanPham> getAll() {
        return repository.findAll();
    }

    public SanPham getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public SanPham create(SanPham sanPham) {
        return repository.save(sanPham);
    }

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

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public long demSanPhamDangBan(UUID ttNguoiBanId) {

        return repository.countByThongTinNguoiBan_IdAndTrangThai(
                ttNguoiBanId,
                TrangThaiSanPham.DANG_BAN
        );

    }

}