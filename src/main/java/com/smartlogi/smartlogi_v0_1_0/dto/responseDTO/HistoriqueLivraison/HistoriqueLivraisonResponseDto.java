package com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.HistoriqueLivraison;

import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoriqueLivraisonResponseDto {
    private String id;
    private String colisId;
    private StatutColis statut;
    private LocalDateTime dateChangement;
    private String commentaire;
    private LocalDateTime dateCreation;
}