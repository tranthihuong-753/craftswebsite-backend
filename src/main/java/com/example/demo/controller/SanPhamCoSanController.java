package com.example.demo.controller;

import com.example.demo.dto.SanPhamCoSanRequest;
import com.example.demo.dto.SellerProductDTO;
import com.example.demo.entity.SanPhamCoSan;
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
            @RequestParam String status,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "desc") String sort
    ) {

        Sort sorting = sort.equals("asc")
                ? Sort.by("sanPham.ngayTao").ascending()
                : Sort.by("sanPham.ngayTao").descending();

        Pageable pageable = PageRequest.of(page - 1, size, sorting);

        return sanPhamCoSanService.getProducts(status, pageable);
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

        SanPhamCoSan result = sanPhamCoSanService.updateSanPhamCoSanTrangThai(id);

        return ResponseEntity.ok(result);
    }

}

// {
//   "moTa": "Vòng tay handmade từ gỗ",
//   "gia": 150000,
//   "canNang": 0.2,
//   "chieuDai": 10,
//   "chieuRong": 5,
//   "chieuCao": 2,
//   "giaGoc": 200000,
//   "soLuongBanDau": 10,
//   "soLuongHienTai": 10,

//   "trangThaiSPCS": "CON_HANG",

//   "sellerId": "0f1c2a9c-9c41-4a6f-8c7a-1f98f5a12345",
//   "danhMucId": 3,

//   "trangThaiSanPham": "DANG_BAN",
//   "soGioLamViecUocTinh": 5,

//   "trangThaiChungChi": "HOAT_DONG"
// }