package com.smartlogi.smartlogiv010.security.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CreateRoleRequest {
    private String name;
    private Set<String> permissionIds;
}
