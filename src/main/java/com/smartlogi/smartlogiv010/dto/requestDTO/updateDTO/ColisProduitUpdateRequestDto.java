package com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO;


import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColisProduitUpdateRequestDto {


    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;

    @Min(value = 0, message = "Le prix ne peut pas être négatif")
    private BigDecimal prix;
}
