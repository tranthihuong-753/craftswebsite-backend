package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.ChungChi;
import com.example.demo.entity.DanhMuc;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.enums.TrangThaiChungChi;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;
import com.example.demo.repository.AnhVideoSanPhamRepository;
import com.example.demo.repository.ChungChiRepository;
import com.example.demo.repository.DanhMucRepository;
import com.example.demo.repository.SanPhamCoSanRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.repository.ThongTinNguoiBanRepository;

@ExtendWith(MockitoExtension.class)
public class SanPhamCoSanCreateTest {

    @Mock private ThongTinNguoiBanRepository thongTinNguoiBanRepository;
    @Mock private DanhMucRepository danhMucRepository;
    @Mock private ChungChiRepository chungChiRepository;
    @Mock private SanPhamRepository sanPhamRepository;
    @Mock private AnhVideoSanPhamRepository anhVideoSanPhamRepository;
    @Mock private SanPhamCoSanRepository sanPhamCoSanRepository;

    @InjectMocks private SanPhamCoSanService sanPhamCoSanService;

    private UUID sellerId;
    private Long danhMucId;

    @BeforeEach
    void setUp() {
        sellerId = UUID.randomUUID();
        danhMucId = 1L;
    }

    @Test
    @DisplayName("TC_CART_021: Tạo sản phẩm hợp lệ với đầy đủ liên kết")
    void TC_CART_021() {
        when(thongTinNguoiBanRepository.findById(sellerId)).thenReturn(Optional.of(new ThongTinNguoiBan()));
        when(danhMucRepository.findById(danhMucId)).thenReturn(Optional.of(new DanhMuc()));
        when(chungChiRepository.save(any())).thenReturn(new ChungChi());
        when(sanPhamRepository.save(any())).thenAnswer(i -> {
            SanPham sp = i.getArgument(0);
            sp.setId(100L); return sp;
        });
        when(sanPhamCoSanRepository.save(any())).thenReturn(new SanPhamCoSan());

        SanPhamCoSan result = sanPhamCoSanService.createSanPhamCoSan(
                "Mô tả", BigDecimal.valueOf(100), 1.0, 10.0, 10.0, 10.0, 
                BigDecimal.valueOf(80), 10L, 10L, TrangThaiSanPhamCoSan.LUU_HIEN, 
                sellerId, danhMucId, TrangThaiSanPham.LUU_AN, 5L, 
                TrangThaiChungChi.LUU_AN, List.of("url1", "url2")
        );

        assertNotNull(result);
        verify(sanPhamCoSanRepository).save(any());
        verify(anhVideoSanPhamRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("TC_CART_022: Tạo sản phẩm với Seller không tồn tại (Negative)")
    void TC_CART_022() {
        when(thongTinNguoiBanRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            sanPhamCoSanService.createSanPhamCoSan(
                "Mô tả", BigDecimal.valueOf(100), 1.0, 10.0, 10.0, 10.0, 
                BigDecimal.valueOf(80), 10L, 10L, TrangThaiSanPhamCoSan.LUU_HIEN, 
                sellerId, danhMucId, TrangThaiSanPham.LUU_AN, 5L, 
                TrangThaiChungChi.LUU_AN, null
            );
        });
    }

    @Test
    @DisplayName("TC_CART_023: Tạo sản phẩm không có ảnh/video (mediaLinks null)")
    void TC_CART_023() {
        when(thongTinNguoiBanRepository.findById(sellerId)).thenReturn(Optional.of(new ThongTinNguoiBan()));
        when(danhMucRepository.findById(danhMucId)).thenReturn(Optional.of(new DanhMuc()));
        when(chungChiRepository.save(any())).thenReturn(new ChungChi());
        when(sanPhamRepository.save(any())).thenReturn(new SanPham());
        when(sanPhamCoSanRepository.save(any())).thenReturn(new SanPhamCoSan());

        sanPhamCoSanService.createSanPhamCoSan(
            "Mô tả", BigDecimal.valueOf(100), 1.0, 10.0, 10.0, 10.0, 
            BigDecimal.valueOf(80), 10L, 10L, TrangThaiSanPhamCoSan.LUU_HIEN, 
            sellerId, danhMucId, TrangThaiSanPham.LUU_AN, 5L, 
            TrangThaiChungChi.LUU_AN, null
        );

        verify(anhVideoSanPhamRepository, never()).save(any());
    }

    @Test
    @DisplayName("TC_CART_024: Tạo sản phẩm với Danh mục không tồn tại (Negative)")
    void TC_CART_024() {
        when(thongTinNguoiBanRepository.findById(sellerId)).thenReturn(Optional.of(new ThongTinNguoiBan()));
        // Giả lập không tìm thấy danh mục
        when(danhMucRepository.findById(danhMucId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            sanPhamCoSanService.createSanPhamCoSan(
                "Mô tả", BigDecimal.valueOf(100), 1.0, 10.0, 10.0, 10.0, 
                BigDecimal.valueOf(80), 10L, 10L, TrangThaiSanPhamCoSan.LUU_HIEN, 
                sellerId, danhMucId, TrangThaiSanPham.LUU_AN, 5L, 
                TrangThaiChungChi.LUU_AN, null
            );
        });
    }

    @Test
    @DisplayName("TC_CART_025: Kiểm tra logic ID Mục tiêu cho Chứng chỉ")
    void TC_CART_025() {
        when(thongTinNguoiBanRepository.findById(sellerId)).thenReturn(Optional.of(new ThongTinNguoiBan()));
        when(danhMucRepository.findById(danhMucId)).thenReturn(Optional.of(new DanhMuc()));
        
        ChungChi mockCc = new ChungChi();
        when(chungChiRepository.save(any(ChungChi.class))).thenReturn(mockCc);
        
        SanPham mockSp = new SanPham();
        mockSp.setId(555L); // ID giả lập của Sản phẩm
        when(sanPhamRepository.save(any(SanPham.class))).thenReturn(mockSp);
        when(sanPhamCoSanRepository.save(any())).thenReturn(new SanPhamCoSan());

        sanPhamCoSanService.createSanPhamCoSan(
                "Mô tả", BigDecimal.valueOf(100), 1.0, 10.0, 10.0, 10.0, 
                BigDecimal.valueOf(80), 10L, 10L, TrangThaiSanPhamCoSan.LUU_HIEN, 
                sellerId, danhMucId, TrangThaiSanPham.LUU_AN, 5L, 
                TrangThaiChungChi.LUU_AN, null
        );

        // Kiểm tra xem setIdMucTieu có được gọi với đúng ID của SanPham (555L) không
        assertEquals(555L, mockCc.getIdMucTieu());
        verify(chungChiRepository, times(2)).save(any()); // Lưu lần 1 khi tạo, lần 2 khi update ID mục tiêu
    }
}

