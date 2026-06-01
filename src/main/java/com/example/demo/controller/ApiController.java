package com.example.demo.controller;

import com.example.demo.annotation.ApiDescription;
import com.example.demo.entity.Api;
import com.example.demo.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-manager")
public class ApiController {

    @Autowired
    private ApiService apiService;

    // CREATE
    @PostMapping
    @ApiDescription("Tạo mới thông tin API hệ thống")
    public Api create(@RequestBody Api api) {
        return apiService.create(api);
    }

    // READ ALL
    @GetMapping
    @ApiDescription("Lấy danh sách tất cả API trong hệ thống")
    public List<Api> getAll() {
        return apiService.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    @ApiDescription("Lấy thông tin chi tiết một API theo ID")
    public Api getById(@PathVariable Long id) {
        return apiService.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    @ApiDescription("Cập nhật thông tin cấu hình API")
    public Api update(@PathVariable Long id, @RequestBody Api api) {
        return apiService.update(id, api);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ApiDescription("Xóa API khỏi danh sách quản lý")
    public void delete(@PathVariable Long id) {
        apiService.delete(id);
    }

    // ACTIVATE
    @PutMapping("/{id}/activate")
    @ApiDescription("Kích hoạt trạng thái hoạt động của API")
    public Api activate(@PathVariable Long id) {
        return apiService.activate(id);
    }

    // DEACTIVATE
    @PutMapping("/{id}/deactivate")
    @ApiDescription("Vô hiệu hóa (ngưng sử dụng) API")
    public Api deactivate(@PathVariable Long id) {
        return apiService.deactivate(id);
    }
}