package com.example.demo.controller;

import com.example.demo.entity.TaiKhoanNganHang;
import com.example.demo.service.TaiKhoanNganHangService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tai-khoan-ngan-hang")
@CrossOrigin
public class TaiKhoanNganHangController {

    private final TaiKhoanNganHangService service;

    public TaiKhoanNganHangController(TaiKhoanNganHangService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaiKhoanNganHang> getAll() {
        return service.getAll();
    }

    @PostMapping
    public TaiKhoanNganHang create(@RequestBody TaiKhoanNganHang entity) {
        return service.create(entity);
    }

    @PutMapping("/{id}")
    public TaiKhoanNganHang update(@PathVariable Long id, @RequestBody TaiKhoanNganHang entity) {
        return service.update(id, entity);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}