package com.example.demo.controller;

import com.example.demo.entity.GioHang;
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

    @PostMapping
    public GioHang add(@RequestBody GioHang gh) {
        return service.add(gh);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

@PostMapping("/cart/add/{spcsId}")
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

}