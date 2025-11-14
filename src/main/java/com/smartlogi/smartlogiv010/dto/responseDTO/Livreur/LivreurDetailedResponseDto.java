package com.smartlogi.smartlogiv010.dto.responseDTO.Livreur;

import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisSimpleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivreurDetailedResponseDto {
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
    private List<ColisSimpleResponseDto> colisAssignes = new ArrayList<>();
}