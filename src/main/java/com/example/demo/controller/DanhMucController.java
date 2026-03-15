package com.example.demo.controller;

import com.example.demo.entity.DanhMuc;
import com.example.demo.service.DanhMucService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/danh-muc")
@RequiredArgsConstructor
public class DanhMucController {

    private final DanhMucService service;

    @GetMapping
    public List<DanhMuc> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public DanhMuc getById(@PathVariable Integer id) {
        return service.getById(id).orElseThrow();
    }

    @PostMapping
    public DanhMuc create(@RequestBody DanhMuc danhMuc) {
        return service.create(danhMuc);
    }

    @PutMapping("/{id}")
    public DanhMuc update(
            @PathVariable Integer id,
            @RequestBody DanhMuc danhMuc
    ) {
        return service.update(id, danhMuc);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }

}