package com.example.demo.controller;

import com.example.demo.dto.SellerRegisterRequest;
import com.example.demo.entity.NguoiDung;
import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.repository.NguoiDungRepository;
import com.example.demo.repository.TaiKhoanNganHangRepository;
import com.example.demo.repository.ThongTinNguoiBanRepository;
import com.example.demo.service.JwtService;
import com.example.demo.service.ThongTinNguoiBanService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/thong-tin-nguoi-ban")
public class ThongTinNguoiBanController {

    @Autowired
    private ThongTinNguoiBanService service;

    @Autowired
    private JwtService jwtService;

    // @PostMapping
    // public ThongTinNguoiBan createThongTinNguoiBan(@RequestBody ThongTinNguoiBan data) {
    //     return service.create(data);
    // }

    @GetMapping
    public List<ThongTinNguoiBan> getAllThongTinNguoiBan() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ThongTinNguoiBan getThongTinNguoiBanById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ThongTinNguoiBan updateThongTinNguoiBan(
            @PathVariable UUID id,
            @RequestBody ThongTinNguoiBan data
    ) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void deleteThongTinNguoiBan(@PathVariable UUID id) {
        service.delete(id);
    }

    // tu nguoimua check xem da co tai khoan nguoi ban chua 
    @GetMapping("/check/{ndId}")
    public ResponseEntity<Boolean> checkNguoiBanByNguoiDungId(@PathVariable UUID ndId)
    {
        boolean exists = service.checkNguoiBanByNguoiDungId(ndId);
        return ResponseEntity.ok(exists);
    }

    // TAO TAI KHOAN NGUOI BAN
    // @PostMapping
    // public ResponseEntity<?> registerSeller(
    //         @RequestBody SellerRegisterRequest req,
    //         HttpServletRequest request) {

    //     System.out.println(req);
    //     String userId = (String) request.getAttribute("userId");

    //     if (userId == null) {
    //         return ResponseEntity.status(401).body(
    //                 Map.of("error", "Unauthorized")
    //         );
    //     }

    //     UUID uid;
    //     try {
    //         uid = UUID.fromString(userId);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(401).body(
    //                 Map.of("error", "Token không hợp lệ")
    //         );
    //     }

    //     try {
    //         service.registerSeller(uid, req);

    //         return ResponseEntity.ok(
    //                 Map.of("message", "Đăng ký người bán thành công")
    //         );

    //     } catch (RuntimeException e) {
    //         return ResponseEntity
    //                 .status(HttpStatus.BAD_REQUEST)
    //                 .body(Map.of("error", e.getMessage()));
    //     }
    // }

    // TAO TAI KHOAN NGUOI BAN - VERSION 2 (KHONG DUNG MAP DE TRA VE MESSAGE)
    @PostMapping("/register")
    public ResponseEntity<?> registerSeller(
            @RequestBody SellerRegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        String userIdStr = (String) httpRequest.getAttribute("userId");

        if (userIdStr == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UUID userId = UUID.fromString(userIdStr);

        service.registerSeller(userId, request);

        return ResponseEntity.ok("Đăng ký seller thành công");
    }

    @GetMapping("/me")
    public ResponseEntity<UUID> getMySellerId(HttpServletRequest request) {

        String userId = (String) request.getAttribute("userId");

        UUID sellerId = service.getSellerIdByUserId(
                UUID.fromString(userId)
        );

        if (sellerId == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(sellerId);
    }

}