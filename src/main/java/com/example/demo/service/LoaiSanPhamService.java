package com.example.demo.service;

import com.example.demo.entity.LoaiSanPham;
import com.example.demo.repository.LoaiSanPhamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoaiSanPhamService {

    private final LoaiSanPhamRepository repository;

    public List<LoaiSanPham> getAll() {
        return repository.findAll();
    }

    public Optional<LoaiSanPham> getById(Integer id) {
        return repository.findById(id);
    }

    public LoaiSanPham create(LoaiSanPham loaiSanPham) {
        return repository.save(loaiSanPham);
    }

    public LoaiSanPham update(Integer id, LoaiSanPham loaiSanPham) {

        LoaiSanPham old = repository.findById(id).orElseThrow();

        old.setLoai(loaiSanPham.getLoai());
        old.setMoTa(loaiSanPham.getMoTa());
        old.setTrangThai(loaiSanPham.getTrangThai());

        return repository.save(old);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

}