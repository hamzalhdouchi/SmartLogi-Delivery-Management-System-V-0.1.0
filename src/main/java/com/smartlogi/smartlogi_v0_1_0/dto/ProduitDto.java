package com.smartlogi.smartlogi_v0_1_0.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProduitDto {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    @Size(max = 100, message = "La catégorie ne doit pas dépasser 100 caractères")
    private String categorie;

    @DecimalMin(value = "0.1", message = "Le poids doit être supérieur à 0")
    private BigDecimal poids;

    @DecimalMin(value = "0.0", message = "Le prix ne peut pas être négatif")
    private BigDecimal prix;

    private LocalDateTime dateCreation;

    // Constructeurs, Getters et Setters
    public ProduitDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public BigDecimal getPoids() { return poids; }
    public void setPoids(BigDecimal poids) { this.poids = poids; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}