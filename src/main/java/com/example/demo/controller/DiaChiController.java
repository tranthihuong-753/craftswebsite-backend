package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.annotation.ApiDescription;
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
    @ApiDescription("Thêm mới địa chỉ liên hệ và giao hàng cho người dùng")
    public DiaChi createDiaChi(@RequestBody DiaChi diaChi) {
        return diaChiService.createDiaChi(diaChi);
    }

    @GetMapping
    @ApiDescription("Truy vấn danh sách tất cả các địa chỉ lưu trú trên hệ thống")
    public List<DiaChi> getAllDiaChi() {
        return diaChiService.getAllDiaChi();
    }

    @GetMapping("/{id}")
    @ApiDescription("Xem thông tin chi tiết địa chỉ cụ thể theo mã định danh")
    public DiaChi getDiaChiById(@PathVariable Long id) {
        return diaChiService.getDiaChiById(id);
    }

    @PutMapping("/{id}")
    @ApiDescription("Cập nhật thông tin chi tiết hoặc thay đổi trạng thái địa chỉ")
    public DiaChi updateDiaChi(@PathVariable Long id, @RequestBody DiaChi diaChi) {
        return diaChiService.updateDiaChi(id, diaChi);
    }

    @DeleteMapping("/{id}")
    @ApiDescription("Gỡ bỏ địa chỉ khỏi danh sách quản lý của người dùng")
    public void deleteDiaChi(@PathVariable Long id) {
        diaChiService.deleteDiaChi(id);
    }
}