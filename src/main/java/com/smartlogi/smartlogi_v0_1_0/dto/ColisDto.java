package com.smartlogi.smartlogi_v0_1_0.dto;

import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ColisDto {
    private Long id;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le poids est obligatoire")
    @DecimalMin(value = "0.1", message = "Le poids doit être supérieur à 0")
    private BigDecimal poids;

    private StatutColis statut;
    private Priorite priorite;

    @NotBlank(message = "La ville de destination est obligatoire")
    @Size(max = 100, message = "La ville ne doit pas dépasser 100 caractères")
    private String villeDestination;

    @NotNull(message = "L'expéditeur est obligatoire")
    private Long clientExpediteurId;
    private String clientExpediteurNom;

    @NotNull(message = "Le destinataire est obligatoire")
    private Long destinataireId;
    private String destinataireNom;

    private Long livreurId;
    private String livreurNom;

    private Long zoneId;
    private String zoneNom;

    private LocalDateTime dateCreation;
    private LocalDateTime dateModification;

    private List<HistoriqueLivraisonDto> historique;
    private List<ColisProduitDto> produits;

    // Constructeurs, Getters et Setters
    public ColisDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

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

    public Long getClientExpediteurId() { return clientExpediteurId; }
    public void setClientExpediteurId(Long clientExpediteurId) { this.clientExpediteurId = clientExpediteurId; }

    public String getClientExpediteurNom() { return clientExpediteurNom; }
    public void setClientExpediteurNom(String clientExpediteurNom) { this.clientExpediteurNom = clientExpediteurNom; }

    public Long getDestinataireId() { return destinataireId; }
    public void setDestinataireId(Long destinataireId) { this.destinataireId = destinataireId; }

    public String getDestinataireNom() { return destinataireNom; }
    public void setDestinataireNom(String destinataireNom) { this.destinataireNom = destinataireNom; }

    public Long getLivreurId() { return livreurId; }
    public void setLivreurId(Long livreurId) { this.livreurId = livreurId; }

    public String getLivreurNom() { return livreurNom; }
    public void setLivreurNom(String livreurNom) { this.livreurNom = livreurNom; }

    public Long getZoneId() { return zoneId; }
    public void setZoneId(Long zoneId) { this.zoneId = zoneId; }

    public String getZoneNom() { return zoneNom; }
    public void setZoneNom(String zoneNom) { this.zoneNom = zoneNom; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }

    public LocalDateTime getDateModification() { return dateModification; }
    public void setDateModification(LocalDateTime dateModification) { this.dateModification = dateModification; }

    public List<HistoriqueLivraisonDto> getHistorique() { return historique; }
    public void setHistorique(List<HistoriqueLivraisonDto> historique) { this.historique = historique; }

    public List<ColisProduitDto> getProduits() { return produits; }
    public void setProduits(List<ColisProduitDto> produits) { this.produits = produits; }
}