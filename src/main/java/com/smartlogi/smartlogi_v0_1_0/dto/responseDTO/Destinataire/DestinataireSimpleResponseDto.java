package com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Destinataire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinataireSimpleResponseDto {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private LocalDateTime dateCreation;
}