// package com.example.demo.controller;

// import com.example.demo.service.OtpService;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/otp")
// public class OtpController {

//     @Autowired
//     private OtpService otpService;

//     // Gửi OTP
//     @PostMapping("/send-otp")
//     public void sendOtp(@RequestParam String phone,
//                         @RequestParam String deviceId) {
//         otpService.guiOtp(phone, deviceId);
//     }

//     // Xác thực OTP
//     @PostMapping("/verify-otp")
//     public boolean verifyOtp(@RequestParam String phone,
//                              @RequestParam String otpInput) {
//         return otpService.xacThucOtp(phone, otpInput);
//     }
// }