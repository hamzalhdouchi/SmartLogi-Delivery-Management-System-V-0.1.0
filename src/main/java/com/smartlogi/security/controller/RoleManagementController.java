package com.smartlogi.security.controller;

import com.smartlogi.security.dto.Permission.request.AssignPermissionsRequest;
import com.smartlogi.security.dto.Permission.response.PermissionResponse;
import com.smartlogi.security.dto.roleDTO.request.CreateRoleRequest;
import com.smartlogi.security.dto.roleDTO.request.UpdateRoleRequest;
import com.smartlogi.security.dto.roleDTO.response.RoleResponse;
import com.smartlogi.smartlogiv010.apiResponse.ApiResponse;
import com.smartlogi.smartlogiv010.entity.Permission;
import com.smartlogi.smartlogiv010.entity.Role;
import com.smartlogi.security.service.RoleManagementService;
import com.smartlogi.security.userMapper.PermissionMapper;
import com.smartlogi.security.userMapper.RoleMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/admin/roles")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RoleManagementController {

    private final RoleManagementService roleManagementService;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody CreateRoleRequest request) {
        Role role;

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            role = roleManagementService.createRoleWithPermissions(
                    request.getName(),
                    request.getPermissionIds()
            );
        } else {
            role = roleManagementService.createRole(request.getName());
        }
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Rôle créé avec succès")
                .data(roleMapper.toResponse(role))
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<List<RoleResponse>>> getAllRoles() {
        List<Role> roles = roleManagementService.getAllRoles();
        ApiResponse<List<RoleResponse>> response = ApiResponse.<List<RoleResponse>>builder()
                .success(true)
                .message("Liste des rôles récupérée avec succès")
                .data(roleMapper.toResponseList(roles))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleById(@PathVariable String id) {
        Role role = roleManagementService.getRoleById(id);
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Rôle récupéré avec succès")
                .data(roleMapper.toResponse(role))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<RoleResponse>> getRoleByName(@PathVariable String name) {
        Role role = roleManagementService.getRoleByName(name);
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Rôle récupéré avec succès")
                .data(roleMapper.toResponse(role))
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<RoleResponse>> updateRole(
            @PathVariable String id,
            @Valid @RequestBody UpdateRoleRequest request) {
        Role role = roleManagementService.updateRoleName(id, request.getName());
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Rôle mis à jour avec succès")
                .data(roleMapper.toResponse(role))
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<Void>> deleteRole(@PathVariable String id) {
        roleManagementService.deleteRole(id);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Rôle supprimé avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<Set<PermissionResponse>>> getRolePermissions(@PathVariable String id) {
        Set<Permission> permissions = roleManagementService.getRolePermissions(id);
        ApiResponse<Set<PermissionResponse>> response = ApiResponse.<Set<PermissionResponse>>builder()
                .success(true)
                .message("Permissions du rôle récupérées avec succès")
                .data(permissionMapper.toResponseSet(permissions))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<RoleResponse>> addPermissionToRole(
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        Role role = roleManagementService.addPermissionToRole(roleId, permissionId);
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Permission ajoutée au rôle avec succès")
                .data(roleMapper.toResponse(role))
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{roleId}/permissions/batch")
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<RoleResponse>> addPermissionsToRole(
            @PathVariable String roleId,
            @Valid @RequestBody AssignPermissionsRequest request) {
        Role role = roleManagementService.addPermissionsToRole(roleId, request.getPermissionIds());
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Permissions ajoutées au rôle avec succès")
                .data(roleMapper.toResponse(role))
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<RoleResponse>> removePermissionFromRole(
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        Role role = roleManagementService.removePermissionFromRole(roleId, permissionId);
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Permission retirée du rôle avec succès")
                .data(roleMapper.toResponse(role))
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roleId}/permissions/batch")
    @PreAuthorize("hasAuthority('CAN_MANAGE_ROLES')")
    public ResponseEntity<ApiResponse<RoleResponse>> removePermissionsFromRole(
            @PathVariable String roleId,
            @Valid @RequestBody AssignPermissionsRequest request) {
        Role role = roleManagementService.removePermissionsFromRole(roleId, request.getPermissionIds());
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Permissions retirées du rôle avec succès")
                .data(roleMapper.toResponse(role))
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('CAN_MANAGE_PERMISSIONS')")
    public ResponseEntity<ApiResponse<RoleResponse>> replaceRolePermissions(
            @PathVariable String roleId,
            @Valid @RequestBody AssignPermissionsRequest request) {
        Role role = roleManagementService.replaceRolePermissions(roleId, request.getPermissionIds());
        ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                .success(true)
                .message("Permissions du rôle remplacées avec succès")
                .data(roleMapper.toResponse(role))
                .build();

        return ResponseEntity.ok(response);
    }
}
