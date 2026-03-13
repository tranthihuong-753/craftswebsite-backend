package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.VaiTroNguoiDung;
import com.example.demo.service.VaiTroNguoiDungService;

@RestController
@RequestMapping("/vai-tro-nguoi-dung")
public class VaiTroNguoiDungController {
    @Autowired
    private VaiTroNguoiDungService vaiTroNguoiDungService;
    
    @PostMapping
    public VaiTroNguoiDung createVaiTroNguoiDung(@RequestBody VaiTroNguoiDung vtnd) {
        return vaiTroNguoiDungService.createVaiTroNguoiDung(vtnd);
    }

    @GetMapping
    public List<VaiTroNguoiDung> getAllVaiTroNguoiDung() {
        return vaiTroNguoiDungService.getAllVaiTroNguoiDung();
    }

    @GetMapping("/{id}")
    public VaiTroNguoiDung getVaiTroNguoiDungById(@PathVariable UUID id) {
        return vaiTroNguoiDungService.getVaiTroNguoiDungById(id);
    }

    @PutMapping("/{id}")
    public VaiTroNguoiDung updateVaiTroNguoiDung(@PathVariable UUID id,
                                                 @RequestBody VaiTroNguoiDung vtnd) {
        return vaiTroNguoiDungService.updateVaiTroNguoiDung(id, vtnd);
    }

    @DeleteMapping("/{id}")
    public void deleteVaiTroNguoiDung(@PathVariable UUID id) {
        vaiTroNguoiDungService.deleteVaiTroNguoiDung(id);
    }
}
