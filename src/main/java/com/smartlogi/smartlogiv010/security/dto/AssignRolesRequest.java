package com.smartlogi.smartlogiv010.security.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AssignRolesRequest {
    private Set<String> roleIds;
}
