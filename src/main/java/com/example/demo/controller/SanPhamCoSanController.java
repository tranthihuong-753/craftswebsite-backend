package com.example.demo.controller;

import com.example.demo.dto.SanPhamCoSanRequest;
import com.example.demo.dto.SanPhamModerationDTO;
import com.example.demo.dto.SanPhamSearchDTO;
import com.example.demo.dto.SellerProductDTO;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;
import com.example.demo.service.SanPhamCoSanService;
import com.example.demo.service.ThongTinNguoiBanService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private SanPhamCoSanService sanPhamCoSanService;

    @Autowired
    private ThongTinNguoiBanService thongTinNguoiBanService;

    // TAO SAN PHAM CO SAN 
    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody SanPhamCoSanRequest request,
            HttpServletRequest httpRequest
    ) {
        String userIdStr = (String) httpRequest.getAttribute("userId");

        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UUID userId = UUID.fromString(userIdStr);

        SanPhamCoSan result = sanPhamCoSanService.createSanPhamCoSan(
                request,
                userId
        );

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public Page<SellerProductDTO> getProducts(
            @RequestParam String status,
            @RequestParam(required = false) String search,
            Pageable pageable,
            HttpServletRequest request
    ) {
        String userIdStr = (String) request.getAttribute("userId");

        if (userIdStr == null) {
            throw new RuntimeException("Unauthorized");
        }

        UUID userId = UUID.fromString(userIdStr);

        UUID sellerId = thongTinNguoiBanService.getSellerIdByUserId(userId);

        if (sellerId == null) {
            throw new RuntimeException("Chưa có tài khoản người bán");
        }

        return sanPhamCoSanService.getProducts(sellerId, status, search, pageable);
    }

    // LAY SAN PHAM BANG Id 
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
        @PathVariable Long id
        , HttpServletRequest request
    ) {

        String userIdStr = (String) request.getAttribute("userId");

        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UUID userId = UUID.fromString(userIdStr);

        SanPhamCoSan result = sanPhamCoSanService.getById(id, userId);

        return ResponseEntity.ok(result);

    }

    // UPDATE SAN PHAM BANG Id SAN PHAM
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody SanPhamCoSanRequest request,
            HttpServletRequest httpRequest
    ) {
        String userIdStr = (String) httpRequest.getAttribute("userId");

        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UUID userId = UUID.fromString(userIdStr);

        SanPhamCoSan result = sanPhamCoSanService.updateSanPhamCoSan(id, request, userId);

        return ResponseEntity.ok(result);
    }
    
    // XOA SAN PHAM BANG Id SAN PHAM
    @PutMapping("/{id}/delete")
    public ResponseEntity<?> deleteSanPham(
        @PathVariable Long id
        , HttpServletRequest request
    ) {
        String userIdStr = (String) request.getAttribute("userId");

        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UUID userId = UUID.fromString(userIdStr);

        sanPhamCoSanService.deleteSanPham(id, userId);

        return ResponseEntity.ok("Đã xóa");
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

    // TIM KIEM SAN PHAM BEN USER 
    @GetMapping("/moderation-products-user") 
    public Page<SanPhamSearchDTO> search(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return sanPhamCoSanService.search(search, PageRequest.of(page, size));
    }

}
