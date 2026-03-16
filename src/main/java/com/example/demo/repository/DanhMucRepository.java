package com.example.demo.repository;

import com.example.demo.entity.DanhMuc;
import com.example.demo.enums.TrangThaiDanhMuc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DanhMucRepository extends JpaRepository<DanhMuc, Long> {

    boolean existsByTen(String ten);

    List<DanhMuc> findByTrangThai(TrangThaiDanhMuc trangThai);

}