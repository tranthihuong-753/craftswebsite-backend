package com.example.demo.controller;

import com.example.demo.entity.AnhVideo;
import com.example.demo.service.AnhVideoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/anh-video")
public class AnhVideoController {

    @Autowired
    private AnhVideoService service;

    // CREATE
    @PostMapping
    public AnhVideo create(@RequestBody AnhVideo data) {
        return service.create(data);
    }

    // READ ALL
    @GetMapping
    public List<AnhVideo> getAll() {
        return service.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public AnhVideo getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public AnhVideo update(@PathVariable Long id, @RequestBody AnhVideo data) {
        return service.update(id, data);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}