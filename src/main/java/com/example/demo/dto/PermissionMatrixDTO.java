package com.example.demo.dto;

import lombok.Data;
import java.util.List;

@Data
public class PermissionMatrixDTO {

    private List<ApiInfo> apis;

    @Data
    public static class ApiInfo {
        private Long apiId; // 
        private String path; // 
        private String method; // 
        private boolean active;
        private String name; //

        private List<RolePermission> roles; // 
    }

    @Data
    public static class RolePermission {
        private Long roleId;
        private String roleName;
        private boolean allow;
    }
}
