package com.example.demo.controller;

import com.example.demo.entity.Api;
import com.example.demo.service.ApiService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-manager")
public class ApiController {

    @Autowired
    private ApiService apiService;

    // CREATE
    @PostMapping
    public Api create(@RequestBody Api api) {
        return apiService.create(api);
    }

    // READ ALL
    @GetMapping
    public List<Api> getAll() {
        return apiService.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Api getById(@PathVariable Long id) {
        return apiService.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Api update(@PathVariable Long id, @RequestBody Api api) {
        return apiService.update(id, api);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        apiService.delete(id);
    }

    // ACTIVATE
    @PutMapping("/{id}/activate")
    public Api activate(@PathVariable Long id) {
        return apiService.activate(id);
    }

    // DEACTIVATE
    @PutMapping("/{id}/deactivate")
    public Api deactivate(@PathVariable Long id) {
        return apiService.deactivate(id);
    }
}