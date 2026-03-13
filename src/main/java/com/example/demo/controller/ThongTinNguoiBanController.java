package com.example.demo.controller;

import com.example.demo.entity.ThongTinNguoiBan;
import com.example.demo.service.ThongTinNguoiBanService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/thong-tin-nguoi-ban")
public class ThongTinNguoiBanController {

    @Autowired
    private ThongTinNguoiBanService service;

    @PostMapping
    public ThongTinNguoiBan createThongTinNguoiBan(@RequestBody ThongTinNguoiBan data) {
        return service.create(data);
    }

    @GetMapping
    public List<ThongTinNguoiBan> getAllThongTinNguoiBan() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ThongTinNguoiBan getThongTinNguoiBanById(@PathVariable UUID id) {
        return service.getById(id);
    }

    @PutMapping("/{id}")
    public ThongTinNguoiBan updateThongTinNguoiBan(
            @PathVariable UUID id,
            @RequestBody ThongTinNguoiBan data
    ) {
        return service.update(id, data);
    }

    @DeleteMapping("/{id}")
    public void deleteThongTinNguoiBan(@PathVariable UUID id) {
        service.delete(id);
    }

    // tu nguoimua check xem da co tai khoan nguoi ban chua 
    @GetMapping("/check/{ndId}")
    public boolean checkNguoiBanByNguoiDungId(@PathVariable UUID ndId)
    {
        return service.checkNguoiBanByNguoiDungId(ndId);
    }
}