// package com.example.demo.entity;

// import jakarta.persistence.*;
// import lombok.Data;

// import java.time.LocalDateTime;
// import java.util.UUID;

// @Entity
// @Table(name = "tblOtp")
// @Data
// public class Otp {

//     @Id
//     @GeneratedValue(strategy = GenerationType.UUID)
//     private UUID id;

//     @Column(nullable = false)
//     private String soDienThoai;

//     @Column(name = "OTP_Code")
//     private String maOtpHash;

//     @Column(nullable = false)
//     private LocalDateTime thoiGianHetHan;

//     @Column(nullable = false)
//     private Integer soLanThu = 0;

//     @Column(nullable = false)
//     private Boolean daSuDung = false;

//     @Column(nullable = false)
//     private LocalDateTime thoiGianTao = LocalDateTime.now();

//     @Column(nullable = false)
//     private String loai; // REGISTER / LOGIN / RESET_PASSWORD

//     private String ipAddress;
//     private String maThietBi;
// }