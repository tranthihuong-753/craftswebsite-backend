package com.example.demo.repository;

import com.example.demo.entity.TaiKhoanNganHang;
import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.enums.TrangThaiTaiKhoanNganHang;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaiKhoanNganHangRepository extends JpaRepository<TaiKhoanNganHang, Long> {
    ///
    Optional<TaiKhoanNganHang> findFirstByTtnbIdAndTrangThai(
        ThongTinNguoiBan ttnbId,
        TrangThaiTaiKhoanNganHang trangThai
    );

    /**
     * Giải thích tên hàm:
     * findFirstBy: Lấy bản ghi đầu tiên
     * TtnbId_Id: Đi vào thực thể ThongTinNguoiBan (tên biến là ttnbId) lấy Id (UUID)
     * AndTrangThai: Và trạng thái phải là CON_SU_DUNG
     */
    Optional<TaiKhoanNganHang> findFirstByTtnbId_IdAndTrangThai(UUID sellerInfoId, TrangThaiTaiKhoanNganHang trangThai);
}