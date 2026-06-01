// package com.example.demo.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.math.BigDecimal;
// import java.util.Collections;
// import java.util.List;
// import java.util.Optional;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.ArgumentCaptor;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.example.demo.dto.SanPhamCoSanRequest;
// import com.example.demo.entity.AnhVideoSanPham;
// import com.example.demo.entity.DanhMuc;
// import com.example.demo.entity.SanPham;
// import com.example.demo.entity.SanPhamCoSan;
// import com.example.demo.enums.TrangThaiSanPham;
// import com.example.demo.enums.TrangThaiSanPhamCoSan;
// import com.example.demo.repository.AnhVideoSanPhamRepository;
// import com.example.demo.repository.DanhMucRepository;
// import com.example.demo.repository.SanPhamCoSanRepository;

// @ExtendWith(MockitoExtension.class)
// public class SanPhamCoSanUpdateServiceTest {

//     @Mock private SanPhamCoSanRepository sanPhamCoSanRepository;
//     @Mock private DanhMucRepository danhMucRepository;
//     @Mock private AnhVideoSanPhamRepository anhVideoSanPhamRepository;
//     @InjectMocks private SanPhamCoSanService sanPhamCoSanService;

//     private SanPhamCoSan existingSpcs;
//     private SanPham existingSp;
//     private SanPhamCoSanRequest request;

//     @BeforeEach
//     void setUp() {
//         existingSp = new SanPham();
//         existingSp.setId(100L);
        
//         existingSpcs = new SanPhamCoSan();
//         existingSpcs.setId(1L);
//         existingSpcs.setSanPham(existingSp);

//         request = new SanPhamCoSanRequest();
//         request.setMoTa("Mô tả mới");
//         request.setGia(BigDecimal.valueOf(200));
//         request.setTrangThaiSPCS(TrangThaiSanPhamCoSan.DANG_BAN);
//     }

//     @Test
//     @DisplayName("TC_CART_026: Update thông tin cơ bản thành công")
//     void TC_CART_026() {
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(existingSpcs));
//         when(sanPhamCoSanRepository.save(any())).thenAnswer(i -> i.getArgument(0));

//         SanPhamCoSan result = sanPhamCoSanService.updateSanPhamCoSan(1L, request);

//         assertEquals("Mô tả mới", result.getMoTa());
//         assertEquals(BigDecimal.valueOf(200), result.getGia());
//         verify(sanPhamCoSanRepository).save(any());
//     }

//     @Test
//     @DisplayName("TC_CART_028: Update Media (Xóa cũ, thêm mới)")
//     void TC_CART_028() {
//         request.setMediaLinks(List.of("link1.jpg", "link2.png"));
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(existingSpcs));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(existingSpcs);

//         sanPhamCoSanService.updateSanPhamCoSan(1L, request);

//         // Kiểm tra xem có gọi lệnh xóa media cũ không
//         verify(anhVideoSanPhamRepository).deleteBySanPhamId(100L);
//         // Kiểm tra xem có gọi lệnh lưu 2 media mới không
//         verify(anhVideoSanPhamRepository, times(2)).save(any());
//     }

//     @Test
//     @DisplayName("TC_CART_030: Update Danh mục không tồn tại (Negative)")
//     void TC_CART_030() {
//         request.setDanhMucId(999L);
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(existingSpcs));
//         when(danhMucRepository.findById(999L)).thenReturn(Optional.empty());

//         assertThrows(RuntimeException.class, () -> {
//             sanPhamCoSanService.updateSanPhamCoSan(1L, request);
//         });
//     }

//     @Test
//     @DisplayName("TC_CART_031: Không thay đổi Media khi mediaLinks là null")
//     void TC_CART_031() {
//         request.setMediaLinks(null);
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(existingSpcs));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(existingSpcs);

//         sanPhamCoSanService.updateSanPhamCoSan(1L, request);

//         // Đảm bảo không gọi lệnh xóa hay lưu media
//         verify(anhVideoSanPhamRepository, never()).deleteBySanPhamId(any());
//         verify(anhVideoSanPhamRepository, never()).save(any());
//     }

//     @Test
//     @DisplayName("TC_CART_033: Xóa sạch Media khi truyền list rỗng")
//     void TC_CART_033() {
//         request.setMediaLinks(Collections.emptyList());
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(existingSpcs));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(existingSpcs);

//         sanPhamCoSanService.updateSanPhamCoSan(1L, request);

//         // Phải gọi delete nhưng KHÔNG được gọi save
//         verify(anhVideoSanPhamRepository).deleteBySanPhamId(100L);
//         verify(anhVideoSanPhamRepository, never()).save(any());
//     }

//     @Test
//     @DisplayName("TC_CART_034: Kiểm tra thứ tự (order) của Media mới")
//     void TC_CART_034() {
//         request.setMediaLinks(List.of("img1", "img2"));
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(existingSpcs));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(existingSpcs);

//         sanPhamCoSanService.updateSanPhamCoSan(1L, request);

//         // Sử dụng ArgumentCaptor để bắt giá trị "thuTu" khi lưu
//         ArgumentCaptor<AnhVideoSanPham> captor = ArgumentCaptor.forClass(AnhVideoSanPham.class);
//         verify(anhVideoSanPhamRepository, times(2)).save(captor.capture());
        
//         List<AnhVideoSanPham> savedMedia = captor.getAllValues();
//         assertEquals(1L, savedMedia.get(0).getThuTu());
//         assertEquals(2L, savedMedia.get(1).getThuTu());
//     }

//     @Test
//     @DisplayName("TC_CART_035: Cập nhật thông tin Sản phẩm gốc")
//     void TC_CART_035() {
//         request.setSoGioLamViecUocTinh(50L);
//         request.setTrangThaiSanPham(TrangThaiSanPham.DANG_BAN);
        
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(existingSpcs));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(existingSpcs);

//         sanPhamCoSanService.updateSanPhamCoSan(1L, request);

//         assertEquals(50L, existingSp.getSoGioLamViecUocTinh());
//         assertEquals(TrangThaiSanPham.DANG_BAN, existingSp.getTrangThai());
//     }
    
//     // Bổ sung thêm 3 case nữa cho đủ 10
//     @Test
//     @DisplayName("TC_CART_029: ID sản phẩm không tồn tại")
//     void TC_CART_029() {
//         when(sanPhamCoSanRepository.findById(any())).thenReturn(Optional.empty());
//         assertThrows(RuntimeException.class, () -> sanPhamCoSanService.updateSanPhamCoSan(99L, request));
//     }

//     @Test
//     @DisplayName("TC_CART_027: Update Danh mục mới thành công")
//     void TC_CART_027() {
//         request.setDanhMucId(2L);
//         DanhMuc newDm = new DanhMuc();
//         newDm.setId(2L);
        
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(existingSpcs));
//         when(danhMucRepository.findById(2L)).thenReturn(Optional.of(newDm));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(existingSpcs);

//         sanPhamCoSanService.updateSanPhamCoSan(1L, request);
//         assertEquals(2L, existingSp.getDanhMuc().getId());
//     }

//     @Test
//     @DisplayName("TC_CART_032: Update trạng thái SPCS")
//     void TC_CART_032() {
//         request.setTrangThaiSPCS(TrangThaiSanPhamCoSan.LUU_AN);
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(existingSpcs));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(existingSpcs);

//         SanPhamCoSan result = sanPhamCoSanService.updateSanPhamCoSan(1L, request);
//         assertEquals(TrangThaiSanPhamCoSan.LUU_AN, result.getTrangThai());
//     }
// }
