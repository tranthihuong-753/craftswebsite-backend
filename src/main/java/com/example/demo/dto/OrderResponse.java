package com.example.demo.dto;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.AllArgsConstructor; // Thêm dòng này
import lombok.NoArgsConstructor;  // Thêm dòng này

@Data
@AllArgsConstructor // Tạo Constructor cho tất cả các trường (Dùng để fix lỗi undefined)
@NoArgsConstructor  // Tạo Constructor không tham số (Bắt buộc cho JSON/Hibernate)
public class OrderResponse {
    private Long orderId;
    private String status;
    private LocalDateTime completedAt;
    private String message;
}