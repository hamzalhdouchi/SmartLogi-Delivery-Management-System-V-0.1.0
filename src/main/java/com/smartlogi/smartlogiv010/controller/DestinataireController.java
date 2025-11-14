package com.smartlogi.smartlogiv010.controller;

import com.smartlogi.smartlogiv010.apiResponse.ApiResponseDTO;
import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.DestinataireCreateDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.DestinataireUpdateDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Destinataire.DestinataireSimpleResponseDto;
import com.smartlogi.smartlogiv010.exception.EmailAlreadyExistsException;
import com.smartlogi.smartlogiv010.service.DestinataireServiceImpl;
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

@RestController
@RequestMapping("/api/destinataires")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestion des Destinataires", description = "API pour la gestion des destinataires (création, modification, recherche)")
public class DestinataireController {

    private final DestinataireServiceImpl destinataireService;

    @Operation(
            summary = "Créer un nouveau destinataire",
            description = "Créer un destinataire avec vérification de l'unicité de l'email"
    )
    @PostMapping
    public ResponseEntity<ApiResponseDTO<DestinataireSimpleResponseDto>> create(
            @Parameter(description = "Données du destinataire à créer", required = true)
            @Valid @RequestBody DestinataireCreateDto requestDto) throws EmailAlreadyExistsException {
        DestinataireSimpleResponseDto createdDestinataire = destinataireService.create(requestDto);

        ApiResponseDTO<DestinataireSimpleResponseDto> response = ApiResponseDTO.<DestinataireSimpleResponseDto>builder()
                .success(true)
                .message("Destinataire créé avec succès")
                .data(createdDestinataire)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Modifier un destinataire",
            description = "Mettre à jour les informations d'un destinataire existant"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<DestinataireSimpleResponseDto>> update(
            @Parameter(description = "ID du destinataire", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id,
            @Parameter(description = "Données de mise à jour du destinataire", required = true)
            @Valid @RequestBody DestinataireUpdateDto requestDto) {
        DestinataireSimpleResponseDto updatedDestinataire = destinataireService.update(id, requestDto);

        ApiResponseDTO<DestinataireSimpleResponseDto> response = ApiResponseDTO.<DestinataireSimpleResponseDto>builder()
                .success(true)
                .message("Destinataire mis à jour avec succès")
                .data(updatedDestinataire)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir un destinataire par ID",
            description = "Récupérer les informations complètes d'un destinataire spécifique"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<DestinataireSimpleResponseDto>> getById(
            @Parameter(description = "ID du destinataire", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        DestinataireSimpleResponseDto destinataire = destinataireService.getById(id);

        ApiResponseDTO<DestinataireSimpleResponseDto> response = ApiResponseDTO.<DestinataireSimpleResponseDto>builder()
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
    public ResponseEntity<ApiResponseDTO<Page<DestinataireSimpleResponseDto>>> getAll(
            @Parameter(description = "Paramètres de pagination et de tri")
            Pageable pageable) {
        Page<DestinataireSimpleResponseDto> destinataires = destinataireService.getAll(pageable);

        ApiResponseDTO<Page<DestinataireSimpleResponseDto>> response = ApiResponseDTO.<Page<DestinataireSimpleResponseDto>>builder()
                .success(true)
                .message("Liste des destinataires récupérée avec succès")
                .data(destinataires)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher des destinataires par nom",
            description = "Rechercher des destinataires par leur nom (recherche partielle)"
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<DestinataireSimpleResponseDto>>> searchByNom(
            @Parameter(description = "Nom ou partie du nom à rechercher", required = true, example = "Dupont")
            @RequestParam String nom) {
        List<DestinataireSimpleResponseDto> destinataires = destinataireService.searchByNom(nom);

        ApiResponseDTO<List<DestinataireSimpleResponseDto>> response = ApiResponseDTO.<List<DestinataireSimpleResponseDto>>builder()
                .success(true)
                .message("Recherche de destinataires par nom effectuée avec succès")
                .data(destinataires)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher un destinataire par mot-clé",
            description = "Rechercher un destinataire par mot-clé (nom, email, téléphone, etc.)"
    )
    @GetMapping("/search-keyword")
    public ResponseEntity<ApiResponseDTO<DestinataireSimpleResponseDto>> findByKeyWord(
            @Parameter(description = "Mot-clé de recherche", required = true, example = "dupont@gmail.com")
            @RequestParam String keyword) {
        DestinataireSimpleResponseDto destinataire = destinataireService.findByKeyWord(keyword);

        ApiResponseDTO<DestinataireSimpleResponseDto> response = ApiResponseDTO.<DestinataireSimpleResponseDto>builder()
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
    public ResponseEntity<ApiResponseDTO<Void>> delete(
            @Parameter(description = "ID du destinataire à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        destinataireService.delete(id);

        ApiResponseDTO<Void> response = ApiResponseDTO.<Void>builder()
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
    public ResponseEntity<ApiResponseDTO<Boolean>> existsById(
            @Parameter(description = "ID du destinataire à vérifier", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        boolean exists = destinataireService.existsById(id);

        String message = exists ?
                "Le destinataire existe" :
                "Le destinataire n'existe pas";

        ApiResponseDTO<Boolean> response = ApiResponseDTO.<Boolean>builder()
                .success(true)
                .message(message)
                .data(exists)
                .build();

        return ResponseEntity.ok(response);
    }
}