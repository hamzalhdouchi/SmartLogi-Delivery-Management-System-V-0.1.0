package com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis;

import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColisSimpleResponseDto {
    private Long id;
    private String description;
    private BigDecimal poids;
    private StatutColis statut;
    private Priorite priorite;
    private String villeDestination;
    private String clientExpediteurId;
    private String clientExpediteurNom;
    private String destinataireId;
    private String destinataireNom;
    private String livreurId;
    private String livreurNom;
    private String zoneId;
    private String zoneNom;
    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;
}