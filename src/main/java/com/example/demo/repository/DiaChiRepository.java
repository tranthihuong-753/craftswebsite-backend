package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.DiaChi;

@Repository
public interface DiaChiRepository extends JpaRepository<DiaChi, Long> {

    @Query("""
        SELECT d FROM DiaChi d 
        WHERE d.vaiTroNguoiDung.id = :userId 
        AND d.status = 1 
        AND d.thietLapMacDinh = 1
    """)
    DiaChi findDefaultByUser(UUID userId);
    
    // Hàm lấy địa chỉ mặc định dựa trên VTND_Id (UUID)
    @Query("SELECT d FROM DiaChi d WHERE d.vaiTroNguoiDung.id = :vtndId AND d.thietLapMacDinh = 1 AND d.status = 1")
    Optional<DiaChi> findDefaultByVaiTroNguoiDungId(@Param("vtndId") UUID vtndId);

    /**
     * Giải thích tên hàm:
     * findFirstBy: Lấy bản ghi đầu tiên tìm thấy
     * VaiTroNguoiDung_NguoiDung_Id: Đi xuyên qua thực thể VaiTroNguoiDung vào NguoiDung để lấy Id (UUID)
     * AndThietLapMacDinh: Và trường thietLapMacDinh phải bằng giá trị truyền vào (1)
     */
    Optional<DiaChi> findFirstByVaiTroNguoiDung_NguoiDung_IdAndThietLapMacDinh(UUID userId, Integer defaultStatus);

    // Bạn cũng nên có thêm hàm này để lấy địa chỉ đang hoạt động (Status = 1)
    Optional<DiaChi> findFirstByVaiTroNguoiDung_NguoiDung_IdAndThietLapMacDinhAndStatus(UUID userId, Integer defaultStatus, Integer activeStatus);

    List<DiaChi> findByVaiTroNguoiDung_Id(UUID vtndId);

    Optional<DiaChi> findByVaiTroNguoiDung_IdAndThietLapMacDinh(
            UUID vtndId,
            Integer macDinh
    );
}
