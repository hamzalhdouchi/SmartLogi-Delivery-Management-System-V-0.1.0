package com.smartlogi.smartlogi_v0_1_0.dto;

import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class ColisUpdateDto {

    @Size(max = 255, message = "La description ne doit pas dépasser 255 caractères")
    private String description;

    @DecimalMin(value = "0.1", message = "Le poids doit être supérieur à 0")
    private BigDecimal poids;

    private StatutColis statut;
    private Priorite priorite;

    @Size(max = 100, message = "La ville ne doit pas dépasser 100 caractères")
    private String villeDestination;

    private Long livreurId;
    private Long zoneId;

    // Constructeurs, Getters et Setters
    public ColisUpdateDto() {}

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPoids() { return poids; }
    public void setPoids(BigDecimal poids) { this.poids = poids; }

    public StatutColis getStatut() { return statut; }
    public void setStatut(StatutColis statut) { this.statut = statut; }

    public Priorite getPriorite() { return priorite; }
    public void setPriorite(Priorite priorite) { this.priorite = priorite; }

    public String getVilleDestination() { return villeDestination; }
    public void setVilleDestination(String villeDestination) { this.villeDestination = villeDestination; }

    public Long getLivreurId() { return livreurId; }
    public void setLivreurId(Long livreurId) { this.livreurId = livreurId; }

    public Long getZoneId() { return zoneId; }
    public void setZoneId(Long zoneId) { this.zoneId = zoneId; }
}