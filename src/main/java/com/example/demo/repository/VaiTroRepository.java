package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.VaiTro;

@Repository
public interface VaiTroRepository extends JpaRepository<VaiTro, Long> {

} 
