package com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivreurSimpleResponseDto {
    private String id;
    private String nom;
    private String prenom;
    private String telephone;
    private String vehicule;
    private LocalDateTime dateCreation;
    private String zoneId;
    private String zoneNom;
}