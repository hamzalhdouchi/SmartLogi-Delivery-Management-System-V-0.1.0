package com.smartlogi.smartlogiv010.dto.requestDTO.createDTO;

import com.smartlogi.smartlogiv010.enums.StatutColis;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoriqueLivraisonCreateRequestDto {

    @NotBlank(message = "L'ID du colis est obligatoire")
    private String colisId;

    @NotNull(message = "Le statut est obligatoire")
    private StatutColis statut;

    @NotNull(message = "La date de changement est obligatoire")
    private LocalDateTime dateChangement;

    private String commentaire;
}