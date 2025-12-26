package com.smartlogi.security.controller;

import com.smartlogi.security.dto.Permission.request.CreatePermissionRequest;
import com.smartlogi.security.dto.Permission.response.PermissionResponse;
import com.smartlogi.security.dto.Permission.request.UpdatePermissionRequest;
import com.smartlogi.smartlogiv010.apiResponse.ApiResponse;
import com.smartlogi.smartlogiv010.entity.Permission;
import com.smartlogi.security.service.RoleManagementService;
import com.smartlogi.security.userMapper.PermissionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class PermissionController {

    private final RoleManagementService roleManagementService;
    private final PermissionMapper permissionMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CAN_MANAGE_PERMISSIONS')")
    public ResponseEntity<ApiResponse<PermissionResponse>> createPermission(
            @Valid @RequestBody CreatePermissionRequest request) {
        Permission permission = roleManagementService.createPermission(request.getName());
        ApiResponse<PermissionResponse> response = ApiResponse.<PermissionResponse>builder()
                .success(true)
                .message("Permission créée avec succès")
                .data(permissionMapper.toResponse(permission))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CAN_MANAGE_PERMISSIONS')")
    public ResponseEntity<ApiResponse<List<PermissionResponse>>> getAllPermissions() {
        List<Permission> permissions = roleManagementService.getAllPermissions();
        ApiResponse<List<PermissionResponse>> response = ApiResponse.<List<PermissionResponse>>builder()
                .success(true)
                .message("Liste des permissions récupérée avec succès")
                .data(permissionMapper.toResponseList(permissions))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_PERMISSIONS')")
    public ResponseEntity<ApiResponse<PermissionResponse>> getPermissionById(@PathVariable String id) {
        Permission permission = roleManagementService.getPermissionById(id);
        ApiResponse<PermissionResponse> response = ApiResponse.<PermissionResponse>builder()
                .success(true)
                .message("Permission récupérée avec succès")
                .data(permissionMapper.toResponse(permission))
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_PERMISSIONS')")
    public ResponseEntity<ApiResponse<PermissionResponse>> updatePermission(
            @PathVariable String id,
            @Valid @RequestBody UpdatePermissionRequest request) {
        Permission permission = roleManagementService.updatePermission(
                id,
                request.getName(),
                request.getDescription()
        );
        ApiResponse<PermissionResponse> response = ApiResponse.<PermissionResponse>builder()
                .success(true)
                .message("Permission mise à jour avec succès")
                .data(permissionMapper.toResponse(permission))
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_PERMISSIONS')")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable String id) {
        roleManagementService.deletePermission(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Permission supprimée avec succès")
                .build();

        return ResponseEntity.ok(response);
    }
}
