package com.smartlogi.smartlogiv010.security.controller;

import com.smartlogi.smartlogiv010.entity.Permission;
import com.smartlogi.smartlogiv010.entity.Role;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.smartlogiv010.security.dto.PermissionResponse;
import com.smartlogi.smartlogiv010.security.dto.RoleResponse;
import com.smartlogi.smartlogiv010.security.dto.*;
import com.smartlogi.smartlogiv010.security.service.RoleManagementService;
import com.smartlogi.smartlogiv010.security.userMapper.PermissionMapper;
import com.smartlogi.smartlogiv010.security.userMapper.RoleMapper;
import com.smartlogi.smartlogiv010.security.userMapper.UserMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserRoleController {

    private final RoleManagementService roleManagementService;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final UserMapper userMapper;

    @PostMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('ASSIGN_ROLE')")
    public ResponseEntity<UserResponse> assignRoleToUser(
            @PathVariable String userId,
            @Valid @RequestBody AssignRoleRequest request) {
        User user = roleManagementService.assignRoleToUser(userId, request.getRoleId());
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @PutMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('UPDATE_USER_ROLE')")
    public ResponseEntity<UserResponse> changeUserRole(
            @PathVariable String userId,
            @Valid @RequestBody AssignRoleRequest request) {
        User user = roleManagementService.changeUserRole(userId, request.getRoleId());
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @DeleteMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('REMOVE_USER_ROLE')")
    public ResponseEntity<UserResponse> removeRoleFromUser(@PathVariable String userId) {
        User user = roleManagementService.removeRoleFromUser(userId);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('READ_USER_ROLE')")
    public ResponseEntity<RoleResponse> getUserRole(@PathVariable String userId) {
        Role role = roleManagementService.getUserRole(userId);
        return ResponseEntity.ok(roleMapper.toResponse(role));
    }

    @GetMapping("/{userId}/permissions")
    @PreAuthorize("hasAuthority('READ_USER_PERMISSIONS')")
    public ResponseEntity<Set<PermissionResponse>> getUserPermissions(@PathVariable String userId) {
        Set<Permission> permissions = roleManagementService.getUserPermissions(userId);
        return ResponseEntity.ok(permissionMapper.toResponseSet(permissions));
    }

    @GetMapping("/{userId}/has-permission")
    @PreAuthorize("hasAuthority('CHECK_USER_PERMISSION')")
    public ResponseEntity<CheckPermissionResponse> userHasPermission(
            @PathVariable String userId,
            @RequestParam String permission) {
        boolean hasPermission = roleManagementService.userHasPermission(userId, permission);
        return ResponseEntity.ok(new CheckPermissionResponse(hasPermission));
    }

    @GetMapping("/{userId}/has-role")
    @PreAuthorize("hasAuthority('CHECK_USER_ROLE')")
    public ResponseEntity<CheckRoleResponse> userHasRole(
            @PathVariable String userId,
            @RequestParam String role) {
        boolean hasRole = roleManagementService.userHasRole(userId, role);
        return ResponseEntity.ok(new CheckRoleResponse(hasRole));
    }

    @GetMapping("/by-role/{roleId}")
    @PreAuthorize("hasAuthority('READ_USERS_BY_ROLE')")
    public ResponseEntity<?> getUsersByRole(@PathVariable String roleId) {
        Optional<User> users = roleManagementService.getUsersByRole(roleId);
        return ResponseEntity.ok(users.map(userMapper::toResponse));
    }
}
