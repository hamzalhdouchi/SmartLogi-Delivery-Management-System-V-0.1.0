package com.smartlogi.smartlogi_v0_1_0.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ColisProduitDto {
    private Long colisId;
    private Long produitId;
    private String produitNom;
    private String produitCategorie;
    private Integer quantite;
    private BigDecimal prix;
    private LocalDateTime dateAjout;

    // Constructeurs, Getters et Setters
    public ColisProduitDto() {}

    public Long getColisId() { return colisId; }
    public void setColisId(Long colisId) { this.colisId = colisId; }

    public Long getProduitId() { return produitId; }
    public void setProduitId(Long produitId) { this.produitId = produitId; }

    public String getProduitNom() { return produitNom; }
    public void setProduitNom(String produitNom) { this.produitNom = produitNom; }

    public String getProduitCategorie() { return produitCategorie; }
    public void setProduitCategorie(String produitCategorie) { this.produitCategorie = produitCategorie; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public LocalDateTime getDateAjout() { return dateAjout; }
    public void setDateAjout(LocalDateTime dateAjout) { this.dateAjout = dateAjout; }
}