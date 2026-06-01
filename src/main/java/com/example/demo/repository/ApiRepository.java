package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Api;
import com.example.demo.enums.PhuongThucHttp;

public interface ApiRepository extends JpaRepository<Api, Long> {

    boolean existsByDuongDanAndPhuongThuc(String duongDan, PhuongThucHttp phuongThuc);

    Optional<Api> findByDuongDanAndPhuongThuc(String duongDan, PhuongThucHttp phuongThuc);
}