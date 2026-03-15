package com.example.demo.controller;

import com.example.demo.entity.ChungChi;
import com.example.demo.service.ChungChiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chung-chi")
@RequiredArgsConstructor
public class ChungChiController {

    private final ChungChiService service;

    @GetMapping
    public List<ChungChi> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ChungChi getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public ChungChi create(@RequestBody ChungChi chungChi) {
        return service.create(chungChi);
    }

    @PutMapping("/{id}")
    public ChungChi update(
            @PathVariable Long id,
            @RequestBody ChungChi chungChi
    ) {
        return service.update(id, chungChi);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

}