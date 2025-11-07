package com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClientExpediteurUpdateRequestDto {


    @NotBlank(message = "L'ID est obligatoire")
    private String id;

    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s\\-']+$", message = "Le nom ne doit contenir que des lettres, espaces, tirets et apostrophes")
    private String nom;

    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s\\-']+$", message = "Le prénom ne doit contenir que des lettres, espaces, tirets et apostrophes")
    private String prenom;

    @Size(max = 150, message = "L'email ne doit pas dépasser 150 caractères")

    @Email(message = "L'email doit être valide")
    private String email;

    @Size(max = 20, message = "Le téléphone ne doit pas dépasser 20 caractères")
    @Pattern(regexp = "^(\\+212|0)[5-7][0-9]{8}$", message = "Le numéro de téléphone doit être un numéro marocain valide (ex: +212612345678 ou 0612345678)")
    private String telephone;

    @Size(max = 255, message = "L'adresse ne doit pas dépasser 255 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9À-ÿ\\s\\-,.'#&]+$", message = "L'adresse contient des caractères non autorisés")
    private String adresse;


    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

}