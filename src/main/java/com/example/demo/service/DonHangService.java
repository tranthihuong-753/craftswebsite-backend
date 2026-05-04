package com.example.demo.service;

import com.example.demo.entity.DonHang;
import java.util.List;

public interface DonHangService {
    List<DonHang> getAll();
    DonHang getById(Long id);
    DonHang save(DonHang donHang);
    void delete(Long id);
}