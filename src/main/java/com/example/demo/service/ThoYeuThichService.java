package com.example.demo.service;

import com.example.demo.entity.ThoYeuThich;
import com.example.demo.repository.ThoYeuThichRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ThoYeuThichService {

    @Autowired
    private ThoYeuThichRepository repository;

    public ThoYeuThich add(ThoYeuThich tyt) {
        return repository.save(tyt);
    }
}