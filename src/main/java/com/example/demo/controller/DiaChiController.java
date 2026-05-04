package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.CreateAddressRequest;
import com.example.demo.entity.DiaChi;
import com.example.demo.service.DiaChiService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dia-chi")
public class DiaChiController {

    @Autowired
    private DiaChiService diaChiService;

    @PostMapping
    public DiaChi createDiaChi(@RequestBody DiaChi diaChi) {
        return diaChiService.createDiaChi(diaChi);
    }

    @GetMapping
    public List<DiaChi> getAllDiaChi() {
        return diaChiService.getAllDiaChi();
    }

    @GetMapping("/{id}")
    public DiaChi getDiaChiById(@PathVariable Long id) {
        return diaChiService.getDiaChiById(id);
    }

    @PutMapping("/{id}")
    public DiaChi updateDiaChi(@PathVariable Long id, @RequestBody DiaChi diaChi) {
        return diaChiService.updateDiaChi(id, diaChi);
    }

    @DeleteMapping("/{id}")
    public void deleteDiaChi(@PathVariable Long id) {
        diaChiService.deleteDiaChi(id);
    }
}