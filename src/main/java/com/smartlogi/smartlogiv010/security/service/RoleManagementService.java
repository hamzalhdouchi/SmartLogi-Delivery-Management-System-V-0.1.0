package com.smartlogi.smartlogiv010.security.service;

import com.smartlogi.smartlogiv010.entity.Permission;
import com.smartlogi.smartlogiv010.entity.Role;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.smartlogiv010.repository.PermissionRepository;
import com.smartlogi.smartlogiv010.repository.RoleRepository;
import com.smartlogi.smartlogiv010.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleManagementService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRepository userRepository;

    public Role createRole(String roleName) {
        if (roleRepository.findByName(roleName).isPresent()) {
            throw new RuntimeException("Role already exists: " + roleName);
        }

        Role role = new Role();
        role.setName(roleName);
        role.setPermissions(new HashSet<>());
        return roleRepository.save(role);
    }

    public Role createRoleWithPermissions(String roleName, Set<String> permissionIds) {
        Role role = createRole(roleName);

        Set<Permission> permissions = new HashSet<>();
        for (String permissionId : permissionIds) {
            Permission permission = permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new RuntimeException("Permission not found: " + permissionId));
            permissions.add(permission);
        }

        role.setPermissions(permissions);
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(String roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));
    }

    public Role getRoleByName(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
    }

    public Role updateRoleName(String roleId, String newRoleName) {
        Role role = getRoleById(roleId);

        if (roleRepository.findByName(newRoleName).isPresent()) {
            throw new RuntimeException("Role name already exists: " + newRoleName);
        }

        role.setName(newRoleName);
        return roleRepository.save(role);
    }

    public void deleteRole(String roleId) {
        Role role = getRoleById(roleId);
        if (userRepository.existsByRole(role)) {
            long count = userRepository.countByRole(role);
            throw new RuntimeException("Cannot delete role. " + count + " users still have this role");
        }

        roleRepository.delete(role);
    }

    public Permission createPermission(String permissionName, String description) {
        if (permissionRepository.findByName(permissionName).isPresent()) {
            throw new RuntimeException("Permission already exists: " + permissionName);
        }

        Permission permission = new Permission();
        permission.setName(permissionName);
        permission.setDescription(description);
        return permissionRepository.save(permission);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Permission getPermissionById(String permissionId) {
        return permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));
    }

    public Permission updatePermission(String permissionId, String newName, String newDescription) {
        Permission permission = getPermissionById(permissionId);

        if (newName != null && !newName.equals(permission.getName())) {
            if (permissionRepository.findByName(newName).isPresent()) {
                throw new RuntimeException("Permission name already exists: " + newName);
            }
            permission.setName(newName);
        }

        if (newDescription != null) {
            permission.setDescription(newDescription);
        }

        return permissionRepository.save(permission);
    }

    public void deletePermission(String permissionId) {
        Permission permission = getPermissionById(permissionId);

        List<Role> rolesWithPermission = roleRepository.findAll().stream()
                .filter(role -> role.getPermissions().contains(permission))
                .toList();

        for (Role role : rolesWithPermission) {
            role.getPermissions().remove(permission);
            roleRepository.save(role);
        }

        permissionRepository.delete(permission);
    }

    public Role addPermissionToRole(String roleId, String permissionId) {
        Role role = getRoleById(roleId);
        Permission permission = getPermissionById(permissionId);

        if (role.getPermissions().contains(permission)) {
            throw new RuntimeException("Role already has this permission");
        }

        role.getPermissions().add(permission);
        return roleRepository.save(role);
    }

    public Role addPermissionsToRole(String roleId, Set<String> permissionIds) {
        Role role = getRoleById(roleId);

        for (String permissionId : permissionIds) {
            Permission permission = getPermissionById(permissionId);
            role.getPermissions().add(permission);
        }

        return roleRepository.save(role);
    }

    public Role removePermissionFromRole(String roleId, String permissionId) {
        Role role = getRoleById(roleId);
        Permission permission = getPermissionById(permissionId);

        if (!role.getPermissions().contains(permission)) {
            throw new RuntimeException("Role does not have this permission");
        }

        role.getPermissions().remove(permission);
        return roleRepository.save(role);
    }

    public Role removePermissionsFromRole(String roleId, Set<String> permissionIds) {
        Role role = getRoleById(roleId);

        for (String permissionId : permissionIds) {
            Permission permission = getPermissionById(permissionId);
            role.getPermissions().remove(permission);
        }

        return roleRepository.save(role);
    }

    public Set<Permission> getRolePermissions(String roleId) {
        Role role = getRoleById(roleId);
        return role.getPermissions();
    }

    public Role replaceRolePermissions(String roleId, Set<String> permissionIds) {
        Role role = getRoleById(roleId);

        Set<Permission> newPermissions = new HashSet<>();
        for (String permissionId : permissionIds) {
            Permission permission = getPermissionById(permissionId);
            newPermissions.add(permission);
        }

        role.setPermissions(newPermissions);
        return roleRepository.save(role);
    }

    public User assignRoleToUser(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Role role = getRoleById(roleId);

        if (user.getRole() != null && user.getRole().equals(role)) {
            throw new RuntimeException("User already has this role");
        }

        user.setRole(role);
        return userRepository.save(user);
    }

    public User changeUserRole(String userId, String newRoleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Role newRole = getRoleById(newRoleId);
        user.setRole(newRole);

        return userRepository.save(user);
    }

    public User removeRoleFromUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getRole() == null) {
            throw new RuntimeException("User does not have any role");
        }

        user.setRole(null);
        return userRepository.save(user);
    }

    public Role getUserRole(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return user.getRole();
    }

    public Set<Permission> getUserPermissions(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (user.getRole() == null) {
            return new HashSet<>();
        }

        return user.getRole().getPermissions();
    }

    public boolean userHasPermission(String userId, String permissionName) {
        Set<Permission> permissions = getUserPermissions(userId);
        return permissions.stream()
                .anyMatch(permission -> permission.getName().equals(permissionName));
    }

    public boolean userHasRole(String userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return user.getRole() != null && user.getRole().getName().equals(roleName);
    }

    public Optional<User> getUsersByRole(String roleId) {
        Role role = getRoleById(roleId);
        return userRepository.findByRole(role);
    }
}
