package com.smartlogi.smartlogi_v0_1_0.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class ZoneDto {
    private String id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    @NotBlank(message = "Le code postal est obligatoire")
    @Size(max = 10, message = "Le code postal ne doit pas dépasser 10 caractères")
    private String codePostal;

    private Integer nombreLivreurs;
    private Integer nombreColis;

    private LocalDateTime dateCreation;


}