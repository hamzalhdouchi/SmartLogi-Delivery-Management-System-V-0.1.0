package com.smartlogi.security.dto.roleDTO.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignRoleRequest {
    @NotBlank(message = "Role ID is required")
    private String roleId;
}