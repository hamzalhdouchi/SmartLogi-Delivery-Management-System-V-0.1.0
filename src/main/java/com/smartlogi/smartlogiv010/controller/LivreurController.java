package com.smartlogi.smartlogiv010.controller;

import com.smartlogi.smartlogiv010.apiResponse.ApiResponse;
import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.LivreurCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.LivreurUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurDetailedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurSimpleResponseDto;
import com.smartlogi.smartlogiv010.service.interfaces.LivreurService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestion des Livreurs", description = "API pour la gestion complète des livreurs (création, affectation, statistiques)")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class LivreurController {

    private final LivreurService livreurService;

    @Operation(
            summary = "Modifier un livreur",
            description = "Mettre à jour les informations d'un livreur existant"
    )
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_LIVREURS') && hasRole('ROLE_LIVREUR')")
    public ResponseEntity<ApiResponse<LivreurSimpleResponseDto>> update(
            @Parameter(description = "ID du livreur", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable("id") String id,
            @Parameter(description = "Données de mise à jour du livreur", required = true)
            @Valid @RequestBody LivreurUpdateRequestDto requestDto) {
        LivreurSimpleResponseDto updatedLivreur = livreurService.update(id, requestDto);

        ApiResponse<LivreurSimpleResponseDto> response = ApiResponse.<LivreurSimpleResponseDto>builder()
                .success(true)
                .message("Livreur mis à jour avec succès")
                .data(updatedLivreur)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir un livreur par ID",
            description = "Récupérer les informations de base d'un livreur spécifique"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_LIVREUR')")
    public ResponseEntity<ApiResponse<LivreurSimpleResponseDto>> getById(
            @Parameter(description = "ID du livreur", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        LivreurSimpleResponseDto livreur = livreurService.getById(id);

        ApiResponse<LivreurSimpleResponseDto> response = ApiResponse.<LivreurSimpleResponseDto>builder()
                .success(true)
                .message("Livreur récupéré avec succès")
                .data(livreur)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir un livreur avec statistiques",
            description = "Récupérer les informations détaillées d'un livreur incluant ses statistiques de performance"
    )
    @PreAuthorize("hasAuthority('CAN_MANAGE_LIVREURS')")
    @GetMapping("/{id}/advanced")
    public ResponseEntity<ApiResponse<LivreurAdvancedResponseDto>> getByIdWithStats(
            @Parameter(description = "ID du livreur", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        LivreurAdvancedResponseDto livreur = livreurService.getByIdWithStats(id);

        ApiResponse<LivreurAdvancedResponseDto> response = ApiResponse.<LivreurAdvancedResponseDto>builder()
                .success(true)
                .message("Livreur avec statistiques récupéré avec succès")
                .data(livreur)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir un livreur avec ses colis",
            description = "Récupérer les informations complètes d'un livreur incluant la liste de ses colis assignés"
    )
    @GetMapping("/{id}/detailed")
    @PreAuthorize("hasRole('ROLE_LIVREUR')")
    public ResponseEntity<ApiResponse<LivreurDetailedResponseDto>> getByIdWithColis(
            @Parameter(description = "ID du livreur", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        LivreurDetailedResponseDto livreur = livreurService.getByIdWithColis(id);

        ApiResponse<LivreurDetailedResponseDto> response = ApiResponse.<LivreurDetailedResponseDto>builder()
                .success(true)
                .message("Livreur avec colis récupéré avec succès")
                .data(livreur)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister les livreurs paginés",
            description = "Récupérer les livreurs avec pagination, tri et filtres"
    )
    @GetMapping("/paginated")
    @PreAuthorize("hasAuthority('CAN_MANAGE_LIVREURS')")
    public ResponseEntity<ApiResponse<Page<LivreurSimpleResponseDto>>> getAllPaginated(
            @Parameter(description = "Paramètres de pagination et de tri")
            Pageable pageable) {
        Page<LivreurSimpleResponseDto> livreurs = livreurService.getAll(pageable);

        ApiResponse<Page<LivreurSimpleResponseDto>> response = ApiResponse.<Page<LivreurSimpleResponseDto>>builder()
                .success(true)
                .message("Livreurs paginés récupérés avec succès")
                .data(livreurs)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les livreurs par zone",
            description = "Récupérer tous les livreurs assignés à une zone spécifique"
    )
    @GetMapping("/zone/{zoneId}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_LIVREURS')")
    public ResponseEntity<ApiResponse<List<LivreurSimpleResponseDto>>> getByZone(
            @Parameter(description = "ID de la zone", required = true, example = "zone-001")
            @PathVariable String zoneId) {
        List<LivreurSimpleResponseDto> livreurs = livreurService.getByZone(zoneId);

        ApiResponse<List<LivreurSimpleResponseDto>> response = ApiResponse.<List<LivreurSimpleResponseDto>>builder()
                .success(true)
                .message("Livreurs de la zone récupérés avec succès")
                .data(livreurs)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher des livreurs par mot-clé",
            description = "Rechercher des livreurs par mot-clé (nom, prénom, email, téléphone, etc.)"
    )
    @GetMapping("/search/keyword")
    @PreAuthorize("hasAuthority('CAN_MANAGE_LIVREURS')")
    public ResponseEntity<ApiResponse<List<LivreurSimpleResponseDto>>> searchByKeyword(
            @Parameter(description = "Mot-clé de recherche", required = true, example = "dupont@gmail.com")
            @RequestParam String keyword) {
        List<LivreurSimpleResponseDto> livreurs = livreurService.searchByKeyword(keyword);

        ApiResponse<List<LivreurSimpleResponseDto>> response = ApiResponse.<List<LivreurSimpleResponseDto>>builder()
                .success(true)
                .message("Recherche par mot-clé effectuée avec succès")
                .data(livreurs)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Supprimer un livreur",
            description = "Supprimer définitivement un livreur du système"
    )
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CAN_MANAGE_LIVREURS')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID du livreur à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        livreurService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Livreur supprimé avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

}