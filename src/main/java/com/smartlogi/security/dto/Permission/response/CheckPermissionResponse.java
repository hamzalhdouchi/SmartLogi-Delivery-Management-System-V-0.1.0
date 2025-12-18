package com.smartlogi.security.dto.Permission.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckPermissionResponse {
    private boolean hasPermission;
}
