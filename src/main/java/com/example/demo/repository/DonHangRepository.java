package com.example.demo.repository;

import com.example.demo.entity.DonHang;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DonHangRepository extends JpaRepository<DonHang, Long> {
    List<DonHang> findAllByIdIn(List<Long> ids);
}