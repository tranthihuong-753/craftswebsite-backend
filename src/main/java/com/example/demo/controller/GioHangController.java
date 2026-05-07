package com.example.demo.controller;

import com.example.demo.annotation.ApiDescription;
import com.example.demo.entity.GioHang;
import com.example.demo.repository.GioHangRepository;
import com.example.demo.service.GioHangService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gio-hang")
public class GioHangController {

    @Autowired
    private GioHangService service;

    @Autowired
    private GioHangRepository gioHangRepository;

    // THEM SP VAO GIO HANG 
    @PostMapping("/cart/add/{spcsId}")
    @ApiDescription("Thêm sản phẩm vào giỏ hàng và thực hiện chốt dữ liệu đơn giá snapshot")
    public ResponseEntity<?> addToCart(
            @PathVariable Long spcsId,
            HttpServletRequest request
    ) {
        String userIdStr = (String) request.getAttribute("userId");

        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UUID userId = UUID.fromString(userIdStr);

        GioHang gh = service.addToCart(spcsId, userId);

        return ResponseEntity.ok(gh);
    }

    // LAY SAN PHAM THEO USER ID DE HIEN THI TRONG GIO HANG 
    @GetMapping
    @ApiDescription("Truy vấn danh sách sản phẩm trong giỏ hàng theo định danh người dùng")
    public ResponseEntity<?> getCart(HttpServletRequest request) {

        String userIdStr = (String) request.getAttribute("userId");

        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UUID userId = UUID.fromString(userIdStr);

        return ResponseEntity.ok(service.getCart(userId));
    }

    // TICK/ BO TICK SAN PHAM PHUC VU CHO DAT HANG 
    @PatchMapping("/{id}/check")
    @ApiDescription("Thay đổi trạng thái lựa chọn sản phẩm phục vụ quy trình tạo đơn hàng")
    public ResponseEntity<?> toggleCheck(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        UUID userId = UUID.fromString(userIdStr);

        GioHang gh = gioHangRepository.findById(id).orElseThrow();

        if (!gh.getVaiTroNguoiDung().getNguoiDung().getId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        gh.setDuocChon(!gh.getDuocChon());
        gioHangRepository.save(gh);

        return ResponseEntity.ok().build();
    }

    // CHINH SUA SO LUONG SAN PHAM TRONG GIO HANG 
    @PatchMapping("/{id}/quantity")
    @ApiDescription("Cập nhật số lượng sản phẩm mong muốn trong giỏ hàng")
    public ResponseEntity<?> updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            HttpServletRequest request
    ) {

        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        UUID userId = UUID.fromString(userIdStr);

        GioHang gh = gioHangRepository.findById(id).orElseThrow();

        if (!gh.getVaiTroNguoiDung().getNguoiDung().getId().equals(userId)) {
            return ResponseEntity.status(403).build();
        }

        gh.setSoLuong(quantity);
        gioHangRepository.save(gh);

        return ResponseEntity.ok().build();
    }

    // XOA SAN PHAM KHOI GIO HANG 
    @DeleteMapping("/{id}")
    @ApiDescription("Gỡ bỏ sản phẩm khỏi giỏ hàng cá nhân")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            HttpServletRequest request
    ) {

        String userIdStr = (String) request.getAttribute("userId");
        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        UUID userId = UUID.fromString(userIdStr);
        System.out.println("userId: " + userId);

        GioHang gh = gioHangRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));

        // 🔥 CHECK QUYỀN SỞ HỮU
        if (!gh.getVaiTroNguoiDung().getNguoiDung().getId().equals(userId)) {
            System.out.println("userId: " + gh.getVaiTroNguoiDung().getId());
            System.out.println("Khong khop ID user");
            return ResponseEntity.status(403).body("Forbidden");
        }

        gioHangRepository.delete(gh);

        return ResponseEntity.ok().build();
    }

}