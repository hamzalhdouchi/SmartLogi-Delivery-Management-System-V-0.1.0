package com.smartlogi.security.dto.authDto.response;


import com.smartlogi.security.dto.roleDTO.response.RoleResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private RoleResponse role;
    private boolean enabled;
}

