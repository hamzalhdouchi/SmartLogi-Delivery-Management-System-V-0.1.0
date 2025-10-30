package com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone;

import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis.ColisSimpleResponseDto;
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