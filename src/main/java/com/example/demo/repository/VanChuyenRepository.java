package com.example.demo.repository;

import com.example.demo.entity.VanChuyen;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VanChuyenRepository extends JpaRepository<VanChuyen, Long> {
}