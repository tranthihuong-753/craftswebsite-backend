package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.DiaChi;
import com.example.demo.repository.DiaChiRepository;

@Service
public class DiaChiService {

    @Autowired
    private DiaChiRepository diaChiRepository;

    public DiaChi createDiaChi(DiaChi diaChi) {
        return diaChiRepository.save(diaChi);
    }

    public List<DiaChi> getAllDiaChi() {
        return diaChiRepository.findAll();
    }

    public DiaChi getDiaChiById(Long id) {
        return diaChiRepository.findById(id).orElse(null);
    }

    @Transactional
    public DiaChi updateDiaChi(Long id, DiaChi newDiaChi) {

        DiaChi diaChi = diaChiRepository.findById(id).orElse(null);

        if (diaChi != null) {
            diaChi.setThanhPho(newDiaChi.getThanhPho());
            diaChi.setHuyen(newDiaChi.getHuyen());
            diaChi.setXa(newDiaChi.getXa());
            diaChi.setCuThe(newDiaChi.getCuThe());
            diaChi.setThietLapMacDinh(newDiaChi.getThietLapMacDinh());
            diaChi.setStatus(newDiaChi.getStatus());
            diaChi.setVaiTroNguoiDung(newDiaChi.getVaiTroNguoiDung());

            return diaChiRepository.save(diaChi);
        }

        return null;
    }

    @Transactional
    public void deleteDiaChi(Long id) {
        diaChiRepository.deleteById(id);
    }
}