package com.example.demo.service;

import com.example.demo.config.CacheConfig;
import com.example.demo.dto.PermissionMatrixDTO;
import com.example.demo.entity.Api;
import com.example.demo.entity.VaiTro;
import com.example.demo.entity.VaiTroApi;
import com.example.demo.repository.VaiTroApiRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VaiTroApiService {

    @Autowired
    private VaiTroApiRepository repository;
    @Autowired
    private CacheManager cacheManager;
 
    // UPDATE PERMISSION {Cấp / thu hồi quyền API cho role + refresh cache}
    public void updatePermission(Long roleId, Long apiId, boolean allow) {
        VaiTro vt = new VaiTro();
        vt.setId(roleId);

        Api api = new Api();
        api.setId(apiId);

        VaiTroApi entity = repository.findByVaiTroAndApi(vt, api)
                .orElseGet(() -> {
                    VaiTroApi newEntity = new VaiTroApi();
                    newEntity.setVaiTro(vt);
                    newEntity.setApi(api);
                    return newEntity;
                });

        if (allow) {
            entity.setChoPhep(true);
            repository.save(entity);
        } else {
            // deny = remove or disable
            repository.delete(entity);
        }

        // 🔥 CRITICAL: invalidate cache ngay lập tức
        Optional.ofNullable(cacheManager.getCache(CacheConfig.PERMISSION_CACHE))
                .ifPresent(cache -> cache.clear());
    }

    // MATRIX PERMISSION {Trả về bảng phân quyền dạng matrix (Role × API)}
    public PermissionMatrixDTO getPermissionMatrix() {
        try{
        List<VaiTroApi> all = repository.findAll();

        Map<Api, List<VaiTroApi>> grouped = all.stream()
                .collect(Collectors.groupingBy(VaiTroApi::getApi));

        PermissionMatrixDTO dto = new PermissionMatrixDTO();

        List<PermissionMatrixDTO.ApiInfo> apiInfos = grouped.entrySet().stream()
                .map(entry -> {

                    Api api = entry.getKey();

                    PermissionMatrixDTO.ApiInfo info = new PermissionMatrixDTO.ApiInfo();
                    info.setApiId(api.getId());
                    info.setPath(api.getDuongDan());
                    info.setMethod(api.getPhuongThuc().name());
                    info.setActive(Boolean.TRUE.equals(api.getHoatDong()));
                    info.setName(api.getTen());

                    List<PermissionMatrixDTO.RolePermission> roles = entry.getValue().stream()
                            .map(v -> {
                                PermissionMatrixDTO.RolePermission rp = new PermissionMatrixDTO.RolePermission();
                                rp.setRoleId(v.getVaiTro().getId());
                                rp.setRoleName(v.getVaiTro().getLoai());
                                rp.setAllow(Boolean.TRUE.equals(v.getChoPhep()));
                                return rp;
                            }).toList();

                    info.setRoles(roles);

                    return info;
                })
                .toList();

        dto.setApis(apiInfos);

        return dto;
    }     catch (Exception e) {
        e.printStackTrace(); // 🔥 BẮT BUỘC
        throw e;
    }}

}