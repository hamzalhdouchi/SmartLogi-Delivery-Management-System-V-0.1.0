package com.smartlogi.smartlogiv010.dto.requestDTO.createDTO;

import com.smartlogi.smartlogiv010.enums.RoleUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivreurCreateRequestDto {

    @NotBlank(message = "Le nom du livreur est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s\\-']+$",
            message = "Le nom ne doit contenir que des lettres, espaces, tirets et apostrophes")
    private String nom;

    @NotBlank(message = "Le prénom du livreur est obligatoire")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s\\-']+$",
            message = "Le prénom ne doit contenir que des lettres, espaces, tirets et apostrophes")
    private String prenom;

    @NotBlank(message = "Le téléphone du livreur est obligatoire")
    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    @Pattern(regexp = "^(\\+212|0)[5-7][0-9]{8}$",
            message = "Le numéro de téléphone doit être un numéro marocain valide (ex: +212612345678 ou 0612345678)")
    private String telephone;

    @NotBlank(message = "L'email est obligatoire")
    @Size(max = 150, message = "L'email ne doit pas dépasser 150 caractères")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le type de véhicule est obligatoire")
    @Size(max = 50, message = "Le type de véhicule ne doit pas dépasser 50 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9À-ÿ\\s\\-]+$",
            message = "Le type de véhicule contient des caractères non autorisés")
    private String vehicule;

    private String zoneId;

    private RoleUser role;

    @NotBlank(message = "Le password est obligatoire")
    @Size(min = 8,message = "Le password ne doit min 8  caractères")
    private String password;
}