package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.VaiTro;
import com.example.demo.service.VaiTroService;

@RestController
@RequestMapping("/vai-tro")
public class VaiTroController {
    @Autowired
    private VaiTroService vaiTroService;
    
    @PostMapping
    public VaiTro createVaiTro(@RequestBody String loai) {
        return vaiTroService.createVaiTro(loai);
    }

    @GetMapping
    public List<VaiTro> getAllVaiTro() {
        return vaiTroService.getAllVaiTro();
    }

    @GetMapping("/{id}")
    public VaiTro getVaiTroById(@PathVariable Long id) {
        return vaiTroService.getVaiTroById(id);
    }

    @PutMapping("/{id}")
    public VaiTro updateVaiTro(@PathVariable Long id, @RequestBody VaiTro vaiTro) {
        return vaiTroService.updateVaiTro(id, vaiTro);
    }

    @DeleteMapping("/{id}")
    public void deleteVaiTro(@PathVariable Long id) {
        vaiTroService.deleteVaiTro(id);
    }

    @DeleteMapping("/all")
    public void deleteAllVaiTro() {
        List<VaiTro> vaiTroList = vaiTroService.getAllVaiTro();
        for (VaiTro vaiTro : vaiTroList) {
            vaiTroService.deleteVaiTro(vaiTro.getId());
        }
    }
}
