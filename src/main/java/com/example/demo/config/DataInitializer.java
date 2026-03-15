package com.example.demo.config;

import com.example.demo.entity.VaiTro;
import com.example.demo.repository.VaiTroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final VaiTroRepository vaiTroRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> {

            String[] roles = {
                    "BUYER",
                    "SELLER",
                    "SHIPPER",
                    "COMPLIANCE_ADMIN",
                    "SUPPORT_ADMIN",
                    "SUPER_ADMIN"
            };

            for (String role : roles) {
                // check xem ton tai chua roi moi tao 
                if (vaiTroRepository.existsByLoai(role)) {
                    continue;
                }
                VaiTro vt = new VaiTro();
                vt.setLoai(role);
                vaiTroRepository.save(vt);
            }
        };
    }
}