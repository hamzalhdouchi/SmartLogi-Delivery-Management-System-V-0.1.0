package com.smartlogi.security.userMapper;

import com.smartlogi.smartlogiv010.entity.Permission;
import com.smartlogi.security.dto.Permission.response.PermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {


    PermissionResponse toResponse(Permission permission);

    List<PermissionResponse> toResponseList(List<Permission> permissions);

    Set<PermissionResponse> toResponseSet(Set<Permission> permissions);

    Permission toEntity(PermissionResponse permissionResponse);
}
