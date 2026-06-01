package com.example.demo.controller;

import com.example.demo.entity.NhatKyKiemToan;
import com.example.demo.service.NhatKyKiemToanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/nhat-ky-kiem-toan")
@RequiredArgsConstructor 
public class NhatKyKiemToanController {

    private final NhatKyKiemToanService service;

    @PostMapping("/approve/{id}")
    public String approveProduct(
            @PathVariable Long id,
            @RequestBody String reason
    ) {

        UUID adminId = UUID.randomUUID(); // sau này lấy từ JWT

        service.duyetSanPham(id, reason, adminId);

        return "APPROVED";
    }

    @PostMapping("/violation/{id}")
    public String violationProduct(
            @PathVariable Long id,
            @RequestBody String reason
    ) {

        UUID adminId = UUID.randomUUID();

        service.viPhamSanPham(id, reason, adminId);

        return "VIOLATION";
    }

}