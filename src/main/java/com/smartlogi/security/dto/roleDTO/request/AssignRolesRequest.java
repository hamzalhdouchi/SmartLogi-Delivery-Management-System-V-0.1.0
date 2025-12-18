package com.smartlogi.security.dto.roleDTO.request;

import lombok.Data;

import java.util.Set;

@Data
public class AssignRolesRequest {
    private Set<String> roleIds;
}
