package com.smartlogi.smartlogiv010.dto.responseDTO.Livreur;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivreurAdvancedResponseDto {
    private String id;
    private String nom;
    private String prenom;
    private String telephone;
    private String vehicule;
    private LocalDateTime dateCreation;
    private String zoneId;
    private String zoneNom;
    private Integer nombreColisAssignes;
    private Integer nombreColisLives;
    private Integer nombreColisEnCours;
}