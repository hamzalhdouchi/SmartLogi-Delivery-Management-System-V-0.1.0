package com.smartlogi.security.dto.authDto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s'-]+$", message = "Le nom ne doit contenir que des lettres")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 50, message = "Le prénom doit contenir entre 2 et 50 caractères")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s'-]+$", message = "Le prénom ne doit contenir que des lettres")
    private String prenom;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(
            regexp = "^(\\+212|0)[5-7][0-9]{8}$",
            message = "Format téléphone invalide. Utilisez: 0612345678 ou +212612345678"
    )
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format email invalide")
    @Size(max = 100, message = "L'email ne doit pas dépasser 100 caractères")
    private String email;

    @NotBlank(message = "L'adresse est obligatoire")
    @Size(min = 10, max = 255, message = "L'adresse doit contenir entre 10 et 255 caractères")
    private String adresse;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, max = 100, message = "Le mot de passe doit contenir entre 8 et 100 caractères")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Le mot de passe doit contenir au moins: 1 majuscule, 1 minuscule, 1 chiffre et 1 caractère spécial"
    )
    private String password;

    @NotNull(message = "Le rôle est obligatoire")
    private String role;

}
