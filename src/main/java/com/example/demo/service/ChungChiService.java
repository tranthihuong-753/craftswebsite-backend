package com.example.demo.service;

import com.example.demo.entity.ChungChi;
import com.example.demo.repository.ChungChiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChungChiService {

    private final ChungChiRepository repository;

    public List<ChungChi> getAll() {
        return repository.findAll();
    }

    public ChungChi getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public ChungChi create(ChungChi chungChi) {
        return repository.save(chungChi);
    }

    public ChungChi update(Long id, ChungChi chungChi) {

        ChungChi old = repository.findById(id).orElseThrow();

        old.setLoai(chungChi.getLoai());
        old.setLoaiMucTieu(chungChi.getLoaiMucTieu());
        old.setIdMucTieu(chungChi.getIdMucTieu());
        old.setDiemTrungBinh(chungChi.getDiemTrungBinh());
        old.setTongDanhGia(chungChi.getTongDanhGia());
        old.setTrangThai(chungChi.getTrangThai());

        return repository.save(old);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}