package com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitSimpleResponseDto {
    private String id;
    private String nom;
    private String categorie;
    private BigDecimal poids;
    private BigDecimal prix;
    private LocalDateTime dateCreation;
}