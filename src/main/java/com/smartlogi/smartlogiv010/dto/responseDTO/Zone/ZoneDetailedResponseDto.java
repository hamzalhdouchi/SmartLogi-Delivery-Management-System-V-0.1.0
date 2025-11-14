package com.smartlogi.smartlogiv010.dto.responseDTO.Zone;

import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisSimpleResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurSimpleResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneDetailedResponseDto {
    private String id;
    private String nom;
    private String codePostal;
    private LocalDateTime dateCreation;
    private Integer nombreLivreurs;
    private Integer nombreColis;
    private List<LivreurSimpleResponseDto> livreurs = new ArrayList<>();
    private List<ColisSimpleResponseDto> colis = new ArrayList<>();
}