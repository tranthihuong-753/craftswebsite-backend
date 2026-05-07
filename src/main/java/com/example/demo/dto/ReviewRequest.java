package com.example.demo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {
    private int rating;      // Số sao (1-5)
    private String comment;  // Lời nhắn gửi dễ thương
}