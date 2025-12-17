package com.smartlogi.smartlogiv010.security.controller;

import com.smartlogi.smartlogiv010.entity.Permission;
import com.smartlogi.smartlogiv010.entity.Role;
import com.smartlogi.smartlogiv010.security.dto.*;
import com.smartlogi.smartlogiv010.security.service.RoleManagementService;
import com.smartlogi.smartlogiv010.security.userMapper.PermissionMapper;
import com.smartlogi.smartlogiv010.security.userMapper.RoleMapper;
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
@PreAuthorize("hasRole('ADMIN')")
public class RoleManagementController {

    private final RoleManagementService roleManagementService;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ROLE')")
    public ResponseEntity<RoleResponse> createRole(@Valid @RequestBody CreateRoleRequest request) {
        Role role;

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            role = roleManagementService.createRoleWithPermissions(
                    request.getName(),
                    request.getPermissionIds()
            );
        } else {
            role = roleManagementService.createRole(request.getName());
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(roleMapper.toResponse(role));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ROLES')")
    public ResponseEntity<List<RoleResponse>> getAllRoles() {
        List<Role> roles = roleManagementService.getAllRoles();
        return ResponseEntity.ok(roleMapper.toResponseList(roles));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_ROLES')")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable String id) {
        Role role = roleManagementService.getRoleById(id);
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasAuthority('READ_ROLES')")
    public ResponseEntity<RoleResponse> getRoleByName(@PathVariable String name) {
        Role role = roleManagementService.getRoleByName(name);
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable String id,
            @Valid @RequestBody UpdateRoleRequest request) {
        Role role = roleManagementService.updateRoleName(id, request.getName());
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_ROLE')")
    public ResponseEntity<Void> deleteRole(@PathVariable String id) {
        roleManagementService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('READ_ROLES')")
    public ResponseEntity<Set<PermissionResponse>> getRolePermissions(@PathVariable String id) {
        Set<Permission> permissions = roleManagementService.getRolePermissions(id);
        return ResponseEntity.ok(permissionMapper.toResponseSet(permissions));
    }

    @PostMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('ASSIGN_PERMISSION')")
    public ResponseEntity<RoleResponse> addPermissionToRole(
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        Role role = roleManagementService.addPermissionToRole(roleId, permissionId);
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    @PostMapping("/{roleId}/permissions/batch")
    @PreAuthorize("hasAuthority('ASSIGN_PERMISSION')")
    public ResponseEntity<RoleResponse> addPermissionsToRole(
            @PathVariable String roleId,
            @Valid @RequestBody AssignPermissionsRequest request) {
        Role role = roleManagementService.addPermissionsToRole(roleId, request.getPermissionIds());
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    @DeleteMapping("/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasAuthority('REMOVE_PERMISSION')")
    public ResponseEntity<RoleResponse> removePermissionFromRole(
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        Role role = roleManagementService.removePermissionFromRole(roleId, permissionId);
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    @DeleteMapping("/{roleId}/permissions/batch")
    @PreAuthorize("hasAuthority('REMOVE_PERMISSION')")
    public ResponseEntity<RoleResponse> removePermissionsFromRole(
            @PathVariable String roleId,
            @Valid @RequestBody AssignPermissionsRequest request) {
        Role role = roleManagementService.removePermissionsFromRole(roleId, request.getPermissionIds());
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    @PutMapping("/{roleId}/permissions")
    @PreAuthorize("hasAuthority('MANAGE_PERMISSIONS')")
    public ResponseEntity<RoleResponse> replaceRolePermissions(
            @PathVariable String roleId,
            @Valid @RequestBody AssignPermissionsRequest request) {
        Role role = roleManagementService.replaceRolePermissions(roleId, request.getPermissionIds());
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }
}
