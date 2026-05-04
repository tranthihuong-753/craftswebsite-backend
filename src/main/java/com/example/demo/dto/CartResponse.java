package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class CartResponse {
    private List<ShopDTO> shops;
}