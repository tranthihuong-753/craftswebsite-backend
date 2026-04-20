package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tblAnhVideo")
@Data
public class AnhVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AV_Id")
    private Long id;

    @Column(name = "AV_Link")
    private String link;

    @Column(name = "AV_type")
    private String type; // image | video
}