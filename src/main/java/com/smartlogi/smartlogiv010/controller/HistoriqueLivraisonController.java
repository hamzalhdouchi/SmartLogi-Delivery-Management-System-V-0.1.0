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

    @Operation(
            summary = "Obtenir l'historique d'un colis (ordre chronologique)",
            description = "Récupérer l'historique d'un colis trié par date de changement (du plus ancien au plus récent)"
    )
    @GetMapping("/colis/{colisId}/asc")
    public ResponseEntity<ApiResponse<List<HistoriqueLivraisonResponseDto>>> getByColisOrderByDateAsc(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId) {
        List<HistoriqueLivraisonResponseDto> historiques = historiqueLivraisonService.getByColisOrderByDateAsc(colisId);

        ApiResponse<List<HistoriqueLivraisonResponseDto>> response = ApiResponse.<List<HistoriqueLivraisonResponseDto>>builder()
                .success(true)
                .message("Historique du colis (ordre chronologique) récupéré avec succès")
                .data(historiques)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir l'historique d'un colis (ordre anti-chronologique)",
            description = "Récupérer l'historique d'un colis trié par date de changement (du plus récent au plus ancien)"
    )
    @GetMapping("/colis/{colisId}/desc")
    public ResponseEntity<ApiResponse<List<HistoriqueLivraisonResponseDto>>> getByColisOrderByDateDesc(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId) {
        List<HistoriqueLivraisonResponseDto> historiques = historiqueLivraisonService.getByColisOrderByDateDesc(colisId);

        ApiResponse<List<HistoriqueLivraisonResponseDto>> response = ApiResponse.<List<HistoriqueLivraisonResponseDto>>builder()
                .success(true)
                .message("Historique du colis (ordre anti-chronologique) récupéré avec succès")
                .data(historiques)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les historiques par statut",
            description = "Récupérer tous les historiques de livraison ayant un statut spécifique"
    )
    @GetMapping("/statut/{statut}")
    public ResponseEntity<ApiResponse<List<HistoriqueLivraisonResponseDto>>> getByStatut(
            @Parameter(description = "Statut des livraisons", required = true, example = "EN_COURS")
            @PathVariable StatutColis statut) {
        List<HistoriqueLivraisonResponseDto> historiques = historiqueLivraisonService.getByStatut(statut);

        ApiResponse<List<HistoriqueLivraisonResponseDto>> response = ApiResponse.<List<HistoriqueLivraisonResponseDto>>builder()
                .success(true)
                .message("Historiques par statut récupérés avec succès")
                .data(historiques)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les historiques d'un colis par statut",
            description = "Récupérer les historiques d'un colis spécifique avec un statut donné"
    )
    @GetMapping("/colis/{colisId}/statut/{statut}")
    public ResponseEntity<ApiResponse<List<HistoriqueLivraisonResponseDto>>> getByColisIdAndStatut(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId,
            @Parameter(description = "Statut des livraisons", required = true, example = "LIVRE")
            @PathVariable StatutColis statut) {
        List<HistoriqueLivraisonResponseDto> historiques = historiqueLivraisonService.getByColisIdAndStatut(colisId, statut);

        ApiResponse<List<HistoriqueLivraisonResponseDto>> response = ApiResponse.<List<HistoriqueLivraisonResponseDto>>builder()
                .success(true)
                .message("Historiques du colis par statut récupérés avec succès")
                .data(historiques)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les historiques par période",
            description = "Récupérer les historiques de livraison créés dans une période spécifique"
    )
    @GetMapping("/date-changement")
    public ResponseEntity<ApiResponse<List<HistoriqueLivraisonResponseDto>>> getByDateChangementBetween(
            @Parameter(description = "Date de début (format ISO)", required = true, example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Date de fin (format ISO)", required = true, example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<HistoriqueLivraisonResponseDto> historiques = historiqueLivraisonService.getByDateChangementBetween(startDate, endDate);

        ApiResponse<List<HistoriqueLivraisonResponseDto>> response = ApiResponse.<List<HistoriqueLivraisonResponseDto>>builder()
                .success(true)
                .message("Historiques par période de changement récupérés avec succès")
                .data(historiques)
                .build();

        return ResponseEntity.ok(response);
    }
}