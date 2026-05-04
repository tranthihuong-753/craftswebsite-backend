package com.example.demo.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.entity.NguoiDung;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    // public String generateToken(NguoiDung user) {
    //     return Jwts.builder()
    //             .setSubject(user.getId().toString())
    //             .claim("roles", "BUYER") // có thể sửa sau
    //             .setIssuedAt(new Date())
    //             .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 ngày
    //             .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
    //             .compact();
    // }
    public String generateToken(NguoiDung user, List<String> roles) {

        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("roles", roles) // 👈 LIST
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(key)
                .compact();
    }

    public String extractUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}