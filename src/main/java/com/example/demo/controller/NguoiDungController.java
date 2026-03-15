package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.NguoiDung;
import com.example.demo.service.NguoiDungService;

@RestController
@RequestMapping("/nguoi-dung")
public class NguoiDungController {
    @Autowired
    private NguoiDungService nguoiDungService;
    
    @PostMapping
    public NguoiDung createNguoiDung(@RequestBody NguoiDung nguoiDung) {
        return nguoiDungService.createNguoiDung(nguoiDung);
    }

    // READ ALL
    @GetMapping
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungService.getAllNguoiDung();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public NguoiDung getNguoiDungById(@PathVariable UUID id) {
        return nguoiDungService.getNguoiDungById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public NguoiDung updateNguoiDung(@PathVariable UUID id, @RequestBody NguoiDung nguoiDung) {
        return nguoiDungService.updateNguoiDung(id, nguoiDung);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteNguoiDung(@PathVariable UUID id) {
        nguoiDungService.deleteNguoiDung(id);
    }

    // GET BY CCCD
    // @PostMapping("/scan-cccd")
    // public NguoiDung scanCCCD(@RequestBody Map<String,String> body) {
    //     System.out.println("scanCCCD");
    //     String imageUrl = body.get("imageUrl");

    //     return nguoiDungService.taoNguoiDungTuCCCD(imageUrl);
    // }

    // TAo bang sdt 
    @PostMapping("/create/sdt")
    public NguoiDung taoBangSDT(@RequestBody Map<String,String> body) {
        String sdt = body.get("sdt");

        return nguoiDungService.dangKyBangSDT(sdt);
    }

    // update voi cccd 
    @PostMapping("/scan-cccd")
    public NguoiDung scanCCCD(@RequestBody Map<String,String> body) {

        String imageUrl = body.get("imageUrl");
        String userId = body.get("userId");

        return nguoiDungService.taoNguoiDungTuCCCD(
                UUID.fromString(userId),
                imageUrl
        );
    }


    @PostMapping("/set-password")
    public NguoiDung setPassword(@RequestBody Map<String,String> body){

        String userId = body.get("userId");
        String password = body.get("password");
        String tenDangNhap = body.get("tenDangNhap");
        
        return nguoiDungService.datMatKhau(
                UUID.fromString(userId),
                password,
                tenDangNhap
        );
    }

    @GetMapping("/ten-dang-nhap")
    public List<String> getAllTenDangNhap() {
        return nguoiDungService.getAllTenDangNhap();
    }

    // @PostMapping("/login")
    // public boolean login(@RequestBody Map<String,String> body) {
    //     String username = body.get("username");
    //     String password = body.get("password");
    //     return nguoiDungService.login(username, password);
    // }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {

        String username = body.get("username");
        String password = body.get("password");

        UUID userId = nguoiDungService.loginAndGetUserId(username, password);

        if(userId == null){
            return ResponseEntity.status(401).body(
                    Map.of("success", false, "message", "Sai tài khoản hoặc mật khẩu")
            );
        }

        return ResponseEntity.ok(
                Map.of("success", true, "userId", userId)
        );
    }

    // tu id lay anhchandung 
    @GetMapping("/{id}/anh-chan-dung")
    public Map<String,Object> getAnhChanDungById(@PathVariable UUID id) {

        String anhChanDungUrl = nguoiDungService.getAnhChanDungById(id).getLink();

        return Map.of(
                "success", true,
                "anhChanDungUrl", anhChanDungUrl
        );
    }

    // tu id lay ten nguoi dung
    @GetMapping("/{id}/ten")
    public Map<String,Object> getTenById(@PathVariable UUID id) {

        String ten = nguoiDungService.getTenById(id);

        return Map.of(
                "success", true,
                "ten", ten
        );
    }

}
