package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Api;

public interface ApiRepository extends JpaRepository<Api, Long> {

}