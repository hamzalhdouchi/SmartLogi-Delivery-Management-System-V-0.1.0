package com.smartlogi.smartlogiv010.dto.responseDTO.HistoriqueLivraison;

import com.smartlogi.smartlogiv010.enums.StatutColis;
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