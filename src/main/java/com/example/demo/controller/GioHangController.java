package com.example.demo.controller;

import com.example.demo.entity.GioHang;
import com.example.demo.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gio-hang")
public class GioHangController {

    @Autowired
    private GioHangService service;

    @PostMapping
    public GioHang add(@RequestBody GioHang gh) {
        return service.add(gh);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}