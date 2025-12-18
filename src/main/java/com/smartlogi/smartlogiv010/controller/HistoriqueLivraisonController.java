package com.smartlogi.smartlogiv010.controller;

import com.smartlogi.smartlogiv010.apiResponse.ApiResponse;
import com.smartlogi.smartlogiv010.dto.responseDTO.HistoriqueLivraison.HistoriqueLivraisonResponseDto;
import com.smartlogi.smartlogiv010.enums.StatutColis;
import com.smartlogi.smartlogiv010.service.interfaces.HistoriqueLivraisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/historique-livraison")
@RequiredArgsConstructor
@Tag(name = "Historique de Livraison", description = "API pour le suivi et la consultation de l'historique des livraisons")
public class HistoriqueLivraisonController {

    private final HistoriqueLivraisonService historiqueLivraisonService;

    @Operation(
            summary = "Obtenir un historique par ID",
            description = "Récupérer les détails d'un enregistrement spécifique de l'historique de livraison"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_READ_OWN_COLIS_HISTORIQUE')")
    public ResponseEntity<ApiResponse<HistoriqueLivraisonResponseDto>> getById(
            @Parameter(description = "ID de l'historique de livraison", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        HistoriqueLivraisonResponseDto historique = historiqueLivraisonService.getById(id);

        ApiResponse<HistoriqueLivraisonResponseDto> response = ApiResponse.<HistoriqueLivraisonResponseDto>builder()
                .success(true)
                .message("Historique de livraison récupéré avec succès")
                .data(historique)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister tous les historiques",
            description = "Récupérer la liste complète de tous les enregistrements d'historique de livraison"
    )
    @GetMapping
    @PreAuthorize("hasAuthority('CAN_READ_COLIS_HISTORIQUE_FULL')")
    public ResponseEntity<ApiResponse<List<HistoriqueLivraisonResponseDto>>> getAll() {
        List<HistoriqueLivraisonResponseDto> historiques = historiqueLivraisonService.getAll();

        ApiResponse<List<HistoriqueLivraisonResponseDto>> response = ApiResponse.<List<HistoriqueLivraisonResponseDto>>builder()
                .success(true)
                .message("Liste des historiques de livraison récupérée avec succès")
                .data(historiques)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister les historiques paginés",
            description = "Récupérer les historiques de livraison avec pagination, tri et filtres"
    )
    @GetMapping("/paginated")
    @PreAuthorize("hasAuthority('CAN_READ_COLIS_HISTORIQUE_FULL')")
    public ResponseEntity<ApiResponse<Page<HistoriqueLivraisonResponseDto>>> getAllPaginated(
            @Parameter(description = "Paramètres de pagination et de tri")
            Pageable pageable) {
        Page<HistoriqueLivraisonResponseDto> historiques = historiqueLivraisonService.getAll(pageable);

        ApiResponse<Page<HistoriqueLivraisonResponseDto>> response = ApiResponse.<Page<HistoriqueLivraisonResponseDto>>builder()
                .success(true)
                .message("Historiques de livraison paginés récupérés avec succès")
                .data(historiques)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir l'historique d'un colis",
            description = "Récupérer tous les enregistrements d'historique pour un colis spécifique"
    )
    @GetMapping("/colis/{colisId}")
    @PreAuthorize("hasAuthority('CAN_READ_OWN_COLIS_HISTORIQUE')")
    public ResponseEntity<ApiResponse<List<HistoriqueLivraisonResponseDto>>> getByColis(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId) {
        List<HistoriqueLivraisonResponseDto> historiques = historiqueLivraisonService.getByColis(colisId);

        ApiResponse<List<HistoriqueLivraisonResponseDto>> response = ApiResponse.<List<HistoriqueLivraisonResponseDto>>builder()
                .success(true)
                .message("Historique du colis récupéré avec succès")
                .data(historiques)
                .build();

        return ResponseEntity.ok(response);
    }
}