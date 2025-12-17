package com.smartlogi.smartlogiv010.controller;

import com.smartlogi.smartlogiv010.apiResponse.ApiResponse;
import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ZoneCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ZoneUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Zone.ZoneDetailedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Zone.ZoneSimpleResponseDto;
import com.smartlogi.smartlogiv010.service.interfaces.ZoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestion des Zones", description = "API pour la gestion des zones de livraison (création, modification, recherche, statistiques)")
public class ZoneController {

    private final ZoneService zoneService;

    @Operation(
            summary = "Créer une nouvelle zone",
            description = "Ajouter une nouvelle zone de livraison avec ses informations géographiques et opérationnelles"
    )
    @PostMapping
    public ResponseEntity<ApiResponse<ZoneSimpleResponseDto>> create(
            @Parameter(description = "Données de la zone à créer", required = true)
            @Valid @RequestBody ZoneCreateRequestDto requestDto) {
        ZoneSimpleResponseDto createdZone = zoneService.create(requestDto);

        ApiResponse<ZoneSimpleResponseDto> response = ApiResponse.<ZoneSimpleResponseDto>builder()
                .success(true)
                .message("Zone créée avec succès")
                .data(createdZone)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Modifier une zone",
            description = "Mettre à jour les informations d'une zone existante"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ZoneSimpleResponseDto>> update(
            @Parameter(description = "ID de la zone", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id,
            @Parameter(description = "Données de mise à jour de la zone", required = true)
            @Valid @RequestBody ZoneUpdateRequestDto requestDto) {
        ZoneSimpleResponseDto updatedZone = zoneService.update(id, requestDto);

        ApiResponse<ZoneSimpleResponseDto> response = ApiResponse.<ZoneSimpleResponseDto>builder()
                .success(true)
                .message("Zone mise à jour avec succès")
                .data(updatedZone)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir une zone par ID",
            description = "Récupérer les informations de base d'une zone spécifique"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ZoneSimpleResponseDto>> getById(
            @Parameter(description = "ID de la zone", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        ZoneSimpleResponseDto zone = zoneService.getById(id);

        ApiResponse<ZoneSimpleResponseDto> response = ApiResponse.<ZoneSimpleResponseDto>builder()
                .success(true)
                .message("Zone récupérée avec succès")
                .data(zone)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir une zone avec détails complets",
            description = "Récupérer les informations détaillées d'une zone incluant ses livreurs et colis associés"
    )
    @GetMapping("/{id}/detailed")
    public ResponseEntity<ApiResponse<ZoneDetailedResponseDto>> getByIdWithDetails(
            @Parameter(description = "ID de la zone", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        ZoneDetailedResponseDto zone = zoneService.getByIdWithDetails(id);

        ApiResponse<ZoneDetailedResponseDto> response = ApiResponse.<ZoneDetailedResponseDto>builder()
                .success(true)
                .message("Zone détaillée récupérée avec succès")
                .data(zone)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister toutes les zones",
            description = "Récupérer la liste complète de toutes les zones de livraison"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<ZoneSimpleResponseDto>>> getAll() {
        List<ZoneSimpleResponseDto> zones = zoneService.getAll();

        ApiResponse<List<ZoneSimpleResponseDto>> response = ApiResponse.<List<ZoneSimpleResponseDto>>builder()
                .success(true)
                .message("Liste des zones récupérée avec succès")
                .data(zones)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister les zones paginées",
            description = "Récupérer les zones avec pagination, tri et filtres"
    )
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<ZoneSimpleResponseDto>>> getAllPaginated(
            @Parameter(description = "Paramètres de pagination et de tri")
            Pageable pageable) {
        Page<ZoneSimpleResponseDto> zones = zoneService.getAll(pageable);

        ApiResponse<Page<ZoneSimpleResponseDto>> response = ApiResponse.<Page<ZoneSimpleResponseDto>>builder()
                .success(true)
                .message("Zones paginées récupérées avec succès")
                .data(zones)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir une zone par code postal",
            description = "Rechercher une zone spécifique par son code postal"
    )
    @GetMapping("/code-postal/{codePostal}")
    public ResponseEntity<ApiResponse<ZoneSimpleResponseDto>> getByCodePostal(
            @Parameter(description = "Code postal de la zone", required = true, example = "20000")
            @PathVariable String codePostal) {
        Optional<ZoneSimpleResponseDto> zone = zoneService.getByCodePostal(codePostal);

        if (zone.isPresent()) {
            ApiResponse<ZoneSimpleResponseDto> response = ApiResponse.<ZoneSimpleResponseDto>builder()
                    .success(true)
                    .message("Zone trouvée par code postal")
                    .data(zone.get())
                    .build();
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<ZoneSimpleResponseDto> response = ApiResponse.<ZoneSimpleResponseDto>builder()
                    .success(false)
                    .message("Aucune zone trouvée avec ce code postal")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(
            summary = "Obtenir une zone par nom",
            description = "Rechercher une zone spécifique par son nom exact"
    )
    @GetMapping("/nom/{nom}")
    public ResponseEntity<ApiResponse<ZoneSimpleResponseDto>> getByNom(
            @Parameter(description = "Nom exact de la zone", required = true, example = "Casablanca Centre")
            @PathVariable String nom) {
        Optional<ZoneSimpleResponseDto> zone = zoneService.getByNom(nom);

        if (zone.isPresent()) {
            ApiResponse<ZoneSimpleResponseDto> response = ApiResponse.<ZoneSimpleResponseDto>builder()
                    .success(true)
                    .message("Zone trouvée par nom")
                    .data(zone.get())
                    .build();
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<ZoneSimpleResponseDto> response = ApiResponse.<ZoneSimpleResponseDto>builder()
                    .success(false)
                    .message("Aucune zone trouvée avec ce nom")
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @Operation(
            summary = "Rechercher des zones par nom",
            description = "Rechercher des zones par leur nom (recherche partielle)"
    )
    @GetMapping("/search/nom")
    public ResponseEntity<ApiResponse<List<ZoneSimpleResponseDto>>> searchByNom(
            @Parameter(description = "Nom ou partie du nom à rechercher", required = true, example = "casa")
            @RequestParam String nom) {
        List<ZoneSimpleResponseDto> zones = zoneService.searchByNom(nom);

        ApiResponse<List<ZoneSimpleResponseDto>> response = ApiResponse.<List<ZoneSimpleResponseDto>>builder()
                .success(true)
                .message("Recherche par nom effectuée avec succès")
                .data(zones)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher des zones par mot-clé",
            description = "Rechercher des zones par mot-clé (nom, ville, code postal, etc.)"
    )
    @GetMapping("/search/keyword")
    public ResponseEntity<ApiResponse<List<ZoneSimpleResponseDto>>> searchByKeyword(
            @Parameter(description = "Mot-clé de recherche", required = true, example = "20000")
            @RequestParam String keyword) {
        List<ZoneSimpleResponseDto> zones = zoneService.searchByKeyword(keyword);

        ApiResponse<List<ZoneSimpleResponseDto>> response = ApiResponse.<List<ZoneSimpleResponseDto>>builder()
                .success(true)
                .message("Recherche par mot-clé effectuée avec succès")
                .data(zones)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Vérifier l'existence d'un code postal",
            description = "Vérifier si un code postal est déjà associé à une zone"
    )
    @GetMapping("/code-postal/{codePostal}/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsByCodePostal(
            @Parameter(description = "Code postal à vérifier", required = true, example = "20000")
            @PathVariable String codePostal) {
        boolean exists = zoneService.existsByCodePostal(codePostal);

        String message = exists ?
                "Une zone existe avec ce code postal" :
                "Aucune zone avec ce code postal";

        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .success(true)
                .message(message)
                .data(exists)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Supprimer une zone",
            description = "Supprimer définitivement une zone du système"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID de la zone à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        zoneService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Zone supprimée avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Vérifier l'existence d'une zone",
            description = "Vérifier si une zone existe dans le système par son ID"
    )
    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsById(
            @Parameter(description = "ID de la zone à vérifier", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        boolean exists = zoneService.existsById(id);

        String message = exists ?
                "La zone existe" :
                "La zone n'existe pas";

        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .success(true)
                .message(message)
                .data(exists)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Vérifier l'existence d'une zone par nom",
            description = "Vérifier si une zone avec un nom spécifique existe déjà"
    )
    @GetMapping("/nom/{nom}/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsByNom(
            @Parameter(description = "Nom de la zone à vérifier", required = true, example = "Casablanca Centre")
            @PathVariable String nom) {
        boolean exists = zoneService.existsByNom(nom);

        String message = exists ?
                "Une zone existe avec ce nom" :
                "Aucune zone avec ce nom";

        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .success(true)
                .message(message)
                .data(exists)
                .build();

        return ResponseEntity.ok(response);
    }
}