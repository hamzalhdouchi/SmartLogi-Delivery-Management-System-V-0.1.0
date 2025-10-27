package com.smartlogi.smartlogi_v0_1_0.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

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

    // Constructeurs, Getters et Setters
    public StatistiquesDto() {
        this.dateGeneration = LocalDateTime.now();
    }

    public Long getTotalColis() { return totalColis; }
    public void setTotalColis(Long totalColis) { this.totalColis = totalColis; }

    public Long getColisLives() { return colisLives; }
    public void setColisLives(Long colisLives) { this.colisLives = colisLives; }

    public Long getColisEnCours() { return colisEnCours; }
    public void setColisEnCours(Long colisEnCours) { this.colisEnCours = colisEnCours; }

    public Map<String, Long> getColisParStatut() { return colisParStatut; }
    public void setColisParStatut(Map<String, Long> colisParStatut) { this.colisParStatut = colisParStatut; }

    public Map<String, Long> getColisParZone() { return colisParZone; }
    public void setColisParZone(Map<String, Long> colisParZone) { this.colisParZone = colisParZone; }

    public Map<String, Long> getColisParPriorite() { return colisParPriorite; }
    public void setColisParPriorite(Map<String, Long> colisParPriorite) { this.colisParPriorite = colisParPriorite; }

    public BigDecimal getPoidsTotal() { return poidsTotal; }
    public void setPoidsTotal(BigDecimal poidsTotal) { this.poidsTotal = poidsTotal; }

    public Long getNombreLivreursActifs() { return nombreLivreursActifs; }
    public void setNombreLivreursActifs(Long nombreLivreursActifs) { this.nombreLivreursActifs = nombreLivreursActifs; }

    public LocalDateTime getDateGeneration() { return dateGeneration; }
    public void setDateGeneration(LocalDateTime dateGeneration) { this.dateGeneration = dateGeneration; }
}