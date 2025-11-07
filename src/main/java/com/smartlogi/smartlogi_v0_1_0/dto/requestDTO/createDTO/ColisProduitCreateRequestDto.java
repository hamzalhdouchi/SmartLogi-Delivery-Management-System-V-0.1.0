package com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColisProduitCreateRequestDto {

    @NotBlank(message = "L'ID du colis est obligatoire")
    private String colisId;

    private String produitId;

    @Valid
    private ProduitCreateRequestDto nouveauProduit;

    @NotNull(message = "La quantité est obligatoire")
    @Min(value = 1, message = "La quantité doit être au moins 1")
    private Integer quantite;

    @NotNull(message = "Le prix est obligatoire")
    @Min(value = 0, message = "Le prix ne peut pas être négatif")
    private BigDecimal prix;


}