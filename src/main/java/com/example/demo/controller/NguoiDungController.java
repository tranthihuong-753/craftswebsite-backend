package com.example.demo.controller;

import java.util.HashMap;
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

import com.example.demo.annotation.ApiDescription;
import com.example.demo.entity.AnhVideo;
import com.example.demo.entity.NguoiDung;
import com.example.demo.service.NguoiDungService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/nguoidung")
public class NguoiDungController {
    @Autowired
    private NguoiDungService nguoiDungService;
    
    @PostMapping
    @ApiDescription("Khởi tạo hồ sơ người dùng mới trong hệ thống")
    public NguoiDung createNguoiDung(@RequestBody NguoiDung nguoiDung) {
        return nguoiDungService.createNguoiDung(nguoiDung);
    }

    // READ ALL
    @GetMapping
    @ApiDescription("Truy vấn danh sách toàn bộ người dùng và trạng thái định danh")
    public List<NguoiDung> getAllNguoiDung() {
        return nguoiDungService.getAllNguoiDung();
    }

    // READ BY ID
    @GetMapping("/{id}")
    @ApiDescription("Xem thông tin chi tiết hồ sơ người dùng theo mã định danh UUID")
    public NguoiDung getNguoiDungById(@PathVariable UUID id) {
        return nguoiDungService.getNguoiDungById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    @ApiDescription("Cập nhật thông tin cá nhân và cấu hình tài khoản người dùng")
    public NguoiDung updateNguoiDung(@PathVariable UUID id, @RequestBody NguoiDung nguoiDung) {
        return nguoiDungService.updateNguoiDung(id, nguoiDung);
    }
 
    // DELETE
    @DeleteMapping("/{id}")
    @ApiDescription("Vô hiệu hóa hoặc gỡ bỏ tài khoản người dùng khỏi hệ thống")
    public void deleteNguoiDung(@PathVariable UUID id) {
        nguoiDungService.deleteNguoiDung(id);
    }

    // TAO TAI KHOAN LEVEL 1 - SU DUNG SDT 
    @PostMapping("/create/sdt")
    @ApiDescription("Đăng ký tài khoản cấp độ 1 (Xác thực qua số điện thoại)")
    public ResponseEntity<?> taoBangSDT(@RequestBody Map<String,String> body) {
        String sdt = body.get("sdt");

        Map<String, Object> result = nguoiDungService.dangKyBangSDT(sdt);

        if(result == null){
            return ResponseEntity.status(401).body(
                    Map.of(
                            "success", false,
                            "message", "Số điện thoại không hợp lệ."
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true
                        ,"token", result.get("token")
                )
        );
    }

    // UPDATE TAI KHOAN LEVEL 2 - SU DUNG CCCD 
    @PostMapping("/scan/cccd")
    @ApiDescription("Nâng cấp xác thực cấp độ 2 (Định danh điện tử qua quét CCCD)")
    public ResponseEntity<?> scanCCCD(
        @RequestBody Map<String,String> body
        , HttpServletRequest request
    ) {
        String imageUrl = body.get("imageUrl");
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        UUID uid;
        try {
            uid = UUID.fromString(userId);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }
        NguoiDung nd = nguoiDungService.taoNguoiDungTuCCCD(
            uid,
            imageUrl
        );

        return ResponseEntity.ok(
            Map.of(
                "ten", nd.getTen(),
                "cccd", nd.getCccd()
            )
        );
    }

    // UPDATE TAI KHOAN LEVEL 3 - TAO USERNAME PASSWORD 
    @PostMapping("/setpassword")
    @ApiDescription("Hoàn tất xác thực cấp độ 3 (Thiết lập thông tin đăng nhập mật khẩu)")
    public ResponseEntity<?> setPassword(
        @RequestBody Map<String,String> body
        , HttpServletRequest request
    ){
        String userId = (String) request.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        UUID uid;
        try {
            uid = UUID.fromString(userId);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token không hợp lệ");
        }


        String password = body.get("password");
        String tenDangNhap = body.get("tenDangNhap");
        
        NguoiDung nd = nguoiDungService.datMatKhau(
            uid,
            password,
            tenDangNhap
        );

        return ResponseEntity.ok(
            Map.of(
                "tenDangNhap", nd.getTenDangNhap(),
                "trangThaiXacThuc", nd.getTrangThaiXacThuc()
            )
        );
    }

    @GetMapping("/tendangnhap")
    @ApiDescription("Truy vấn danh sách kiểm tra tên đăng nhập đã tồn tại")
    public List<String> getAllTenDangNhap() {
        return nguoiDungService.getAllTenDangNhap();
    }

    @PostMapping("/login")
    @ApiDescription("Xác thực đăng nhập hệ thống và cấp mã phiên làm việc (Token)")
    public ResponseEntity<?> login(@RequestBody Map<String,String> body) {

        String username = body.get("username");
        String password = body.get("password");

        Map<String,Object> result = nguoiDungService.login(username,password);

        if(result == null){
            return ResponseEntity.status(401).body(
                    Map.of(
                            "success", false,
                            "message", "Sai tài khoản hoặc mật khẩu"
                    )
            );
        }

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "token", result.get("token"),
                        "roles", result.get("roles")
                )
        );
    }
        
    // TU ID LAY ANH CHAN DUNG 
    @GetMapping("/me/anh-chan-dung")
    @ApiDescription("Truy vấn liên kết ảnh chân dung của người dùng đang đăng nhập")
    public ResponseEntity<?> getMyAnhChanDung(
        HttpServletRequest request
    ) {

        String userId = (String) request.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).body(
                Map.of(
                    "success", false,
                    "message", "Unauthorized"
                )
            );
        }

        UUID uid;
        try {
            uid = UUID.fromString(userId);
        } catch (Exception e) {
            return ResponseEntity.status(401).body(
                Map.of(
                    "success", false,
                    "message", "Token không hợp lệ"
                )
            );
        }

        AnhVideo anh = nguoiDungService.getAnhChanDungById(uid);

        String url = (anh != null) ? anh.getLink() : null;

        return ResponseEntity.ok(
            Map.of(
                "success", true,
                "anhChanDungUrl", url
            )
        );
    }

    // tu id lay ten nguoi dung
    @GetMapping("/me/ten")
    @ApiDescription("Truy vấn họ tên hiển thị của người dùng đang đăng nhập")
    public Map<String,Object> getMyTen(HttpServletRequest request) {

        String userId = (String) request.getAttribute("userId");

        String ten = nguoiDungService.getTenDangNhapById(
                UUID.fromString(userId)
        );

        return Map.of(
            "success", true,
            "ten", ten
        );
    }

}
