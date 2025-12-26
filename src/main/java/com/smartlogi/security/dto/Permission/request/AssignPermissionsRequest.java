package com.smartlogi.security.dto.Permission.request;

import lombok.Data;

import java.util.Set;

@Data
public class AssignPermissionsRequest {
    private Set<String> permissionIds;
}

