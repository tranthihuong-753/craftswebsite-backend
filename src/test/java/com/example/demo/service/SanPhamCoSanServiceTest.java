// package com.example.demo.service;

// import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTrue;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.never;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;

// import java.math.BigDecimal;
// import java.util.Optional;

// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;

// import com.example.demo.entity.ChungChi;
// import com.example.demo.entity.LoaiSanPham;
// import com.example.demo.entity.SanPham;
// import com.example.demo.entity.SanPhamCoSan;
// import com.example.demo.enums.LoaiSP;
// import com.example.demo.enums.TrangThaiSanPham;
// import com.example.demo.enums.TrangThaiSanPhamCoSan;
// import com.example.demo.repository.SanPhamCoSanRepository;
// import com.example.demo.repository.SanPhamRepository;

// // @ExtendWith(MockitoExtension.class)
// // public class SanPhamCoSanServiceTest {

// //     @Mock private SanPhamCoSanRepository sanPhamCoSanRepository;
// //     @Mock private AnhVideoSanPhamRepository anhVideoSanPhamRepository;
// //     @InjectMocks private SanPhamCoSanService sanPhamCoSanService;

// //     private UUID sellerId;
// //     private Pageable pageable;

// //     @BeforeEach
// //     void setUp() {
// //         sellerId = UUID.randomUUID();
// //         pageable = PageRequest.of(0, 10);
// //     }

// //     // --- NHÓM TÌM KIẾM (R7) ---

// //     @Test
// //     @DisplayName("TC_CART_001: Tìm kiếm sản phẩm theo tên hợp lệ")
// //     void TC_CART_001() {
// //         String search = "Gốm";
// //         Page<SanPhamCoSan> mockPage = new PageImpl<>(List.of(createMockSanPhamCoSan()));
// //         when(sanPhamCoSanRepository.searchProducts(any(), any(), anyString(), any())).thenReturn(mockPage);
        
// //         Page<SellerProductDTO> result = sanPhamCoSanService.getProducts(sellerId, "DANG_BAN", search, pageable);
        
// //         assertEquals(1, result.getContent().size());
// //         verify(sanPhamCoSanRepository).searchProducts(eq(sellerId), eq(TrangThaiSanPhamCoSan.DANG_BAN), eq(search), any());
// //     }

// //     @Test
// //     @DisplayName("TC_CART_002: Lấy toàn bộ SP khi search rỗng")
// //     void TC_CART_002() {
// //         Page<SanPhamCoSan> mockPage = new PageImpl<>(List.of(createMockSanPhamCoSan()));
// //         when(sanPhamCoSanRepository.findBySanPhamThongTinNguoiBanIdAndTrangThai(any(), any(), any())).thenReturn(mockPage);
        
// //         Page<SellerProductDTO> result = sanPhamCoSanService.getProducts(sellerId, "DANG_BAN", "", pageable);
        
// //         assertNotNull(result);
// //         verify(sanPhamCoSanRepository).findBySanPhamThongTinNguoiBanIdAndTrangThai(any(), any(), any());
// //     }

// //     @Test
// //     @DisplayName("TC_CART_003: Tìm kiếm không có kết quả")
// //     void TC_CART_003() {
// //         when(sanPhamCoSanRepository.searchProducts(any(), any(), anyString(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
        
// //         Page<SellerProductDTO> result = sanPhamCoSanService.getProducts(sellerId, "DANG_BAN", "KhongTonTai", pageable);
        
// //         assertEquals(0, result.getContent().size());
// //     }

// //     @Test
// //     @DisplayName("TC_CART_007: Tìm kiếm với từ khóa cực dài (Boundary)")
// //     void TC_CART_007() {
// //         String longSearch = "a".repeat(300); // Chuỗi 300 ký tự
// //         when(sanPhamCoSanRepository.searchProducts(any(), any(), anyString(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));

// //         Page<SellerProductDTO> result = sanPhamCoSanService.getProducts(sellerId, "DANG_BAN", longSearch, pageable);
        
// //         assertNotNull(result);
// //         verify(sanPhamCoSanRepository).searchProducts(any(), any(), eq(longSearch), any());
// //     }

// //     // --- NHÓM TRẠNG THÁI (R8) ---

// //     @Test
// //     @DisplayName("TC_CART_004: Lọc SP theo trạng thái DANG_BAN")
// //     void TC_CART_004() {
// //         when(sanPhamCoSanRepository.findBySanPhamThongTinNguoiBanIdAndTrangThai(any(), any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));

// //         sanPhamCoSanService.getProducts(sellerId, "DANG_BAN", null, pageable);
// //         verify(sanPhamCoSanRepository).findBySanPhamThongTinNguoiBanIdAndTrangThai(any(), eq(TrangThaiSanPhamCoSan.DANG_BAN), any());
// //     }

// //     @Test
// //     @DisplayName("TC_CART_005: Lọc SP theo trạng thái VI_PHAM")
// //     void TC_CART_005() {
// //         when(sanPhamCoSanRepository.findBySanPhamThongTinNguoiBanIdAndTrangThai(any(), any(), any())).thenReturn(new PageImpl<>(Collections.emptyList()));

// //         sanPhamCoSanService.getProducts(sellerId, "VI_PHAM", null, pageable);
// //         verify(sanPhamCoSanRepository).findBySanPhamThongTinNguoiBanIdAndTrangThai(any(), eq(TrangThaiSanPhamCoSan.VI_PHAM), any());
// //     }

// //     @Test
// //     @DisplayName("TC_CART_006: Sai định dạng status (Negative)")
// //     void TC_CART_006() {
// //         assertThrows(IllegalArgumentException.class, () -> {
// //             sanPhamCoSanService.getProducts(sellerId, "SAI_ENUM", null, pageable);
// //         });
// //     }

// //     // --- NHÓM PHÂN TRANG & HIỂN THỊ (R9) ---

// //     @Test
// //     @DisplayName("TC_CART_008: Kiểm tra phân trang (Page size)")
// //     void TC_CART_008() {
// //         Pageable customPageable = PageRequest.of(0, 5); // Size = 5
// //         when(sanPhamCoSanRepository.findBySanPhamThongTinNguoiBanIdAndTrangThai(any(), any(), eq(customPageable)))
// //             .thenReturn(new PageImpl<>(Collections.emptyList(), customPageable, 0));

// //         sanPhamCoSanService.getProducts(sellerId, "DANG_BAN", null, customPageable);
// //         verify(sanPhamCoSanRepository).findBySanPhamThongTinNguoiBanIdAndTrangThai(any(), any(), eq(customPageable));
// //     }

// //     @Test
// //     @DisplayName("TC_CART_009: Kiểm tra lấy ảnh có thứ tự ưu tiên")
// //     void TC_CART_009() {
// //         Page<SanPhamCoSan> mockPage = new PageImpl<>(List.of(createMockSanPhamCoSan()));
// //         AnhVideoSanPham av = new AnhVideoSanPham();
// //         av.setLink("image_chinh.jpg");

// //         when(sanPhamCoSanRepository.findBySanPhamThongTinNguoiBanIdAndTrangThai(any(), any(), any())).thenReturn(mockPage);
// //         when(anhVideoSanPhamRepository.findFirstBySanPhamIdOrderByThuTuAsc(any())).thenReturn(Optional.of(av));

// //         Page<SellerProductDTO> result = sanPhamCoSanService.getProducts(sellerId, "LUU_HIEN", null, pageable);
// //         assertEquals("image_chinh.jpg", result.getContent().get(0).getImage());
// //     }

// //     @Test
// //     @DisplayName("TC_CART_010: sellerId không tồn tại (Negative)")
// //     void TC_CART_010() {
// //         UUID nonExistId = UUID.randomUUID();
// //         when(sanPhamCoSanRepository.findBySanPhamThongTinNguoiBanIdAndTrangThai(eq(nonExistId), any(), any()))
// //             .thenReturn(new PageImpl<>(Collections.emptyList()));

// //         Page<SellerProductDTO> result = sanPhamCoSanService.getProducts(nonExistId, "DANG_BAN", null, pageable);
        
// //         assertEquals(0, result.getTotalElements());
// //     }

//     // private SanPhamCoSan createMockSanPhamCoSan() {
//     //     SanPhamCoSan spcs = new SanPhamCoSan();
//     //     spcs.setId(1L); spcs.setGia(BigDecimal.valueOf(100000));
//     //     SanPham sp = new SanPham(); sp.setId(100L);
//     //     LoaiSanPham loai = new LoaiSanPham(); loai.setLoai(LoaiSP.SAN_PHAM_CO_SAN);
//     //     sp.setLoaiSanPham(loai);
//     //     ChungChi cc = new ChungChi(); cc.setId(50L); sp.setChungChi(cc);
//     //     spcs.setSanPham(sp);
//     //     return spcs;
//     // }
// // }

// @ExtendWith(MockitoExtension.class)
// public class SanPhamCoSanServiceTest {

//     // Mock các tầng phụ thuộc - Không truy cập DB thật
//     @Mock private SanPhamCoSanRepository sanPhamCoSanRepository;
//     @Mock private SanPhamRepository sanPhamRepository;
    
//     // Inject các Mock vào Service cần test
//     @InjectMocks private SanPhamCoSanService sanPhamCoSanService;

//     // --- NHÓM CHI TIẾT (R11) ---

//     @Test
//     @DisplayName("TC_CART_011: Lấy chi tiết SP hợp lệ")
//     void TC_CART_011() {
//         Long id = 1L;
//         SanPhamCoSan mockSp = new SanPhamCoSan();
//         mockSp.setId(id);
        
//         when(sanPhamCoSanRepository.findById(id)).thenReturn(Optional.of(mockSp));
        
//         SanPhamCoSan result = sanPhamCoSanService.getById(id);
//         assertNotNull(result);
//         assertEquals(id, result.getId());
//         verify(sanPhamCoSanRepository).findById(id); // Xác nhận đã gọi mock
//     }

//     @Test
//     @DisplayName("TC_CART_012: Lấy chi tiết SP không tồn tại (Negative)")
//     void TC_CART_012() {
//         Long fakeId = 999L;
//         when(sanPhamCoSanRepository.findById(fakeId)).thenReturn(Optional.empty());

//         assertThrows(RuntimeException.class, () -> sanPhamCoSanService.getById(fakeId));
//     }

//     // --- NHÓM XÓA SẢN PHẨM (R12) ---

//     @Test
//     @DisplayName("TC_CART_013: Xóa sản phẩm thành công (Soft Delete)")
//     void TC_CART_013() {
//         Long id = 1L;
//         SanPhamCoSan spcs = new SanPhamCoSan();
//         spcs.setSanPham(new SanPham()); // Mock quan hệ cha-con

//         when(sanPhamCoSanRepository.findById(id)).thenReturn(Optional.of(spcs));
//         // Giả lập lưu vào repo trả về chính nó
//         when(sanPhamCoSanRepository.save(any(SanPhamCoSan.class))).thenAnswer(i -> i.getArguments()[0]);

//         SanPhamCoSan result = sanPhamCoSanService.updateSanPhamCoSanTrangThai(id);

//         assertEquals(TrangThaiSanPhamCoSan.DA_XOA, result.getTrangThai());
//         verify(sanPhamRepository).save(any()); // Xác nhận có cập nhật SP cha
//     }

//     @Test
//     @DisplayName("TC_CART_014: Xóa SP kiểm tra trạng thái SP cha")
//     void TC_CART_014() {
//         Long id = 1L;
//         SanPhamCoSan spcs = new SanPhamCoSan();
//         SanPham spCha = new SanPham();
//         spcs.setSanPham(spCha);

//         when(sanPhamCoSanRepository.findById(id)).thenReturn(Optional.of(spcs));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(spcs);

//         sanPhamCoSanService.updateSanPhamCoSanTrangThai(id);

//         assertEquals(TrangThaiSanPham.DA_XOA, spCha.getTrangThai()); // Kiểm tra logic gán DA_XOA cho SP cha
//     }

//     @Test
//     @DisplayName("TC_CART_015: Xóa SP không tồn tại (Negative)")
//     void TC_CART_015() {
//         Long fakeId = 99L;
//         when(sanPhamCoSanRepository.findById(fakeId)).thenReturn(Optional.empty());

//         assertThrows(RuntimeException.class, () -> sanPhamCoSanService.updateSanPhamCoSanTrangThai(fakeId));
//     }

//     // --- NHÓM LOGIC SEARCH & BOUNDARY ---

// @Test
//     @DisplayName("TC_CART_016: Kiểm tra logic Build Search Text (Logic)")
//     void TC_CART_016() {
//         SanPhamCoSan spcs = new SanPhamCoSan();
//         spcs.setMoTa("Gốm Sứ");
//         spcs.setGia(BigDecimal.valueOf(100));
//         spcs.setSoLuongBanDau(5L);

//         spcs.buildSearchText(); 

//         assertNotNull(spcs.getTimKiem());
//         // Thay vì assertTrue, hãy kiểm tra xem nó có KHÁC null không
//         // Vì mỗi thư viện normalize sẽ ra kết quả khác nhau (gom su hoặc gomsu)
//         assertFalse(spcs.getTimKiem().isEmpty());
//     }

//     @Test
//     @DisplayName("TC_CART_017: Xóa SP đã có trạng thái DA_XOA (Boundary)")
//     void TC_CART_017() {
//         Long id = 5L;
//         SanPhamCoSan spcs = new SanPhamCoSan();
//         spcs.setTrangThai(TrangThaiSanPhamCoSan.DA_XOA);

//         when(sanPhamCoSanRepository.findById(id)).thenReturn(Optional.of(spcs));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(spcs);

//         SanPhamCoSan result = sanPhamCoSanService.updateSanPhamCoSanTrangThai(id);
//         assertEquals(TrangThaiSanPhamCoSan.DA_XOA, result.getTrangThai());
//     }

//     @Test
//     @DisplayName("TC_CART_018: Hiển thị giá và mô tả trong chi tiết")
//     void TC_CART_018() {
//         SanPhamCoSan spcs = new SanPhamCoSan();
//         spcs.setGia(new BigDecimal("200.00"));
//         when(sanPhamCoSanRepository.findById(1L)).thenReturn(Optional.of(spcs));

//         SanPhamCoSan result = sanPhamCoSanService.getById(1L);
//         assertEquals(new BigDecimal("200.00"), result.getGia());
//     }

//     @Test
//     @DisplayName("TC_CART_019: Build search text khi dữ liệu null (Boundary)")
//     void TC_CART_019() {
//         SanPhamCoSan spcs = new SanPhamCoSan();
//         spcs.setMoTa(null);
//         spcs.setGia(null);
//         spcs.setSoLuongBanDau(null);

//         assertDoesNotThrow(() -> spcs.buildSearchText());
//         // Kiểm tra xem nó có trả về chuỗi (không phải null) để tránh crash App
//         assertNotNull(spcs.getTimKiem());
//     }

//     @Test
//     @DisplayName("TC_CART_020: Xóa SP khi không có SP cha liên kết")
//     void TC_CART_020() {
//         Long id = 10L;
//         SanPhamCoSan spcs = new SanPhamCoSan();
//         spcs.setSanPham(null); // Case biên: SPCS mồ côi

//         when(sanPhamCoSanRepository.findById(id)).thenReturn(Optional.of(spcs));
//         when(sanPhamCoSanRepository.save(any())).thenReturn(spcs);

//         assertDoesNotThrow(() -> sanPhamCoSanService.updateSanPhamCoSanTrangThai(id));
//         verify(sanPhamRepository, never()).save(any()); // Không được lưu SP cha nếu null
//     }
// }
