package com.example.demo.service;

import io.jsonwebtoken.security.Keys;
import java.util.Base64;

public class GenSecret {
    public static void main(String[] args) {
        String secret = Base64.getEncoder().encodeToString(
            Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256).getEncoded()
        );
        // System.out.println(secret);
    }
}