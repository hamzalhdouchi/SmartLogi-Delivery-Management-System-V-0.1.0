package com.smartlogi.smartlogi_v0_1_0.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatistiquesDto {
    private Long totalColis;
    private Long colisLives;
    private Long colisEnCours;
    private Map<String, Long> colisParStatut;
    private Map<String, Long> colisParZone;
    private Map<String, Long> colisParPriorite;
    private BigDecimal poidsTotal;
    private Long nombreLivreursActifs;

    private LocalDateTime dateGeneration;


}