package com.example.demo.repository;

import com.example.demo.entity.ThoYeuThich;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ThoYeuThichRepository extends JpaRepository<ThoYeuThich, Long> {


}