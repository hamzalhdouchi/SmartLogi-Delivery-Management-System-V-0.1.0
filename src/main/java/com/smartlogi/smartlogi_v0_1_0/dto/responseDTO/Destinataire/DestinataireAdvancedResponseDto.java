package com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Destinataire;

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
public class DestinataireAdvancedResponseDto {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDateTime dateCreation;
    private List<ColisSimpleResponseDto> colis = new ArrayList<>();
}