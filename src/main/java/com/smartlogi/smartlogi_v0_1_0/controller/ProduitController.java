package com.smartlogi.smartlogi_v0_1_0.controller;

import com.smartlogi.smartlogi_v0_1_0.apiResponse.ApiResponseDTO;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ProduitCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ProduitUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.service.interfaces.ProduitService;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestion des Produits", description = "API pour la gestion complète des produits (création, modification, recherche, statistiques)")
public class ProduitController {

    private final ProduitService produitService;

    @Operation(
            summary = "Créer un nouveau produit",
            description = "Ajouter un nouveau produit dans le catalogue avec ses caractéristiques (nom, catégorie, poids, prix)"
    )
    @PostMapping
    public ResponseEntity<ApiResponseDTO<ProduitSimpleResponseDto>> create(
            @Parameter(description = "Données du produit à créer", required = true)
            @Valid @RequestBody ProduitCreateRequestDto requestDto) {
        ProduitSimpleResponseDto createdProduit = produitService.create(requestDto);

        ApiResponseDTO<ProduitSimpleResponseDto> response = ApiResponseDTO.<ProduitSimpleResponseDto>builder()
                .success(true)
                .message("Produit créé avec succès")
                .data(createdProduit)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Modifier un produit",
            description = "Mettre à jour les informations d'un produit existant"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProduitSimpleResponseDto>> update(
            @Parameter(description = "ID du produit", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable("id") String id,
            @Parameter(description = "Données de mise à jour du produit", required = true)
            @Valid @RequestBody ProduitUpdateRequestDto requestDto) {
        ProduitSimpleResponseDto updatedProduit = produitService.update(id, requestDto);

        ApiResponseDTO<ProduitSimpleResponseDto> response = ApiResponseDTO.<ProduitSimpleResponseDto>builder()
                .success(true)
                .message("Produit mis à jour avec succès")
                .data(updatedProduit)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir un produit par ID",
            description = "Récupérer les informations de base d'un produit spécifique"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<ProduitSimpleResponseDto>> getById(
            @Parameter(description = "ID du produit", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        ProduitSimpleResponseDto produit = produitService.getById(id);

        ApiResponseDTO<ProduitSimpleResponseDto> response = ApiResponseDTO.<ProduitSimpleResponseDto>builder()
                .success(true)
                .message("Produit récupéré avec succès")
                .data(produit)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir un produit avec statistiques",
            description = "Récupérer les informations détaillées d'un produit incluant ses statistiques d'utilisation"
    )
    @GetMapping("/{id}/advanced")
    public ResponseEntity<ApiResponseDTO<ProduitAdvancedResponseDto>> getByIdWithStats(
            @Parameter(description = "ID du produit", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        ProduitAdvancedResponseDto produit = produitService.getByIdWithStats(id);

        ApiResponseDTO<ProduitAdvancedResponseDto> response = ApiResponseDTO.<ProduitAdvancedResponseDto>builder()
                .success(true)
                .message("Produit avec statistiques récupéré avec succès")
                .data(produit)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir un produit avec ses colis",
            description = "Récupérer les informations complètes d'un produit incluant la liste des colis où il est utilisé"
    )
    @GetMapping("/{id}/detailed")
    public ResponseEntity<ApiResponseDTO<ProduitDetailedResponseDto>> getByIdWithColis(
            @Parameter(description = "ID du produit", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        ProduitDetailedResponseDto produit = produitService.getByIdWithColis(id);

        ApiResponseDTO<ProduitDetailedResponseDto> response = ApiResponseDTO.<ProduitDetailedResponseDto>builder()
                .success(true)
                .message("Produit avec colis récupéré avec succès")
                .data(produit)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister tous les produits",
            description = "Récupérer la liste complète de tous les produits du catalogue"
    )
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<ProduitSimpleResponseDto>>> getAll() {
        List<ProduitSimpleResponseDto> produits = produitService.getAll();

        ApiResponseDTO<List<ProduitSimpleResponseDto>> response = ApiResponseDTO.<List<ProduitSimpleResponseDto>>builder()
                .success(true)
                .message("Liste des produits récupérée avec succès")
                .data(produits)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister les produits paginés",
            description = "Récupérer les produits avec pagination, tri et filtres"
    )
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponseDTO<Page<ProduitSimpleResponseDto>>> getAllPaginated(
            @Parameter(description = "Paramètres de pagination et de tri")
            Pageable pageable) {
        Page<ProduitSimpleResponseDto> produits = produitService.getAll(pageable);

        ApiResponseDTO<Page<ProduitSimpleResponseDto>> response = ApiResponseDTO.<Page<ProduitSimpleResponseDto>>builder()
                .success(true)
                .message("Produits paginés récupérés avec succès")
                .data(produits)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher des produits par nom",
            description = "Rechercher des produits par leur nom (recherche partielle)"
    )
    @GetMapping("/search/nom")
    public ResponseEntity<ApiResponseDTO<List<ProduitSimpleResponseDto>>> searchByNom(
            @Parameter(description = "Nom ou partie du nom à rechercher", required = true, example = "Smartphone")
            @RequestParam String nom) {
        List<ProduitSimpleResponseDto> produits = produitService.searchByNom(nom);

        ApiResponseDTO<List<ProduitSimpleResponseDto>> response = ApiResponseDTO.<List<ProduitSimpleResponseDto>>builder()
                .success(true)
                .message("Recherche par nom effectuée avec succès")
                .data(produits)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher des produits par catégorie",
            description = "Récupérer tous les produits d'une catégorie spécifique"
    )
    @GetMapping("/categorie/{categorie}")
    public ResponseEntity<ApiResponseDTO<List<ProduitSimpleResponseDto>>> getByCategorie(
            @Parameter(description = "Catégorie des produits", required = true, example = "Électronique")
            @PathVariable String categorie) {
        List<ProduitSimpleResponseDto> produits = produitService.getByCategorie(categorie);

        ApiResponseDTO<List<ProduitSimpleResponseDto>> response = ApiResponseDTO.<List<ProduitSimpleResponseDto>>builder()
                .success(true)
                .message("Produits par catégorie récupérés avec succès")
                .data(produits)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher des produits par mot-clé",
            description = "Rechercher des produits par mot-clé (nom, catégorie, description, etc.)"
    )
    @GetMapping("/search/keyword")
    public ResponseEntity<ApiResponseDTO<List<ProduitSimpleResponseDto>>> searchByKeyword(
            @Parameter(description = "Mot-clé de recherche", required = true, example = "samsung")
            @RequestParam String keyword) {
        List<ProduitSimpleResponseDto> produits = produitService.searchByKeyword(keyword);

        ApiResponseDTO<List<ProduitSimpleResponseDto>> response = ApiResponseDTO.<List<ProduitSimpleResponseDto>>builder()
                .success(true)
                .message("Recherche par mot-clé effectuée avec succès")
                .data(produits)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher des produits par plage de prix",
            description = "Récupérer les produits dont le prix est compris dans une plage spécifique"
    )
    @GetMapping("/prix/range")
    public ResponseEntity<ApiResponseDTO<List<ProduitSimpleResponseDto>>> getByPrixBetween(
            @Parameter(description = "Prix minimum", required = true, example = "100.00")
            @RequestParam BigDecimal prixMin,
            @Parameter(description = "Prix maximum", required = true, example = "500.00")
            @RequestParam BigDecimal prixMax) {
        List<ProduitSimpleResponseDto> produits = produitService.getByPrixBetween(prixMin, prixMax);

        ApiResponseDTO<List<ProduitSimpleResponseDto>> response = ApiResponseDTO.<List<ProduitSimpleResponseDto>>builder()
                .success(true)
                .message("Produits par plage de prix récupérés avec succès")
                .data(produits)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir toutes les catégories",
            description = "Récupérer la liste de toutes les catégories de produits disponibles"
    )
    @GetMapping("/categories")
    public ResponseEntity<ApiResponseDTO<List<String>>> getAllCategories() {
        List<String> categories = produitService.getAllCategories();

        ApiResponseDTO<List<String>> response = ApiResponseDTO.<List<String>>builder()
                .success(true)
                .message("Liste des catégories récupérée avec succès")
                .data(categories)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Supprimer un produit",
            description = "Supprimer définitivement un produit du catalogue"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<Void>> delete(
            @Parameter(description = "ID du produit à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        produitService.delete(id);

        ApiResponseDTO<Void> response = ApiResponseDTO.<Void>builder()
                .success(true)
                .message("Produit supprimé avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Vérifier l'existence d'un produit",
            description = "Vérifier si un produit existe dans le catalogue par son ID"
    )
    @GetMapping("/{id}/exists")
    public ResponseEntity<ApiResponseDTO<Boolean>> existsById(
            @Parameter(description = "ID du produit à vérifier", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        boolean exists = produitService.existsById(id);

        String message = exists ?
                "Le produit existe" :
                "Le produit n'existe pas";

        ApiResponseDTO<Boolean> response = ApiResponseDTO.<Boolean>builder()
                .success(true)
                .message(message)
                .data(exists)
                .build();

        return ResponseEntity.ok(response);
    }
}