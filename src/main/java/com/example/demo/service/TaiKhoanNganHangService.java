package com.example.demo.service;

import com.example.demo.entity.TaiKhoanNganHang;
import com.example.demo.repository.TaiKhoanNganHangRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaiKhoanNganHangService {

    private final TaiKhoanNganHangRepository repository;

    public TaiKhoanNganHangService(TaiKhoanNganHangRepository repository) {
        this.repository = repository;
    }

    public List<TaiKhoanNganHang> getAll() {
        return repository.findAll();
    }

    public TaiKhoanNganHang create(TaiKhoanNganHang entity) {
        entity.setNgayTao(LocalDateTime.now());
        return repository.save(entity);
    }

    public TaiKhoanNganHang update(Long id, TaiKhoanNganHang data) {
        TaiKhoanNganHang old = repository.findById(id).orElseThrow();

        old.setMaNganHang(data.getMaNganHang());
        old.setTenNganHang(data.getTenNganHang());
        old.setSoTaiKhoan(data.getSoTaiKhoan());
        old.setTenTaiKhoan(data.getTenTaiKhoan());
        old.setChuoiVietQR(data.getChuoiVietQR());
        old.setTrangThai(data.getTrangThai());

        return repository.save(old);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}