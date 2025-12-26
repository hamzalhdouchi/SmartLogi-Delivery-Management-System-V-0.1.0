package com.smartlogi.security.userMapper;

import com.smartlogi.security.dto.authDto.request.SignupRequest;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.security.dto.authDto.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {RoleMapper.class}
)
public interface UserMapper {

    @Mapping(target = "role" ,source = "role.name")
    UserResponse toResponse(User user);

    List<UserResponse> toRoleResponseList(List<User> users);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "role.name" , source = "role")
    User toEntity(SignupRequest signupRequest);
}
