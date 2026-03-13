package com.example.demo.service;

import com.example.demo.entity.AnhVideo;
import com.example.demo.repository.AnhVideoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnhVideoService {

    @Autowired
    private AnhVideoRepository repository;

    // CREATE
    public AnhVideo create(AnhVideo data) {
        return repository.save(data);
    }

    // READ ALL
    public List<AnhVideo> getAll() {
        return repository.findAll();
    }

    // READ BY ID
    public AnhVideo getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    // UPDATE
    public AnhVideo update(Long id, AnhVideo newData) {

        AnhVideo old = repository.findById(id).orElse(null);

        if (old != null) {
            old.setLink(newData.getLink());
            return repository.save(old);
        }

        return null;
    }

    // DELETE
    public void delete(Long id) {
        repository.deleteById(id);
    }
}