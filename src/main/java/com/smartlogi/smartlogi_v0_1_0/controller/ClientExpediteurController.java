package com.smartlogi.smartlogi_v0_1_0.controller;

import com.smartlogi.smartlogi_v0_1_0.apiResponse.ApiResponseDTO;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ClientExpediteurCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ClientExpediteurUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.service.clientExpsditeurInterfaceImpl;
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
@RequestMapping("/api/clients-expediteurs")
@RequiredArgsConstructor
@Validated
@Tag(name = "Clients Expéditeurs", description = "API pour la gestion des clients expéditeurs")
public class ClientExpediteurController {

    private final clientExpsditeurInterfaceImpl clientExpediteurService;

    @Operation(
            summary = "Créer un client expéditeur",
            description = "Créer un nouveau client expéditeur dans le système"
    )
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ClientExpediteurSimpleResponseDto>> create(
            @Parameter(description = "Données du client expéditeur à créer", required = true)
            @Valid @RequestBody ClientExpediteurCreateRequestDto requestDto) {
        ClientExpediteurSimpleResponseDto createdClient = clientExpediteurService.create(requestDto);

        ApiResponseDTO<ClientExpediteurSimpleResponseDto> response = ApiResponseDTO.<ClientExpediteurSimpleResponseDto>builder()
                .success(true)
                .message("Client expéditeur créé avec succès")
                .data(createdClient)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Modifier un client expéditeur",
            description = "Mettre à jour les informations d'un client expéditeur existant"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ClientExpediteurSimpleResponseDto>> update(
            @Parameter(description = "ID du client expéditeur", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable("id") String id,
            @Parameter(description = "Données de mise à jour du client", required = true)
            @Valid @RequestBody ClientExpediteurUpdateRequestDto requestDto) {
        ClientExpediteurSimpleResponseDto updatedClient = clientExpediteurService.update(id, requestDto);

        ApiResponseDTO<ClientExpediteurSimpleResponseDto> response = ApiResponseDTO.<ClientExpediteurSimpleResponseDto>builder()
                .success(true)
                .message("Client expéditeur mis à jour avec succès")
                .data(updatedClient)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir un client expéditeur par ID",
            description = "Récupérer les détails complets d'un client expéditeur spécifique"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ClientExpediteurSimpleResponseDto>> getById(
            @Parameter(description = "ID du client expéditeur", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        ClientExpediteurSimpleResponseDto client = clientExpediteurService.getById(id);

        ApiResponseDTO<ClientExpediteurSimpleResponseDto> response = ApiResponseDTO.<ClientExpediteurSimpleResponseDto>builder()
                .success(true)
                .message("Client expéditeur récupéré avec succès")
                .data(client)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister tous les clients expéditeurs",
            description = "Obtenir la liste paginée de tous les clients expéditeurs avec possibilité de tri et de pagination"
    )
    @GetMapping
    public ResponseEntity<ApiResponseDTO<Page<ClientExpediteurSimpleResponseDto>>> getAll(
            @Parameter(description = "Paramètres de pagination et de tri")
            Pageable pageable) {
        Page<ClientExpediteurSimpleResponseDto> clients = clientExpediteurService.getAll(pageable);

        ApiResponseDTO<Page<ClientExpediteurSimpleResponseDto>> response = ApiResponseDTO.<Page<ClientExpediteurSimpleResponseDto>>builder()
                .success(true)
                .message("Liste des clients expéditeurs récupérée avec succès")
                .data(clients)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher des clients par nom",
            description = "Rechercher des clients expéditeurs par leur nom (recherche partielle)"
    )
    @GetMapping("/search")
    public ResponseEntity<ApiResponseDTO<List<ClientExpediteurSimpleResponseDto>>> searchByNom(
            @Parameter(description = "Nom ou partie du nom à rechercher", required = true, example = "Dupont")
            @RequestParam String nom) {
        List<ClientExpediteurSimpleResponseDto> clients = clientExpediteurService.searchByNom(nom);

        ApiResponseDTO<List<ClientExpediteurSimpleResponseDto>> response = ApiResponseDTO.<List<ClientExpediteurSimpleResponseDto>>builder()
                .success(true)
                .message("Recherche de clients expéditeurs effectuée avec succès")
                .data(clients)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher un client par mot-clé",
            description = "Rechercher un client expéditeur par mot-clé (nom, email, téléphone, etc.)"
    )
    @GetMapping("/search-keyword")
    public ResponseEntity<ApiResponseDTO<ClientExpediteurSimpleResponseDto>> findByKeyWord(
            @Parameter(description = "Mot-clé de recherche", required = true, example = "dupont@gmail.com")
            @RequestParam String keyword) {
        ClientExpediteurSimpleResponseDto clientExpediteurSimpleResponseDto = clientExpediteurService.findByKeyWord(keyword);

        ApiResponseDTO<ClientExpediteurSimpleResponseDto> response = ApiResponseDTO.<ClientExpediteurSimpleResponseDto>builder()
                .success(true)
                .message("Client expéditeur trouvé avec succès")
                .data(clientExpediteurSimpleResponseDto)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Supprimer un client expéditeur",
            description = "Supprimer définitivement un client expéditeur du système"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(
            @Parameter(description = "ID du client expéditeur à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        clientExpediteurService.delete(id);

        ApiResponseDTO<Void> response = ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Client expéditeur supprimé avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Vérifier l'existence d'un client",
            description = "Vérifier si un client expéditeur existe dans le système par son ID"
    )
    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponseDTO<Boolean>> existsById(
            @Parameter(description = "ID du client expéditeur à vérifier", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        boolean exists = clientExpediteurService.existsById(id);

        String message = exists ?
                "Le client expéditeur existe" :
                "Le client expéditeur n'existe pas";

        ApiResponseDTO<Boolean> response = ApiResponseDTO.<Boolean>builder()
                .success(true)
                .message(message)
                .data(exists)
                .build();

        return ResponseEntity.ok(response);
    }
}