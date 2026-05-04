package com.example.demo.service;

import com.example.demo.entity.DonHang;
import com.example.demo.repository.DonHangRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonHangServiceImpl implements DonHangService {

    private final DonHangRepository repository;

    public DonHangServiceImpl(DonHangRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<DonHang> getAll() {
        return repository.findAll();
    }

    @Override
    public DonHang getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public DonHang save(DonHang donHang) {
        return repository.save(donHang);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}