package com.example.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ThongTinNguoiBan;

public interface ThongTinNguoiBanRepository extends JpaRepository<ThongTinNguoiBan, UUID> {

    // tu nguoimua check xem da co tai khoan nguoi ban chua
    boolean existsByNguoiDung_Id(UUID ndId);
}