package com.example.demo.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AnhVideo;
import com.example.demo.entity.NguoiDung;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, UUID> {

    Optional<NguoiDung> findBySdt(String sdt);

    @Query("SELECT n.tenDangNhap FROM NguoiDung n WHERE n.tenDangNhap IS NOT NULL")
    List<String> findAllTenDangNhap();

    // Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);

    Optional<NguoiDung> findByTenDangNhapAndMatKhau(String tenDangNhap, String matKhau);

    // tu id lay anhchandung 
    @Query("SELECT n.anhVideo_anhChanDung FROM NguoiDung n WHERE n.id = :id")
    Optional<AnhVideo> findAnhChanDungById(UUID id);

    // tu id lay ten nguoi dung
    @Query("SELECT n.ten FROM NguoiDung n WHERE n.id = :id")
    Optional<String> findTenById(UUID id);

    // findFirstByNguoiDungId
    @Query("SELECT n FROM NguoiDung n WHERE n.id = :ndId")
    Optional<NguoiDung> findFirstByNguoiDungId(UUID ndId);

} 
