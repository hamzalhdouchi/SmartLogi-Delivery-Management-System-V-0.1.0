package com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO;

import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColisUpdateRequestDto {

    @NotBlank(message = "The ID est obligatoire")
    private String id;

    @NotBlank(message = "La description est obligatoire")
    private String description;

    @NotNull(message = "Le poids est obligatoire")
    @DecimalMin(value = "0.1", message = "Le poids doit être supérieur à 0")
    private BigDecimal poids;

    private StatutColis statut;
    private Priorite priorite;

    @NotBlank(message = "La ville de destination est obligatoire")
    @Size(max = 100, message = "La ville ne doit pas dépasser 100 caractères")
    private String villeDestination;

    private String livreurId;
    private String zoneId;


}