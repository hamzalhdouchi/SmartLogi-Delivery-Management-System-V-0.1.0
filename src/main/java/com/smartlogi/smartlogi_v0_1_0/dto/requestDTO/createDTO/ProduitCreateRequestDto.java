package com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitCreateRequestDto {

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(max = 100, message = "Le nom du produit ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9À-ÿ\\s\\-,.&]+$",
            message = "Le nom du produit contient des caractères non autorisés")
    @Column(unique = true)
    private String nom;

    @NotBlank(message = "La catégorie du produit est obligatoire")
    @Size(max = 100, message = "La catégorie ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZÀ-ÿ\\s\\-&]+$",
            message = "La catégorie contient des caractères non autorisés")
    private String categorie;

    @NotNull(message = "Le poids du produit est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids doit être d'au moins 0.01 kg")
    @Column(precision = 10, scale = 3, nullable = false)
    private BigDecimal poids;

    @NotNull(message = "Le prix du produit est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être d'au moins 0.01")
    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal prix;
}