package com.smartlogi.smartlogiv010.security.controller;

import com.smartlogi.smartlogiv010.entity.Permission;
import com.smartlogi.smartlogiv010.security.dto.*;
import com.smartlogi.smartlogiv010.security.service.RoleManagementService;
import com.smartlogi.smartlogiv010.security.userMapper.PermissionMapper;
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
@PreAuthorize("hasRole('ADMIN')")
public class PermissionController {

    private final RoleManagementService roleManagementService;
    private final PermissionMapper permissionMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_PERMISSION')")
    public ResponseEntity<PermissionResponse> createPermission(
            @Valid @RequestBody CreatePermissionRequest request) {
        Permission permission = roleManagementService.createPermission(
                request.getName(),
                request.getDescription()
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(permissionMapper.toResponse(permission));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_PERMISSIONS')")
    public ResponseEntity<List<PermissionResponse>> getAllPermissions() {
        List<Permission> permissions = roleManagementService.getAllPermissions();
        return ResponseEntity.ok(permissionMapper.toResponseList(permissions));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_PERMISSIONS')")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable String id) {
        Permission permission = roleManagementService.getPermissionById(id);
        return ResponseEntity.ok(permissionMapper.toResponse(permission));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_PERMISSION')")
    public ResponseEntity<PermissionResponse> updatePermission(
            @PathVariable String id,
            @Valid @RequestBody UpdatePermissionRequest request) {
        Permission permission = roleManagementService.updatePermission(
                id,
                request.getName(),
                request.getDescription()
        );
        return ResponseEntity.ok(permissionMapper.toResponse(permission));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_PERMISSION')")
    public ResponseEntity<Void> deletePermission(@PathVariable String id) {
        roleManagementService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
