package com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.ColisProduit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColisProduitResponseDto {
    private String colisId;
    private String produitId;
    private String produitNom;
    private String produitCategorie;
    private Integer quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal prixTotal;
    private LocalDateTime dateAjout;
}