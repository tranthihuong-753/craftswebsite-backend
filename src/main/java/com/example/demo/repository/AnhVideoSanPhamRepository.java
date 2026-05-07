package com.example.demo.repository;

import com.example.demo.entity.AnhVideo;
import com.example.demo.entity.AnhVideoSanPham;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnhVideoSanPhamRepository extends JpaRepository<AnhVideoSanPham, Long> {

    Optional<AnhVideoSanPham> findFirstBySanPhamIdOrderByThuTuAsc(Long sanPhamId);

    @Modifying
    @Query("DELETE FROM AnhVideoSanPham a WHERE a.sanPham.id = :sanPhamId")
    void deleteBySanPhamId(Long sanPhamId);

    List<AnhVideoSanPham> findBySanPhamIdAndType(Long sanPhamId, String type);

    Optional<AnhVideoSanPham> findFirstBySanPhamIdAndType(Long sanPhamId, String type);

    // Laays anh dau tien theo sanPhamId
    Optional<AnhVideoSanPham> findFirstBySanPhamIdAndTypeOrderByThuTuAsc(Long sanPhamId, String type);
}