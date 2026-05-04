package com.example.demo.controller;

import com.example.demo.entity.ThoYeuThich;
import com.example.demo.service.ThoYeuThichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tho-yeu-thich")
public class ThoYeuThichController {

    @Autowired
    private ThoYeuThichService service;

    @PostMapping
    public ThoYeuThich add(@RequestBody ThoYeuThich tyt) {
        return service.add(tyt);
    }
}