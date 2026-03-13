package com.example.demo.controller;

import com.example.demo.entity.VaiTroApi;
import com.example.demo.service.VaiTroApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vai-tro-api")
public class VaiTroApiController {

    @Autowired
    private VaiTroApiService service;

    // CREATE
    @PostMapping
    public VaiTroApi create(@RequestBody VaiTroApi data) {
        return service.create(data);
    }

    // READ ALL
    @GetMapping
    public List<VaiTroApi> getAll() {
        return service.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public VaiTroApi getById(@PathVariable Long id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public VaiTroApi update(@PathVariable Long id, @RequestBody VaiTroApi data) {
        return service.update(id, data);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}