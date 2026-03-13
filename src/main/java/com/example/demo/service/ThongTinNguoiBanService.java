package com.example.demo.service;

import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.repository.ThongTinNguoiBanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ThongTinNguoiBanService {

    @Autowired
    private ThongTinNguoiBanRepository repository;

    // CREATE
    public ThongTinNguoiBan create(ThongTinNguoiBan data) {
        return repository.save(data);
    }

    // READ ALL
    public List<ThongTinNguoiBan> getAll() {
        return repository.findAll();
    }

    // READ BY ID
    public ThongTinNguoiBan getById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public ThongTinNguoiBan update(UUID id, ThongTinNguoiBan newData) {

        ThongTinNguoiBan old = repository.findById(id).orElse(null);

        if (old != null) {

            old.setNguoiDung(newData.getNguoiDung());
            old.setTienNhanCong(newData.getTienNhanCong());
            old.setTienThuongHieu(newData.getTienThuongHieu());
            old.setTrangThai(newData.getTrangThai());
            old.setAV_banner(newData.getAV_banner());
            old.setAV_hinhNen(newData.getAV_hinhNen());

            return repository.save(old);
        }

        return null;
    }

    // DELETE
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    // tu nguoimua check xem da co tai khoan nguoi ban chua
    public boolean checkNguoiBanByNguoiDungId(UUID ndId)
    {
        return repository.existsByNguoiDung_Id(ndId);
    }
}