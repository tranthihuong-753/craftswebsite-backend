package com.example.demo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.security.JwtService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@Component
public class JwtFilter extends OncePerRequestFilter { 

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Always allow CORS preflight
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String path = request.getRequestURI();

        // Public endpoints (no JWT required)
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeUnauthorized(response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            UUID userId = jwtService.extractUserId(token);
            request.setAttribute("userId", userId.toString());
        } catch (Exception e) {
            writeUnauthorized(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        // Auth / onboarding
        if (path.equals("/nguoidung/login")) return true;
        if (path.equals("/nguoidung/create/sdt")) return true;

        // Public catalog/search (user-facing)
        if (path.equals("/danh-muc")) return true;
        if (path.startsWith("/danh-muc/")) return true;
        if (path.equals("/san-pham-co-san/moderation-products-user")) return true;

        // Swagger / OpenAPI (if enabled)
        if (path.startsWith("/swagger-ui")) return true;
        if (path.startsWith("/v3/api-docs")) return true;

        return false;
    }

    private void writeUnauthorized(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.write("{\"success\":false,\"message\":\"Unauthorized\"}");
            out.flush();
        }
    }
}