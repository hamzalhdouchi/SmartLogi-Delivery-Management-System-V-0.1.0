package com.smartlogi.security.dto.roleDTO.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckRoleResponse {
    private boolean hasRole;
}