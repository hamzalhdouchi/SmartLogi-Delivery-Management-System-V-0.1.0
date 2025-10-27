package com.smartlogi.smartlogi_v0_1_0.dto;

import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public class ColisCreateDto {

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le poids est obligatoire")
    @DecimalMin(value = "0.1", message = "Le poids doit être supérieur à 0")
    private BigDecimal poids;

    private Priorite priorite = Priorite.NORMALE;

    @NotBlank(message = "La ville de destination est obligatoire")
    @Size(max = 100, message = "La ville ne doit pas dépasser 100 caractères")
    private String villeDestination;

    @NotNull(message = "L'expéditeur est obligatoire")
    private Long clientExpediteurId;

    @NotNull(message = "Le destinataire est obligatoire")
    private Long destinataireId;

    private Long zoneId;
    private List<ProduitColisDto> produits;

    // Constructeurs, Getters et Setters
    public ColisCreateDto() {}

    // Classe interne pour les produits dans la création
    public static class ProduitColisDto {
        private Long produitId;
        private Integer quantite;

        public Long getProduitId() { return produitId; }
        public void setProduitId(Long produitId) { this.produitId = produitId; }

        public Integer getQuantite() { return quantite; }
        public void setQuantite(Integer quantite) { this.quantite = quantite; }
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPoids() { return poids; }
    public void setPoids(BigDecimal poids) { this.poids = poids; }

    public Priorite getPriorite() { return priorite; }
    public void setPriorite(Priorite priorite) { this.priorite = priorite; }

    public String getVilleDestination() { return villeDestination; }
    public void setVilleDestination(String villeDestination) { this.villeDestination = villeDestination; }

    public Long getClientExpediteurId() { return clientExpediteurId; }
    public void setClientExpediteurId(Long clientExpediteurId) { this.clientExpediteurId = clientExpediteurId; }

    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }

    public Long getZoneId() { return zoneId; }
    public void setZoneId(Long zoneId) { this.zoneId = zoneId; }

    public List<ProduitColisDto> getProduits() { return produits; }
    public void setProduits(List<ProduitColisDto> produits) { this.produits = produits; }
}