// package com.example.demo.service;

// import com.example.demo.entity.*;
// import com.example.demo.enums.ND_Trangthaixacthuc;
// import com.example.demo.model.ND_CCCD;
// import com.example.demo.repository.*;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import java.util.Arrays;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;

// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyString;
// import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class)
// public class NguoiDungServiceTest {

//     @Mock private NguoiDungRepository nguoiDungRepository;
//     @Mock private CCCDService cccdService;
//     @Mock private AnhVideoService anhVideoService;
//     @Mock private VaiTroRepository vaiTroRepository;
//     @Mock private VaiTroNguoiDungRepository vtndRepository;

//     @InjectMocks private NguoiDungService nguoiDungService;

//     // --- MỨC 1: SĐT (R1) ---

//     @Test
//     @DisplayName("TC_AUTH_001: Đăng ký SĐT mới hợp lệ")
//     void TC_AUTH_001() {
//         String sdt = "0987654321";
//         when(nguoiDungRepository.findBySdt(sdt)).thenReturn(Optional.empty());
//         when(nguoiDungRepository.save(any(NguoiDung.class))).thenAnswer(i -> i.getArguments()[0]);

//         NguoiDung result = nguoiDungService.dangKyBangSDT(sdt);
//         assertEquals(sdt, result.getSdt());
//         assertEquals(ND_Trangthaixacthuc.PHONE_VERIFIED, result.getTrangThaiXacThuc());
//     }

//     @Test
//     @DisplayName("TC_AUTH_002: Đăng ký SĐT đã tồn tại (Trả về user cũ)")
//     void TC_AUTH_002() {
//         String sdt = "0123456789";
//         NguoiDung existingNd = new NguoiDung();
//         existingNd.setSdt(sdt);
        
//         when(nguoiDungRepository.findBySdt(sdt)).thenReturn(Optional.of(existingNd));

//         NguoiDung result = nguoiDungService.dangKyBangSDT(sdt);
        
//         assertEquals(sdt, result.getSdt());
//         verify(nguoiDungRepository, never()).save(any()); // Không được gọi hàm save
//     }

//     // --- MỨC 2: CCCD (R2) ---

//     @Test
//     @DisplayName("TC_AUTH_003: Scan CCCD với User không tồn tại (Negative)")
//     void TC_AUTH_003() {
//         UUID fakeId = UUID.randomUUID();
//         when(nguoiDungRepository.findById(fakeId)).thenReturn(Optional.empty());

//         assertThrows(RuntimeException.class, () -> {
//             nguoiDungService.taoNguoiDungTuCCCD(fakeId, "img_url");
//         });
//     }

//     @Test
//     @DisplayName("TC_AUTH_004: Scan CCCD thành công (Mức 2)")
//     void TC_AUTH_004() {
//         UUID userId = UUID.randomUUID();
//         NguoiDung nd = new NguoiDung();
//         ND_CCCD mockCccd = new ND_CCCD();
//         mockCccd.setHoTen("HOANG THI HUONG");

//         when(nguoiDungRepository.findById(userId)).thenReturn(Optional.of(nd));
//         when(cccdService.scanCCCD(any())).thenReturn(mockCccd);
//         when(anhVideoService.create(any())).thenReturn(new AnhVideo());
//         when(nguoiDungRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

//         NguoiDung result = nguoiDungService.taoNguoiDungTuCCCD(userId, "url");
//         assertEquals("HOANG THI HUONG", result.getTen());
//         assertEquals(ND_Trangthaixacthuc.CCCD, result.getTrangThaiXacThuc());
//     }

//     // --- MỨC 3: PASSWORD (R3) ---

//     @Test
//     @DisplayName("TC_AUTH_005: Đặt mật khẩu thành công (Mức 3)")
//     void TC_AUTH_005() {
//         UUID userId = UUID.randomUUID();
//         NguoiDung nd = new NguoiDung();
//         when(nguoiDungRepository.findById(userId)).thenReturn(Optional.of(nd));
//         when(vaiTroRepository.findByLoai("BUYER")).thenReturn(new VaiTro());
//         when(nguoiDungRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

//         NguoiDung result = nguoiDungService.datMatKhau(userId, "pass123456", "huong_user");
//         assertEquals("pass123456", result.getMatKhau());
//         assertEquals(ND_Trangthaixacthuc.CCCD_PASS, result.getTrangThaiXacThuc());
//     }

//     @Test
//     @DisplayName("TC_AUTH_006: Đặt mật khẩu cực ngắn - 1 ký tự (Boundary)")
//     void TC_AUTH_006() {
//         UUID userId = UUID.randomUUID();
//         when(nguoiDungRepository.findById(userId)).thenReturn(Optional.of(new NguoiDung()));

//         // Chạy và mong đợi lỗi Runtime (do bạn đã sửa Service)
//         assertThrows(RuntimeException.class, () -> {
//             nguoiDungService.datMatKhau(userId, "1", "u");
//         });
//         verify(nguoiDungRepository, never()).save(any());
//     }

//     @Test
//     @DisplayName("TC_AUTH_007: Đặt mật khẩu trống (Negative)")
//     void TC_AUTH_007() {
//         UUID userId = UUID.randomUUID();
//         when(nguoiDungRepository.findById(userId)).thenReturn(Optional.of(new NguoiDung()));

//         assertThrows(RuntimeException.class, () -> {
//             nguoiDungService.datMatKhau(userId, null, "");
//         });
//     }

//     @Test
//     @DisplayName("TC_AUTH_008: Đăng nhập thành công (R4)")
//     void TC_AUTH_008() {
//         String user = "huong_admin";
//         String pass = "secret123";
//         UUID mockId = UUID.randomUUID();
        
//         NguoiDung nd = new NguoiDung();
//         nd.setId(mockId);
//         nd.setTenDangNhap(user);
//         nd.setMatKhau(pass);

//         // Giả lập tìm thấy user trong DB
//         when(nguoiDungRepository.findByTenDangNhapAndMatKhau(user, pass))
//             .thenReturn(Optional.of(nd));

//         UUID result = nguoiDungService.loginAndGetUserId(user, pass);

//         assertNotNull(result);
//         assertEquals(mockId, result);
//     }

//     @Test
//     @DisplayName("TC_AUTH_009: Đăng nhập sai mật khẩu (R5)")
//     void TC_AUTH_009() {
//         String user = "huong_admin";
//         String wrongPass = "wrong_pass";
        
//         // Giả lập DB không tìm thấy cặp (user, wrongPass)
//         when(nguoiDungRepository.findByTenDangNhapAndMatKhau(user, wrongPass))
//             .thenReturn(Optional.empty());

//         UUID result = nguoiDungService.loginAndGetUserId(user, wrongPass);

//         assertNull(result, "Phải trả về null khi sai mật khẩu");
//     }

//     @Test
//     @DisplayName("TC_AUTH_010: Tài khoản không tồn tại (R5)")
//     void TC_AUTH_010() {
//         String nonExistUser = "ghost_user";
        
//         when(nguoiDungRepository.findByTenDangNhapAndMatKhau(anyString(), anyString()))
//             .thenReturn(Optional.empty());

//         UUID result = nguoiDungService.loginAndGetUserId(nonExistUser, "123456");

//         assertNull(result);
//     }

//     @Test
//     @DisplayName("TC_AUTH_011: Đăng nhập với chuỗi rỗng (Boundary)")
//     void TC_AUTH_011() {
//         // Test case kiểm tra xử lý dữ liệu trống
//         when(nguoiDungRepository.findByTenDangNhapAndMatKhau("", ""))
//             .thenReturn(Optional.empty());

//         UUID result = nguoiDungService.loginAndGetUserId("", "");

//         assertNull(result);
//     }

//     @Test
//     @DisplayName("TC_AUTH_012: Lấy tất cả tên đăng nhập")
//     void TC_AUTH_012() {
//         List<String> mockList = Arrays.asList("user1", "user2", "user3");
//         when(nguoiDungRepository.findAllTenDangNhap()).thenReturn(mockList);

//         List<String> result = nguoiDungService.getAllTenDangNhap();

//         assertEquals(3, result.size());
//         assertTrue(result.contains("user2"));
//     }

// }

