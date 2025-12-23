package com.smartlogi.smartlogiv010.controller;

import com.smartlogi.security.dto.authDto.response.UserResponse;
import com.smartlogi.smartlogiv010.apiResponse.ApiResponse;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ClientExpediteurUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
import com.smartlogi.smartlogiv010.service.clientExpsditeurInterfaceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
            summary = "Modifier un client expéditeur",
            description = "Mettre à jour les informations d'un client expéditeur existant"
    )
    @PutMapping("/{id}/update")
    @PreAuthorize("hasRole('ROLE_SENDER')")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @Parameter(description = "ID du client expéditeur", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable("id") String id,
            @Parameter(description = "Données de mise à jour du client", required = true)
            @Valid @RequestBody ClientExpediteurUpdateRequestDto requestDto) {
        UserResponse updatedClient = clientExpediteurService.update(id, requestDto);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
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
    @GetMapping("/{id}/getClient")
    @PreAuthorize("hasAuthority('CAN_READ_OWN_COLIS')")
    public ResponseEntity<ApiResponse<UserResponse>> getById(
            @Parameter(description = "ID du client expéditeur", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        UserResponse client = clientExpediteurService.getById(id);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
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
    @PreAuthorize("hasAuthority('CAN_MANAGE_SENDERS')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAll(
            @Parameter(description = "Paramètres de pagination et de tri")
            Pageable pageable) {
        Page<UserResponse> clients = clientExpediteurService.getAll(pageable);

        ApiResponse<Page<UserResponse>> response = ApiResponse.<Page<UserResponse>>builder()
                .success(true)
                .message("Liste des clients expéditeurs récupérée avec succès")
                .data(clients)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher un client par mot-clé",
            description = "Rechercher un client expéditeur par mot-clé (nom, email, téléphone, etc.)"
    )
    @GetMapping("/search-keyword")
    @PreAuthorize("hasAuthority('CAN_MANAGE_SENDERS')")
    public ResponseEntity<ApiResponse<UserResponse>> findByKeyWord(
            @Parameter(description = "Mot-clé de recherche", required = true, example = "dupont@gmail.com")
            @RequestParam String keyword) {
        UserResponse clientExpediteurSimpleResponseDto = clientExpediteurService.findByKeyWord(keyword);

        ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
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
    @PreAuthorize("hasAuthority('CAN_MANAGE_SENDERS')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID du client expéditeur à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        clientExpediteurService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Client expéditeur supprimé avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

}