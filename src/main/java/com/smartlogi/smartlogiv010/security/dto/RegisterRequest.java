package com.smartlogi.smartlogiv010.security.dto;

import com.smartlogi.smartlogiv010.enums.RoleUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String adresse;
    private String password;
    private RoleUser role;
}
