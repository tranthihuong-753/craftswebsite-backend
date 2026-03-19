// package com.example.demo.service;

// import com.example.demo.entity.Otp;
// import com.example.demo.repository.OtpRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.crypto.bcrypt.BCrypt;
// import org.springframework.stereotype.Service;

// import java.time.LocalDateTime;
// import java.util.Random;

// @Service
// public class OtpService {

//     @Autowired
//     private OtpRepository otpRepository;

//     // Gửi OTP
//     public void guiOtp(String phone, String deviceId) {

//         // 1. generate OTP
//         String otp = String.valueOf(new Random().nextInt(900000) + 100000);

//         // 2. hash
//         String hash = BCrypt.hashpw(otp, BCrypt.gensalt());

//         // 3. lưu DB
//         Otp entity = new Otp();
//         entity.setSoDienThoai(phone);
//         entity.setMaOtpHash(hash);
//         entity.setThoiGianTao(LocalDateTime.now());
//         entity.setThoiGianHetHan(LocalDateTime.now().plusMinutes(5));
//         entity.setSoLanThu(0);
//         entity.setDaSuDung(false);
//         entity.setMaThietBi(deviceId);

//         otpRepository.save(entity);

//         // TODO: gửi SMS
//         System.out.println("OTP (test): " + otp);
//     }

//     // Xác thực OTP
//     public boolean xacThucOtp(String phone, String otpInput) {

//         Otp otp = otpRepository
//                 .findTopBySoDienThoaiOrderByThoiGianTaoDesc(phone)
//                 .orElse(null);

//         if (otp == null) return false;

//         if (otp.getDaSuDung()) return false;

//         if (otp.getThoiGianHetHan().isBefore(LocalDateTime.now()))
//             return false;

//         if (otp.getSoLanThu() >= 5) return false;

//         // check hash
//         if (!BCrypt.checkpw(otpInput, otp.getMaOtpHash())) {
//             otp.setSoLanThu(otp.getSoLanThu() + 1);
//             otpRepository.save(otp);
//             return false;
//         }

//         otp.setDaSuDung(true);
//         otpRepository.save(otp);

//         return true;
//     }
// }