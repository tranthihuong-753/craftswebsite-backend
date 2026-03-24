package com.example.demo.controller;

import com.example.demo.dto.SanPhamCoSanRequest;
import com.example.demo.dto.SanPhamModerationDTO;
import com.example.demo.dto.SellerProductDTO;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;
import com.example.demo.service.SanPhamCoSanService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/san-pham-co-san")
@RequiredArgsConstructor
public class SanPhamCoSanController {

    private final SanPhamCoSanService sanPhamCoSanService;

    @PostMapping
    public SanPhamCoSan create(@RequestBody SanPhamCoSanRequest request) {

        return sanPhamCoSanService.createSanPhamCoSan(
                request.getMoTa(),
                request.getGia(),
                request.getCanNang(),
                request.getChieuDai(),
                request.getChieuRong(),
                request.getChieuCao(),
                request.getGiaGoc(),
                request.getSoLuongBanDau(),
                request.getSoLuongHienTai(),
                request.getTrangThaiSPCS(),
                request.getSellerId(),
                request.getDanhMucId(),
                request.getTrangThaiSanPham(),
                request.getSoGioLamViecUocTinh(),
                request.getTrangThaiChungChi(),
                request.getMediaLinks()
        );
    }

    @GetMapping
    public Page<SellerProductDTO> getProducts(
            @RequestParam UUID sellerId,
            @RequestParam String status,
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return sanPhamCoSanService.getProducts(sellerId, status, search, pageable);
    }

    @GetMapping("/{id}")
    public SanPhamCoSan getById(@PathVariable Long id) {

        return sanPhamCoSanService.getById(id);
    }

    @PutMapping("/{id}")
    public SanPhamCoSan update(
            @PathVariable Long id,
            @RequestBody SanPhamCoSanRequest request
    ) {
        return sanPhamCoSanService.updateSanPhamCoSan(id, request);
    }
    
    @PutMapping("/{id}/delete")
    public ResponseEntity<?> deleteSanPham(@PathVariable Long id) {

        SanPhamCoSan result = sanPhamCoSanService.updateSanPhamCoSanTrangThai(id, "DA_XOA");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/moderation-products")
    public ResponseEntity<Page<SanPhamModerationDTO>> getModerationProducts(
            @RequestParam String status,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort) {
        
        Page<SanPhamModerationDTO> result = sanPhamCoSanService.getModerationProducts(status, search, page, size, sort);
        return ResponseEntity.ok(result);
    }
}
