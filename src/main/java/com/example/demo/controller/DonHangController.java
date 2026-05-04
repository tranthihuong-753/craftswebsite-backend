package com.example.demo.controller;

import com.example.demo.entity.DonHang;
import com.example.demo.service.DonHangService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/donhang")
public class DonHangController {

    private final DonHangService service;

    public DonHangController(DonHangService service) {
        this.service = service;
    }

    @GetMapping
    public List<DonHang> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public DonHang getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public DonHang create(@RequestBody DonHang donHang) {
        return service.save(donHang);
    }

    @PutMapping("/{id}")
    public DonHang update(@PathVariable Long id, @RequestBody DonHang donHang) {
        donHang.setId(id);
        return service.save(donHang);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}