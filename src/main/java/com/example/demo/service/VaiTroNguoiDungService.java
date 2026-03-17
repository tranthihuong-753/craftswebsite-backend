package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.VaiTroNguoiDung;
import com.example.demo.repository.VaiTroNguoiDungRepository;

@Service
public class VaiTroNguoiDungService {
    @Autowired
    private VaiTroNguoiDungRepository vaiTroNguoiDungRepository;
 
    public VaiTroNguoiDung createVaiTroNguoiDung(VaiTroNguoiDung vtnd) {
        return vaiTroNguoiDungRepository.save(vtnd);
    }

    public List<VaiTroNguoiDung> getAllVaiTroNguoiDung() {
        return vaiTroNguoiDungRepository.findAll();
    }

    public VaiTroNguoiDung getVaiTroNguoiDungById(UUID id) {
        return vaiTroNguoiDungRepository.findById(id).orElse(null);
    }

    @Transactional
    public VaiTroNguoiDung updateVaiTroNguoiDung(UUID id, VaiTroNguoiDung newVTND) {

        VaiTroNguoiDung vtnd = vaiTroNguoiDungRepository.findById(id).orElse(null);

        if (vtnd != null) {
            vtnd.setNguoiDung(newVTND.getNguoiDung());
            vtnd.setVaiTro(newVTND.getVaiTro());
            vtnd.setTrangThai(newVTND.getTrangThai());
            vtnd.setNgayDuyet(newVTND.getNgayDuyet());
            vtnd.setVTND_nguoiDuyet(newVTND.getVTND_nguoiDuyet());
            vtnd.setAV_hinhNen(newVTND.getAV_hinhNen());

            return vaiTroNguoiDungRepository.save(vtnd);
        }

        return null;
    }

    @Transactional
    public void deleteVaiTroNguoiDung(UUID id) {
        vaiTroNguoiDungRepository.deleteById(id);
    }

    
}
