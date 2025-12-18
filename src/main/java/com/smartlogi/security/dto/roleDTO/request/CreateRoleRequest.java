package com.smartlogi.security.dto.roleDTO.request;

import lombok.Data;

import java.util.Set;

@Data
public class CreateRoleRequest {
    private String name;
    private Set<String> permissionIds;
}
