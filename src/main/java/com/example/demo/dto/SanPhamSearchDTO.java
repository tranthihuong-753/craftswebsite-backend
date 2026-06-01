package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SanPhamSearchDTO {

    private Long id;

    private String seller;          // tên người bán
    private String sellerAvatar;    // avatar

    private String content;         // mô tả

    private List<MediaDTO> media;   // list ảnh/video

    private BigDecimal price;       // giá
    private Long stock;             // tồn kho

    private Float rating;           // từ chứng chỉ

    private Long totalRating;
    
    private String certificate;     // link chứng chỉ
    
    
}
