package com.smartlogi.security.dto.roleDTO.response;

import com.smartlogi.security.dto.Permission.response.PermissionResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
    private String id;
    private String name;
    private String description;
    private Set<PermissionResponse> permissions;

}
