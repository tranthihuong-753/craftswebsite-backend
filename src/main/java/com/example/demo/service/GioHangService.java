package com.example.demo.service;

import com.example.demo.entity.GioHang;
import com.example.demo.repository.GioHangRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class GioHangService {

    @Autowired
    private GioHangRepository repository;

    public GioHang add(GioHang gh) {
        gh.setNgayTao(LocalDateTime.now());
        gh.setNgayCapNhat(LocalDateTime.now());
        return repository.save(gh);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

}