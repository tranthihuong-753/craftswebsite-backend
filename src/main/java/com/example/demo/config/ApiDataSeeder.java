package com.example.demo.config;

import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.example.demo.annotation.ApiDescription;
import com.example.demo.entity.Api;
import com.example.demo.entity.VaiTro;
import com.example.demo.entity.VaiTroApi;
import com.example.demo.enums.PhuongThucHttp;
import com.example.demo.repository.ApiRepository;
import com.example.demo.repository.VaiTroApiRepository;
import com.example.demo.repository.VaiTroRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiDataSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private final RequestMappingHandlerMapping handlerMapping;
    private final ApiRepository apiRepository;
    private final VaiTroRepository vaiTroRepository;
    private final VaiTroApiRepository vaiTroApiRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        // 1. Lấy tất cả các Role hiện có trong hệ thống
        List<VaiTro> allRoles = vaiTroRepository.findAll();
        if (allRoles.isEmpty()) {
            log.warn("⚠️ Danh sách vai trò trống, không thể gán quyền mặc định.");
            return;
        }

        handlerMapping.getHandlerMethods().forEach((info, method) -> {
            // Chỉ quét Controller
            if (!(method.getBeanType().isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class)
                    || method.getBeanType().isAnnotationPresent(org.springframework.stereotype.Controller.class))) {
                return;
            }

            String urlPattern = info.getPatternValues().stream().findFirst().orElse("");
            if (urlPattern.isEmpty()) return;

            // Đọc mô tả từ Annotation
            ApiDescription desc = method.getMethodAnnotation(ApiDescription.class);
            String fallbackName = "Chưa đặt tên (" + method.getMethod().getName() + ")"; 
            String apiName = Optional.ofNullable(desc).map(ApiDescription::value).orElse(fallbackName);

            info.getMethodsCondition().getMethods().forEach(httpMethod -> {
                PhuongThucHttp phuongThuc = mapHttp(httpMethod);

                // Kiểm tra API đã tồn tại chưa
                Optional<Api> existingApi = apiRepository.findByDuongDanAndPhuongThuc(urlPattern, phuongThuc);
                Api api;

                if (existingApi.isPresent()) {
                    api = existingApi.get();
                    if (!api.getTen().equals(apiName)) {
                        api.setTen(apiName);
                        apiRepository.save(api);
                    }
                } else {
                    // Tạo mới API
                    api = new Api();
                    api.setTen(apiName);
                    api.setDuongDan(urlPattern);
                    api.setPhuongThuc(phuongThuc);
                    api.setHoatDong(true);
                    apiRepository.save(api);
                    log.info("✨ Đăng ký API: [{}] {}", phuongThuc, urlPattern);
                }

                // ===== LOGIC GÁN QUYỀN CHO TẤT CẢ ROLES =====
                for (VaiTro role : allRoles) {
                    // Kiểm tra xem Role này đã có bản ghi quyền cho API này chưa
                    if (!vaiTroApiRepository.existsByVaiTroAndApi(role, api)) {
                        VaiTroApi vta = new VaiTroApi();
                        vta.setApi(api);
                        vta.setVaiTro(role);
                        
                        // MẶC ĐỊNH: Chỉ SUPER_ADMIN là true, các role khác là false
                        if ("SUPER_ADMIN".equals(role.getLoai())) {
                            vta.setChoPhep(true);
                            log.info("🔓 Auto Grant [SUPER_ADMIN] -> {}", urlPattern);
                        } else {
                            vta.setChoPhep(false); 
                            log.info("🔒 Auto Deny [{}] -> {}", role.getLoai(), urlPattern);
                        }
                        
                        vaiTroApiRepository.save(vta);
                    }
                }
            });
        });
    }

    private PhuongThucHttp mapHttp(RequestMethod method) {
        try {
            return PhuongThucHttp.valueOf(method.name());
        } catch (Exception e) {
            return PhuongThucHttp.GET;
        }
    }
}

// @ApiDescription("Danh sách đơn hàng")
