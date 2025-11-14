package com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoneUpdateRequestDto {

    private String id;

    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;

    @Size(max = 10, message = "Le code postal ne doit pas dépasser 10 caractères")
    private String codePostal;
}