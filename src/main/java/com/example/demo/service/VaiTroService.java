package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.VaiTro;
import com.example.demo.repository.VaiTroRepository;

@Service
public class VaiTroService {
    @Autowired
    private VaiTroRepository vaiTroRepository;

    // CREATE
    public VaiTro createVaiTro(String loai) {
        VaiTro vaiTro = new VaiTro();
        vaiTro.setLoai(loai);
        return vaiTroRepository.save(vaiTro);
    }

    // READ ALL
    public List<VaiTro> getAllVaiTro() {
        return vaiTroRepository.findAll();
    }

    // READ BY ID
    public VaiTro getVaiTroById(Long id) {
        return vaiTroRepository.findById(id).orElse(null);
    }

    // UPDATE
    public VaiTro updateVaiTro(Long id, VaiTro newVaiTro) {

        VaiTro vaiTro = vaiTroRepository.findById(id).orElse(null);

        if (vaiTro != null) {
            vaiTro.setLoai(newVaiTro.getLoai());
            return vaiTroRepository.save(vaiTro);
        }

        return null;
    }

    // DELETE
    public void deleteVaiTro(Long id) {
        vaiTroRepository.deleteById(id);
    }

}
