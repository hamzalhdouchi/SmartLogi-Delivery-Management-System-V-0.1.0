package com.smartlogi.smartlogiv010.dto.responseDTO.Produit;

import com.smartlogi.smartlogiv010.dto.responseDTO.ColisProduit.ColisProduitResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitDetailedResponseDto {
    private String id;
    private String nom;
    private String categorie;
    private BigDecimal poids;
    private BigDecimal prix;
    private LocalDateTime dateCreation;
    private Integer nombreColisAssocies;
    private Integer quantiteTotaleVendue;
    private BigDecimal chiffreAffaireTotal;
    private List<ColisProduitResponseDto> colisProduits = new ArrayList<>();
}