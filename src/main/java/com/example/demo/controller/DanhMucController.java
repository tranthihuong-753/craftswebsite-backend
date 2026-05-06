package com.example.demo.controller;

import com.example.demo.annotation.ApiDescription;
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

    // @GetMapping
    // public List<DanhMuc> getAll() {
    //     return service.getAll();
    // }

    @GetMapping("/{id}")
    @ApiDescription("Truy vấn thông tin chi tiết danh mục sản phẩm theo mã định danh")
    public DanhMuc getById(@PathVariable Long id) {
        return service.getById(id).orElseThrow();
    }

    @PostMapping
    @ApiDescription("Khởi tạo danh mục hàng hóa mới và thiết lập thuế suất ban đầu")
    public DanhMuc create(@RequestBody DanhMuc danhMuc) {
        return service.create(danhMuc);
    }

    @PutMapping("/{id}")
    @ApiDescription("Cập nhật thông tin mô tả, trạng thái hoạt động hoặc thuế suất hiện hành của danh mục")
    public DanhMuc update(
            @PathVariable Long id,
            @RequestBody DanhMuc danhMuc
    ) {
        return service.update(id, danhMuc);
    }

    @DeleteMapping("/{id}")
    @ApiDescription("Gỡ bỏ danh mục hàng hóa khỏi hệ thống quản lý chung")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    // LAY TOAN BO DANH MUC 
    @GetMapping
    @ApiDescription("Truy vấn toàn bộ danh sách danh mục ngành hàng đang hoạt động trên hệ thống")
    public List<DanhMuc> getDanhMuc() {
        return service.getAllDanhMuc();
    }    
}