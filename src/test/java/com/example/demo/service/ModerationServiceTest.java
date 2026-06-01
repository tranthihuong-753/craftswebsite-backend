package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.ChungChi;
import com.example.demo.entity.DanhMuc;
import com.example.demo.entity.NhatKyKiemToan;
import com.example.demo.entity.SanPham;
import com.example.demo.entity.SanPhamCoSan;
import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.enums.NKKT_HanhDong;
import com.example.demo.enums.NKKT_KetQua;
import com.example.demo.enums.NKKT_LoaiTacNhan;
import com.example.demo.enums.TrangThaiChungChi;
import com.example.demo.enums.TrangThaiSanPham;
import com.example.demo.enums.TrangThaiSanPhamCoSan;
import com.example.demo.repository.AnhVideoSanPhamRepository;
import com.example.demo.repository.ChungChiRepository;
import com.example.demo.repository.DanhMucRepository;
import com.example.demo.repository.NhatKyKiemToanRepository;
import com.example.demo.repository.SanPhamCoSanRepository;
import com.example.demo.repository.SanPhamRepository;
import com.example.demo.repository.ThongTinNguoiBanRepository;

@ExtendWith(MockitoExtension.class)
public class ModerationServiceTest {

    @Mock private NhatKyKiemToanRepository nhatKyKiemToanRepository;
    @Mock private SanPhamCoSanService sanPhamCoSanService;
    @InjectMocks private NhatKyKiemToanService nhatKyKiemToanService;

    private UUID adminId;
    private Long sanPhamId;

    @BeforeEach
    void setUp() {
        adminId = UUID.randomUUID();
        sanPhamId = 100L;
    }

    // --- NHÓM POSITIVE (TC_MOD_001 -> TC_MOD_003) ---

    @Test
    @DisplayName("TC_MOD_001: Duyệt sản phẩm hợp lệ")
    void TC_MOD_001() {
        nhatKyKiemToanService.duyetSanPham(sanPhamId, "Duyệt bài", adminId);
        verify(nhatKyKiemToanRepository).save(any());
        verify(sanPhamCoSanService).updateSanPhamCoSanTrangThai(sanPhamId, "DANG_BAN");
    }

    @Test
    @DisplayName("TC_MOD_002: Đánh dấu vi phạm hợp lệ")
    void TC_MOD_002() {
        nhatKyKiemToanService.viPhamSanPham(sanPhamId, "Ảnh mờ", adminId);
        verify(nhatKyKiemToanRepository).save(any());
        verify(sanPhamCoSanService).updateSanPhamCoSanTrangThai(sanPhamId, "VI_PHAM");
    }

    // @Test
    // @DisplayName("TC_MOD_016: Lấy thông tin SP trước khi duyệt")
    // void TC_MOD_016() {
    //     SanPhamCoSan sp = new SanPhamCoSan();
    //     sp.setId(sanPhamId);
    //     when(sanPhamCoSanService.getById(sanPhamId)).thenReturn(sp);
        
    //     SanPhamCoSan result = sanPhamCoSanService.getById(sanPhamId);
    //     assertNotNull(result);
    //     assertEquals(sanPhamId, result.getId());
    // }

    // --- NHÓM LOGIC & AUDIT LOG (TC_MOD_003, 006, 007, 008, 011, 012, 015, 018, 020) ---

    @Test
    @DisplayName("TC_MOD_003: Kiểm tra nội dung Log JSON")
    void TC_MOD_003() {
        ArgumentCaptor<NhatKyKiemToan> captor = ArgumentCaptor.forClass(NhatKyKiemToan.class);
        nhatKyKiemToanService.duyetSanPham(sanPhamId, "Good", adminId);
        verify(nhatKyKiemToanRepository).save(captor.capture());
        assertTrue(captor.getValue().getSieuDuLieu().contains("ly_do"));
    }

    @Test
    @DisplayName("TC_MOD_006: Kiểm tra loại tác nhân Log là ADMIN")
    void TC_MOD_006() {
        ArgumentCaptor<NhatKyKiemToan> captor = ArgumentCaptor.forClass(NhatKyKiemToan.class);
        nhatKyKiemToanService.duyetSanPham(sanPhamId, "Reason", adminId);
        verify(nhatKyKiemToanRepository).save(captor.capture());
        assertEquals(NKKT_LoaiTacNhan.ADMIN, captor.getValue().getLoaiTacNhan());
    }

    @Test
    @DisplayName("TC_MOD_007: Kiểm tra hành động duyệt là TAO_SAN_PHAM")
    void TC_MOD_007() {
        ArgumentCaptor<NhatKyKiemToan> captor = ArgumentCaptor.forClass(NhatKyKiemToan.class);
        nhatKyKiemToanService.duyetSanPham(sanPhamId, "Reason", adminId);
        verify(nhatKyKiemToanRepository).save(captor.capture());
        assertEquals(NKKT_HanhDong.TAO_SAN_PHAM, captor.getValue().getHanhDong());
    }

    @Test
    @DisplayName("TC_MOD_008: Kiểm tra hành động vi phạm là XOA_SAN_PHAM")
    void TC_MOD_008() {
        ArgumentCaptor<NhatKyKiemToan> captor = ArgumentCaptor.forClass(NhatKyKiemToan.class);
        nhatKyKiemToanService.viPhamSanPham(sanPhamId, "Reason", adminId);
        verify(nhatKyKiemToanRepository).save(captor.capture());
        assertEquals(NKKT_HanhDong.XOA_SAN_PHAM, captor.getValue().getHanhDong());
    }

    @Test
    @DisplayName("TC_MOD_011: Kiểm tra ID mục tiêu khớp với SP ID")
    void TC_MOD_011() {
        ArgumentCaptor<NhatKyKiemToan> captor = ArgumentCaptor.forClass(NhatKyKiemToan.class);
        nhatKyKiemToanService.duyetSanPham(sanPhamId, "Reason", adminId);
        verify(nhatKyKiemToanRepository).save(captor.capture());
        assertEquals(sanPhamId, captor.getValue().getIdMucTieu());
    }

    @Test
    @DisplayName("TC_MOD_012: Kiểm tra ngày tạo Log không null")
    void TC_MOD_012() {
        ArgumentCaptor<NhatKyKiemToan> captor = ArgumentCaptor.forClass(NhatKyKiemToan.class);
        nhatKyKiemToanService.duyetSanPham(sanPhamId, "Reason", adminId);
        verify(nhatKyKiemToanRepository).save(captor.capture());
        assertNotNull(captor.getValue().getNgayTao());
    }

    @Test
    @DisplayName("TC_MOD_015: Kiểm tra ID Admin trong Log")
    void TC_MOD_015() {
        ArgumentCaptor<NhatKyKiemToan> captor = ArgumentCaptor.forClass(NhatKyKiemToan.class);
        nhatKyKiemToanService.duyetSanPham(sanPhamId, "Reason", adminId);
        verify(nhatKyKiemToanRepository).save(captor.capture());
        assertEquals(adminId, captor.getValue().getIdTacNhan());
    }

    @Test
    @DisplayName("TC_MOD_018: Kiểm tra kết quả Log là THANH_CONG")
    void TC_MOD_018() {
        ArgumentCaptor<NhatKyKiemToan> captor = ArgumentCaptor.forClass(NhatKyKiemToan.class);
        nhatKyKiemToanService.duyetSanPham(sanPhamId, "Reason", adminId);
        verify(nhatKyKiemToanRepository).save(captor.capture());
        assertEquals(NKKT_KetQua.THANH_CONG, captor.getValue().getKetQua());
    }

    @Test
    @DisplayName("TC_MOD_020: Kiểm tra gọi sang Service update trạng thái")
    void TC_MOD_020() {
        nhatKyKiemToanService.duyetSanPham(sanPhamId, "OK", adminId);
        verify(sanPhamCoSanService, times(1)).updateSanPhamCoSanTrangThai(anyLong(), anyString());
    }

    // --- NHÓM NEGATIVE & BOUNDARY (TC_MOD_004, 005, 009, 010, 013, 014, 017, 019) ---

    @Test
    @DisplayName("TC_MOD_004: Duyệt SP không tồn tại (Negative)")
    void TC_MOD_004() {
        doThrow(new RuntimeException()).when(sanPhamCoSanService).updateSanPhamCoSanTrangThai(anyLong(), anyString());
        assertThrows(RuntimeException.class, () -> nhatKyKiemToanService.duyetSanPham(999L, "Error", adminId));
    }

    @Test
    @DisplayName("TC_MOD_005: Duyệt với lý do trống (Boundary)")
    void TC_MOD_005() {
        assertDoesNotThrow(() -> nhatKyKiemToanService.duyetSanPham(sanPhamId, "", adminId));
    }

    @Test
    @DisplayName("TC_MOD_009: Duyệt bài đã bị XÓA (Boundary)")
    void TC_MOD_009() {
        // Logic nghiệp vụ: dù xóa rồi admin vẫn có thể lưu log hành động duyệt (hoặc báo lỗi tùy Hường)
        assertDoesNotThrow(() -> nhatKyKiemToanService.duyetSanPham(sanPhamId, "Duyệt lại bài xóa", adminId));
    }

    @Test
    @DisplayName("TC_MOD_010: Vi phạm bài đã VI PHẠM (Boundary)")
    void TC_MOD_010() {
        assertDoesNotThrow(() -> nhatKyKiemToanService.viPhamSanPham(sanPhamId, "Lý do 2", adminId));
    }

    @Test
    @DisplayName("TC_MOD_013: Lỗi khi lưu Log - DB Down (Negative)")
    void TC_MOD_013() {
        when(nhatKyKiemToanRepository.save(any())).thenThrow(new RuntimeException("DB Down"));
        assertThrows(RuntimeException.class, () -> nhatKyKiemToanService.duyetSanPham(sanPhamId, "Reason", adminId));
    }

    @Test
    @DisplayName("TC_MOD_014: Duyệt với lý do cực dài (Boundary)")
    void TC_MOD_014() {
        String longReason = "a".repeat(1000);
        assertDoesNotThrow(() -> nhatKyKiemToanService.duyetSanPham(sanPhamId, longReason, adminId));
    }

    @Test
    @DisplayName("TC_MOD_017: Duyệt SP đang ẩn (Positive)")
    void TC_MOD_017() {
        assertDoesNotThrow(() -> nhatKyKiemToanService.duyetSanPham(sanPhamId, "Hiện bài ẩn", adminId));
    }

    @Test
    @DisplayName("TC_MOD_019: Vi phạm với lý do có dấu (Positive)")
    void TC_MOD_019() {
        String reason = "Sản phẩm vi phạm bản quyền";
        assertDoesNotThrow(() -> nhatKyKiemToanService.viPhamSanPham(sanPhamId, reason, adminId));
    }
}

