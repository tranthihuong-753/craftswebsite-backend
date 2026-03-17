package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.VaiTroNguoiDung;

@Repository
public interface VaiTroNguoiDungRepository extends JpaRepository<VaiTroNguoiDung, UUID> {

    List<VaiTroNguoiDung> findByNguoiDung_Id(UUID nguoiDungId);
    
} 
