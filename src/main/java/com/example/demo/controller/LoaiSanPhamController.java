package com.example.demo.controller;

import com.example.demo.entity.LoaiSanPham;
import com.example.demo.service.LoaiSanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loai-san-pham")
@RequiredArgsConstructor
public class LoaiSanPhamController {

    private final LoaiSanPhamService service;

    @GetMapping
    public List<LoaiSanPham> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public LoaiSanPham getById(@PathVariable Integer id) {
        return service.getById(id).orElseThrow();
    }

    @PostMapping
    public LoaiSanPham create(@RequestBody LoaiSanPham loaiSanPham) {
        return service.create(loaiSanPham);
    }

    @PutMapping("/{id}")
    public LoaiSanPham update(
            @PathVariable Integer id,
            @RequestBody LoaiSanPham loaiSanPham
    ) {
        return service.update(id, loaiSanPham);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

}