package com.example.demo.service;

import com.example.demo.entity.DanhMuc;
import com.example.demo.enums.TrangThaiDanhMuc;
import com.example.demo.repository.DanhMucRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DanhMucService {

    private final DanhMucRepository repository;

    public List<DanhMuc> getAll() {
        return repository.findAll();
    }

    public Optional<DanhMuc> getById(Long id) {
        return repository.findById(id);
    }

    public DanhMuc create(DanhMuc danhMuc) {
        return repository.save(danhMuc);
    }

    public DanhMuc update(Long id, DanhMuc danhMuc) {

        DanhMuc old = repository.findById(id).orElseThrow();

        old.setTen(danhMuc.getTen());
        old.setMoTa(danhMuc.getMoTa());
        old.setTrangThai(danhMuc.getTrangThai());
        old.setThueSuatHienTai(danhMuc.getThueSuatHienTai());

        return repository.save(old);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<DanhMuc> getAllDanhMuc() {
        return repository.findByTrangThai(TrangThaiDanhMuc.HOAT_DONG);
    }    

}