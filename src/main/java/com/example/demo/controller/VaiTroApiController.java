package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.PermissionMatrixDTO;
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

    // UPDATE PERMISSION
    @PostMapping("/update")
    public ApiResponse<Void> update(
            @RequestParam Long roleId,
            @RequestParam Long apiId,
            @RequestParam boolean allow
    ) {
        service.updatePermission(roleId, apiId, allow);
        return new ApiResponse<>("OK", null, null);
    }
 
    // MATRIX
    @GetMapping("/matrix")
    public ApiResponse<PermissionMatrixDTO> matrix() {
        return new ApiResponse<>("OK", service.getPermissionMatrix(), null);
    }

}
