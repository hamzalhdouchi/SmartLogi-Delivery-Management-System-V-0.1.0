package com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO;

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
public class HistoriqueLivraisonUpdateRequestDto {

    @NotBlank(message = "L'ID est obligatoire")
    private String id;

    @NotNull(message = "Le statut est obligatoire")
    private StatutColis statut;

    @NotNull(message = "La date de changement est obligatoire")
    private LocalDateTime dateChangement;

    private String commentaire;
}