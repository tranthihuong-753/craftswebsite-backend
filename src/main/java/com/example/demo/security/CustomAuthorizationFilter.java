package com.example.demo.security;

import com.example.demo.config.CacheConfig;
import com.example.demo.entity.VaiTroApi;
import com.example.demo.dto.ApiResponse;
import com.example.demo.repository.VaiTroApiRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final CacheManager cacheManager;
    private final VaiTroApiRepository repository;
    private final JwtService jwtService;
    private final AntPathMatcher matcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. CHẶN ĐỨNG LỖI CORS TẠI ĐÂY
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setStatus(HttpServletResponse.SC_OK);
            return; // Kết thúc luôn, không check token cho request OPTIONS
        }

        String path = request.getRequestURI();
        String method = request.getMethod();

        // 2. MỞ CỬA CHO API LOGIN & REGISTER (QUAN TRỌNG!)
        if (path.equals("/nguoidung/login") || path.equals("/nguoidung/register")) {
            log.info("Cho phép truy cập không cần token: {}", path);
            filterChain.doFilter(request, response);
            return; // Kết thúc xử lý Filter tại đây, cho request vào Controller
        }

        // 3. CÁC API KHÁC THÌ MỚI ĐÒI TOKEN
        String token = resolveToken(request);
        
        if (token == null) {
            log.warn("Chặn request vì thiếu token: {}", path);
            sendErrorResponse(response, "Bạn cần đăng nhập", 401);
            return;
        }

        // 1. XỬ LÝ CORS PRE-FLIGHT (QUAN TRỌNG NHẤT)
        // Trình duyệt gửi OPTIONS để check xem có được gửi Authorization không. 
        // Phải cho qua ngay, nếu không nó sẽ báo lỗi "không gửi token".
        if ("OPTIONS".equalsIgnoreCase(method)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 2. LOẠI TRỪ CÁC API CÔNG KHAI
        // Đừng check quyền các API như login, register để tránh lỗi vòng lặp
        if (path.contains("/auth/login") || path.contains("/auth/register")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 4. LẤY ROLES TỪ JWT
            List<String> roles = jwtService.extractRoles(token);
            Cache cache = cacheManager.getCache(CacheConfig.PERMISSION_CACHE);

            // 5. CHECK MATRIX QUYỀN
            boolean allowed = roles.stream().anyMatch(role -> {
                List<VaiTroApi> permissions = getPermissions(role, cache);
                return permissions.stream().anyMatch(p ->
                        Boolean.TRUE.equals(p.getChoPhep())
                                && p.getApi().getHoatDong()
                                && matcher.match(p.getApi().getDuongDan(), path)
                                && p.getApi().getPhuongThuc().name().equalsIgnoreCase(method)
                );
            });

            if (!allowed) {
                log.error("🚫 Truy cập bị từ chối: Role {} không có quyền gọi [{}] {}", roles, method, path);
                sendErrorResponse(response, "Bạn không có quyền truy cập chức năng này", 403);
                return;
            }

            // Mọi thứ OK -> Cho đi tiếp
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            log.error("🔥 Lỗi giải mã Token hoặc Check quyền: {}", e.getMessage());
            sendErrorResponse(response, "Phiên đăng nhập hết hạn hoặc không hợp lệ", 401);
        }
    }

    private List<VaiTroApi> getPermissions(String role, Cache cache) {
        String key = "ROLE_" + role;
        return Optional.ofNullable(cache.get(key, List.class))
                .orElseGet(() -> {
                    List<VaiTroApi> data = repository.findByVaiTro_Loai(role);
                    cache.put(key, data);
                    return data;
                });
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // HÀM TRẢ VỀ JSON LỖI CHUẨN (Vì Exception ở đây @ControllerAdvice không bắt được)
    private void sendErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        ApiResponse<Object> apiResponse = new ApiResponse<>(message, null, LocalDateTime.now());
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Quan trọng để xử lý LocalDateTime
        
        String json = mapper.writeValueAsString(apiResponse);
        response.getWriter().write(json);
    }
}