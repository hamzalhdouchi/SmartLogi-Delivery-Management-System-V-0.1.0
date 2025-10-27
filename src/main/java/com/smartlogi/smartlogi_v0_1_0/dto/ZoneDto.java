package com.smartlogi.smartlogi_v0_1_0.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ZoneDto {
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    @NotBlank(message = "Le code postal est obligatoire")
    @Size(max = 10, message = "Le code postal ne doit pas dépasser 10 caractères")
    private String codePostal;

    private Integer nombreLivreurs;
    private Integer nombreColis;

    private LocalDateTime dateCreation;

    // Constructeurs, Getters et Setters
    public ZoneDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCodePostal() { return codePostal; }
    public void setCodePostal(String codePostal) { this.codePostal = codePostal; }

    public Integer getNombreLivreurs() { return nombreLivreurs; }
    public void setNombreLivreurs(Integer nombreLivreurs) { this.nombreLivreurs = nombreLivreurs; }

    public Integer getNombreColis() { return nombreColis; }
    public void setNombreColis(Integer nombreColis) { this.nombreColis = nombreColis; }

    public LocalDateTime getDateCreation() { return dateCreation; }
    public void setDateCreation(LocalDateTime dateCreation) { this.dateCreation = dateCreation; }
}