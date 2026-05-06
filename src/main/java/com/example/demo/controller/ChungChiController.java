package com.example.demo.controller;

import com.example.demo.annotation.ApiDescription;
import com.example.demo.entity.ChungChi;
import com.example.demo.service.ChungChiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chung-chi")
@RequiredArgsConstructor
public class ChungChiController {

    private final ChungChiService service;

    @GetMapping
    @ApiDescription("Truy vấn danh sách chứng chỉ và điểm uy tín toàn hệ thống")
    public List<ChungChi> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    @ApiDescription("Xem chi tiết thông tin chứng chỉ và thống kê đánh giá theo ID")
    public ChungChi getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    @ApiDescription("Khởi tạo hồ sơ chứng chỉ mới cho sản phẩm hoặc người bán")
    public ChungChi create(@RequestBody ChungChi chungChi) {
        return service.create(chungChi);
    }

    @PutMapping("/{id}")
    @ApiDescription("Cập nhật trạng thái và thông tin định danh của chứng chỉ")
    public ChungChi update(
            @PathVariable Long id,
            @RequestBody ChungChi chungChi
    ) {
        return service.update(id, chungChi);
    }

    @DeleteMapping("/{id}")
    @ApiDescription("Gỡ bỏ hồ sơ chứng chỉ khỏi hệ thống quản lý")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}