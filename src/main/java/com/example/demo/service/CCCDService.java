package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.model.ND_CCCD;

@Service
public class CCCDService {

    public ND_CCCD scanCCCD(String imageUrl) {
 
        String url = "http://localhost:8000/cccd/scan";

        Map<String, String> body = new HashMap<>();
        body.put("image_url", imageUrl);

        RestTemplate restTemplate = new RestTemplate();

        ND_CCCD cccd = restTemplate.postForObject(
                url,
                body,
                ND_CCCD.class
        );

        return cccd;
    }
}