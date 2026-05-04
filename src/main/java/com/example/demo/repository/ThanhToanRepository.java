package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.ThanhToan;

public interface ThanhToanRepository extends JpaRepository<ThanhToan, Long> {
}