package com.example.demo.controller;

import com.example.demo.annotation.ApiDescription;
import com.example.demo.entity.AnhVideo;
import com.example.demo.service.AnhVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anh-video")
public class AnhVideoController {

    @Autowired
    private AnhVideoService service;

    // CREATE
    @PostMapping
    @ApiDescription("Đăng tải và lưu trữ tài nguyên đa phương tiện (Ảnh/Video)")
    public AnhVideo create(@RequestBody AnhVideo data) {
        return service.create(data);
    }

    // READ ALL
    @GetMapping
    @ApiDescription("Truy vấn danh mục tất cả tệp đa phương tiện trên hệ thống")
    public List<AnhVideo> getAll() {
        return service.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    @ApiDescription("Xem thông tin chi tiết và liên kết tệp đa phương tiện theo ID")
    public AnhVideo getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    @ApiDescription("Cập nhật thông tin hoặc đường dẫn tệp đa phương tiện")
    public AnhVideo update(@PathVariable Long id, @RequestBody AnhVideo data) {
        return service.update(id, data);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ApiDescription("Gỡ bỏ hoàn toàn tệp đa phương tiện khỏi hệ thống")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}