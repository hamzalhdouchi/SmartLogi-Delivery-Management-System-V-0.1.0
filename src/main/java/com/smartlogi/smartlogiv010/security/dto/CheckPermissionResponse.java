package com.smartlogi.smartlogiv010.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckPermissionResponse {
    private boolean hasPermission;
}
