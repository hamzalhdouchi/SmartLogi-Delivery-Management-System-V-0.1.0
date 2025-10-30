package com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneSimpleResponseDto {
    private String id;
    private String nom;
    private String codePostal;
    private LocalDateTime dateCreation;
}