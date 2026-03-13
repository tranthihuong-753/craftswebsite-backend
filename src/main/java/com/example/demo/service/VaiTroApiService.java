package com.example.demo.service;

import com.example.demo.entity.VaiTroApi;
import com.example.demo.repository.VaiTroApiRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VaiTroApiService {

    @Autowired
    private VaiTroApiRepository repository;

    // CREATE
    public VaiTroApi create(VaiTroApi data) {
        return repository.save(data);
    }

    // READ ALL
    public List<VaiTroApi> getAll() {
        return repository.findAll();
    }

    // READ BY ID
    public VaiTroApi getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public VaiTroApi update(Long id, VaiTroApi newData) {

        VaiTroApi old = repository.findById(id).orElse(null);

        if (old != null) {

            old.setVaiTro(newData.getVaiTro());
            old.setApi(newData.getApi());
            old.setChoPhep(newData.getChoPhep());

            return repository.save(old);
        }

        return null;
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
}