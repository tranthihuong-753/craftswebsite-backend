package com.example.demo.controller;

import com.example.demo.entity.SanPham;
import com.example.demo.service.SanPhamService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
    public SanPham getById(@PathVariable Integer id) {
        return service.getById(id);
    }

    @PostMapping
    public SanPham create(@RequestBody SanPham sanPham) {
        return service.create(sanPham);
    }

    @PutMapping("/{id}")
    public SanPham update(
            @PathVariable Integer id,
            @RequestBody SanPham sanPham
    ) {
        return service.update(id, sanPham);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

    @GetMapping("/{ttnbId}/dang-ban/count")
    public long demSanPhamDangBan(@PathVariable UUID ttnbId) {
        return service.demSanPhamDangBan(ttnbId);
    }

}