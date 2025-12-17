package com.smartlogi.smartlogiv010.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AssignRoleRequest {
    @NotBlank(message = "Role ID is required")
    private String roleId;
}