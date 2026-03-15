package com.example.demo.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ThongTinNguoiBan;

public interface ThongTinNguoiBanRepository extends JpaRepository<ThongTinNguoiBan, UUID> {

    // tu nguoimua check xem da co tai khoan nguoi ban chua
    boolean existsByNguoiDungId(UUID ndId);

    Optional<ThongTinNguoiBan> findByNguoiDungId(UUID userId);

}