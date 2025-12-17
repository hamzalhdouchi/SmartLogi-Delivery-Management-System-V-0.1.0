package com.smartlogi.smartlogiv010.security.userMapper;

import com.smartlogi.smartlogiv010.entity.Role;
import com.smartlogi.smartlogiv010.security.dto.RoleResponse;
import com.smartlogi.smartlogiv010.security.userMapper.PermissionMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {PermissionMapper.class} // Utilise PermissionMapper pour mapper les permissions
)
public interface RoleMapper {

    RoleResponse toResponse(Role role);

    List<RoleResponse> toResponseList(List<Role> roles);

    /**
     * Convertir Set<Role> -> Set<RoleResponse>
     */
    Set<RoleResponse> toResponseSet(Set<Role> roles);

    /**
     * Convertir RoleResponse -> Role (si besoin)
     */
    @Mapping(target = "permissions", ignore = true) // Ignorer pour éviter les conflits
    Role toEntity(RoleResponse roleResponse);
}
