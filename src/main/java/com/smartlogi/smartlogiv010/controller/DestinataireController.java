package com.smartlogi.smartlogiv010.controller;

import com.smartlogi.security.dto.authDto.response.UserResponse;
import com.smartlogi.smartlogiv010.apiResponse.ApiResponse;
import com.smartlogi.smartlogiv010.dto.responseDTO.Destinataire.DestinataireSimpleResponseDto;
import com.smartlogi.smartlogiv010.service.DestinataireServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinataires")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestion des Destinataires", description = "API pour la gestion des destinataires (création, modification, recherche)")
@PreAuthorize("hasRole('ROLE_MANAGER')")
public class DestinataireController {

    private final DestinataireServiceImpl destinataireService;


    @Operation(
            summary = "Obtenir un destinataire par ID",
            description = "Récupérer les informations complètes d'un destinataire spécifique"
    )
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<UserResponse>> getById(
            @Parameter(description = "ID du destinataire", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        UserResponse destinataire = destinataireService.getById(id);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Destinataire récupéré avec succès")
                .data(destinataire)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister tous les destinataires",
            description = "Obtenir la liste paginée de tous les destinataires avec possibilité de tri et de pagination"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAll(
            @Parameter(description = "Paramètres de pagination et de tri")
            Pageable pageable) {
        Page<UserResponse> destinataires = destinataireService.getAll(pageable);

        ApiResponse<Page<UserResponse>> response = ApiResponse.<Page<UserResponse>>builder()
                .success(true)
                .message("Liste des destinataires récupérée avec succès")
                .data(destinataires)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher un destinataire par mot-clé",
            description = "Rechercher un destinataire par mot-clé (nom, email, téléphone, etc.)"
    )
    @GetMapping("/search-keyword")
    public ResponseEntity<ApiResponse<UserResponse>> findByKeyWord(
            @Parameter(description = "Mot-clé de recherche", required = true, example = "dupont@gmail.com")
            @RequestParam String keyword) {
        UserResponse destinataire = destinataireService.findByKeyWord(keyword);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                .success(true)
                .message("Destinataire trouvé avec succès")
                .data(destinataire)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Supprimer un destinataire",
            description = "Supprimer définitivement un destinataire du système"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID du destinataire à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        destinataireService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Destinataire supprimé avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Vérifier l'existence d'un destinataire",
            description = "Vérifier si un destinataire existe dans le système par son ID"
    )
    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponse<Boolean>> existsById(
            @Parameter(description = "ID du destinataire à vérifier", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        boolean exists = destinataireService.existsById(id);

        String message = exists ?
                "Le destinataire existe" :
                "Le destinataire n'existe pas";

        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .success(true)
                .message(message)
                .data(exists)
                .build();

        return ResponseEntity.ok(response);
    }
}