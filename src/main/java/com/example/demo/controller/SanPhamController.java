package com.example.demo.controller;

import com.example.demo.dto.SanPhamModerationDTO;
import com.example.demo.entity.SanPham;
import com.example.demo.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/san-pham")
@RequiredArgsConstructor
public class SanPhamController {

    private final SanPhamService service;

    @GetMapping
    public List<SanPham> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public SanPham getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public SanPham create(@RequestBody SanPham sanPham) {
        return service.create(sanPham);
    }

    @PutMapping("/{id}")
    public SanPham update(
            @PathVariable Long id,
            @RequestBody SanPham sanPham
    ) {
        return service.update(id, sanPham);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/{ttnbId}/dang-ban/count")
    public long demSanPhamDangBan(@PathVariable UUID ttnbId) {
        return service.demSanPhamDangBan(ttnbId);
    }

}