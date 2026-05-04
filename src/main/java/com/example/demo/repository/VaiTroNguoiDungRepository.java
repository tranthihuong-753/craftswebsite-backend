package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.VaiTroNguoiDung;

@Repository
public interface VaiTroNguoiDungRepository extends JpaRepository<VaiTroNguoiDung, UUID> {

    List<VaiTroNguoiDung> findByNguoiDung_Id(UUID nguoiDungId);

    boolean existsByNguoiDung_IdAndVaiTro_Id(UUID userId, Long id);
    
    @Query("""
        SELECT v
        FROM VaiTroNguoiDung v
        WHERE v.nguoiDung.id = :idNguoiDung
        AND v.vaiTro.loai = 'SELLER'
        AND v.trangThai = 'HOAT_DONG'
    """)
    Optional<VaiTroNguoiDung> findSellerByNguoiDungId(UUID idNguoiDung);

    @Query("""
        SELECT v
        FROM VaiTroNguoiDung v
        WHERE v.nguoiDung.id = :idNguoiDung
        AND v.vaiTro.loai = 'BUYER'
        AND v.trangThai = 'HOAT_DONG'
    """)
    Optional<VaiTroNguoiDung> findUserByNguoiDungId(UUID idNguoiDung);

} 
