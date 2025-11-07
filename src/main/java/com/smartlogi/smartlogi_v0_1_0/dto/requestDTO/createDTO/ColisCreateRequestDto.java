package com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ProduitCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ColisCreateRequestDto implements Serializable{

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

    @NotNull(message = "L'expéditeur est obligatoire")
    private String clientExpediteurId;

    @NotNull(message = "Le destinataire est obligatoire")
    private String destinataireId;

    private String livreurId;
    private String zoneId;

    @Valid
    private List<ProduitColisDto> produits;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProduitColisDto {
        private String produitId;
        @Valid
        private ProduitCreateRequestDto nouveauProduit;
        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 1, message = "La quantité doit être au moins 1")
        private Integer quantite;
        private BigDecimal prix;
    }
}