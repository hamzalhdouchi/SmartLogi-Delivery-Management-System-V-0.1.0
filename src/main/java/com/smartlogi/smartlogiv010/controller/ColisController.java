package com.smartlogi.smartlogiv010.controller;

import com.smartlogi.smartlogiv010.apiResponse.ApiResponse;
import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ColisCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ColisUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisSimpleResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.PoidsParLivreur.PoidsParLivreurDTO;
import com.smartlogi.smartlogiv010.dto.responseDTO.PoidsParLivreur.PoidsParLivreurDetailDTO;
import com.smartlogi.smartlogiv010.entity.ColisProduit;
import com.smartlogi.smartlogiv010.enums.Priorite;
import com.smartlogi.smartlogiv010.enums.StatutColis;
import com.smartlogi.smartlogiv010.service.interfaces.ColisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
@Validated
@Tag(name = "Gestion des Colis", description = "API pour la gestion complète des colis (création, suivi, statistiques)")
public class ColisController {

    private final ColisService colisService;

    @Operation(
            summary = "Créer un nouveau colis",
            description = "Créer un colis avec possibilité d'ajouter des produits existants ou de créer de nouveaux produits"
    )
    @PostMapping("/demande-livraison")
    public ResponseEntity<ApiResponse<ColisSimpleResponseDto>> create(
            @Parameter(description = "Données du colis à créer", required = true)
            @Valid @RequestBody ColisCreateRequestDto requestDto) {
        ColisSimpleResponseDto createdColis = colisService.create(requestDto);

        String message = "Colis créé avec succès";
        if (requestDto.getProduits() != null && !requestDto.getProduits().isEmpty()) {
            int produitsCount = requestDto.getProduits().size();
            int nouveauxProduitsCount = (int) requestDto.getProduits().stream()
                    .filter(produit -> produit.getNouveauProduit() != null)
                    .count();

            message += " avec " + produitsCount + " produit(s)";
            if (nouveauxProduitsCount > 0) {
                message += " (dont " + nouveauxProduitsCount + " nouveau(x) produit(s) créé(s))";
            }
        }

        ApiResponse<ColisSimpleResponseDto> response = ApiResponse.<ColisSimpleResponseDto>builder()
                .success(true)
                .message(message)
                .data(createdColis)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Ajouter un produit à un colis",
            description = "Ajouter un produit existant ou créer un nouveau produit dans un colis existant"
    )
    @PostMapping("/{colisId}/produits")
    public ResponseEntity<ApiResponse<Void>> ajouterProduit(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId,
            @Parameter(description = "Données du produit à ajouter", required = true)
            @Valid @RequestBody ColisCreateRequestDto.ProduitColisDto produitDto) {
        colisService.ajouterProduit(colisId, produitDto);

        String message = "Produit ";
        if (produitDto.getProduitId() != null) {
            message += "existant ajouté au colis avec succès";
        } else {
            message += "créé et ajouté au colis avec succès";
        }

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .build();

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Lister les produits d'un colis",
            description = "Récupérer la liste de tous les produits associés à un colis spécifique"
    )
    @GetMapping("/{colisId}/produits")
    public ResponseEntity<ApiResponse<List<ColisProduit>>> getProduitsByColis(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId) {
        List<ColisProduit> produits = colisService.getProduitsByColis(colisId);

        ApiResponse<List<ColisProduit>> response = ApiResponse.<List<ColisProduit>>builder()
                .success(true)
                .message("Produits du colis récupérés avec succès")
                .data(produits)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Modifier la quantité d'un produit",
            description = "Mettre à jour la quantité d'un produit spécifique dans un colis"
    )
    @PutMapping("/{colisId}/produits/{produitId}/quantite")
    public ResponseEntity<ApiResponse<Void>> mettreAJourQuantiteProduit(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId,
            @Parameter(description = "ID du produit", required = true, example = "123e4567-e89b-12d3-a456-426614174001")
            @PathVariable String produitId,
            @Parameter(description = "Nouvelle quantité (minimum 1)", required = true, example = "5")
            @RequestParam @Valid @jakarta.validation.constraints.Min(value = 1, message = "La quantité doit être au moins 1") Integer nouvelleQuantite) {
        colisService.mettreAJourQuantiteProduit(colisId, produitId, nouvelleQuantite);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Quantité du produit mise à jour avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Supprimer un produit d'un colis",
            description = "Retirer un produit spécifique d'un colis"
    )
    @DeleteMapping("/{colisId}/produits/{produitId}")
    public ResponseEntity<ApiResponse<Void>> supprimerProduit(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId,
            @Parameter(description = "ID du produit", required = true, example = "123e4567-e89b-12d3-a456-426614174001")
            @PathVariable String produitId) {
        colisService.supprimerProduit(colisId, produitId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Produit retiré du colis avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Calculer le prix total d'un colis",
            description = "Obtenir le prix total d'un colis basé sur les produits et leurs quantités"
    )
    @GetMapping("/{colisId}/total-prix")
    public ResponseEntity<ApiResponse<BigDecimal>> getTotalPrixColis(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId) {
        BigDecimal totalPrix = colisService.getPrixTotalColis(colisId);

        ApiResponse<BigDecimal> response = ApiResponse.<BigDecimal>builder()
                .success(true)
                .message("Prix total du colis calculé avec succès")
                .data(totalPrix)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Vérifier l'existence d'un produit dans un colis",
            description = "Vérifier si un produit spécifique existe dans un colis"
    )
    @GetMapping("/{colisId}/produits/{produitId}/existe")
    public ResponseEntity<ApiResponse<Boolean>> produitExisteDansColis(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId,
            @Parameter(description = "ID du produit", required = true, example = "123e4567-e89b-12d3-a456-426614174001")
            @PathVariable String produitId) {
        boolean existe = colisService.produitExisteDansColis(colisId, produitId);

        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .success(true)
                .message("Vérification d'existence du produit dans le colis effectuée avec succès")
                .data(existe)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Mettre à jour un colis",
            description = "Modifier les informations d'un colis existant"
    )
    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<ColisSimpleResponseDto>> update(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable("id") String id,
            @Parameter(description = "Données de mise à jour du colis", required = true)
            @Valid @RequestBody ColisUpdateRequestDto requestDto) {
        ColisSimpleResponseDto updatedColis = colisService.update(id, requestDto);

        ApiResponse<ColisSimpleResponseDto> response = ApiResponse.<ColisSimpleResponseDto>builder()
                .success(true)
                .message("Colis mis à jour avec succès")
                .data(updatedColis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir un colis par ID",
            description = "Récupérer les informations de base d'un colis spécifique"
    )
    @GetMapping("/{id}/getColis")
    public ResponseEntity<ApiResponse<ColisSimpleResponseDto>> getById(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        ColisSimpleResponseDto colis = colisService.getById(id);

        ApiResponse<ColisSimpleResponseDto> response = ApiResponse.<ColisSimpleResponseDto>builder()
                .success(true)
                .message("Colis récupéré avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir les détails complets d'un colis",
            description = "Récupérer toutes les informations détaillées d'un colis (historique, produits, etc.)"
    )
    @GetMapping("/{id}/detailed")
    public ResponseEntity<ApiResponse<ColisAdvancedResponseDto>> getByIdWithDetails(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        ColisAdvancedResponseDto colis = colisService.getByIdWithDetails(id);

        ApiResponse<ColisAdvancedResponseDto> response = ApiResponse.<ColisAdvancedResponseDto>builder()
                .success(true)
                .message("Colis détaillé récupéré avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister tous les colis",
            description = "Récupérer la liste complète de tous les colis"
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getAll() {
        List<ColisSimpleResponseDto> colis = colisService.getAll(Pageable.unpaged()).getContent();

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Liste des colis récupérée avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Lister les colis paginés",
            description = "Récupérer les colis avec pagination, tri et filtres"
    )
    @GetMapping("/paginated")
    public ResponseEntity<ApiResponse<Page<ColisSimpleResponseDto>>> getAllPaginated(
            @Parameter(description = "Paramètres de pagination et de tri")
            Pageable pageable) {
        Page<ColisSimpleResponseDto> colis = colisService.getAll(pageable);

        ApiResponse<Page<ColisSimpleResponseDto>> response = ApiResponse.<Page<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis paginés récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par statut",
            description = "Récupérer tous les colis ayant un statut spécifique"
    )
    @GetMapping("/statut/{statut}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByStatut(
            @Parameter(description = "Statut des colis", required = true, example = "CREE")
            @PathVariable StatutColis statut) {
        List<ColisSimpleResponseDto> colis = colisService.getByStatut(statut);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par statut récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par client expéditeur",
            description = "Récupérer tous les colis d'un client expéditeur spécifique"
    )
    @GetMapping("/client-expediteur/{clientId}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByClientExpediteur(
            @Parameter(description = "ID du client expéditeur", required = true, example = "client-123")
            @PathVariable String clientId) {
        List<ColisSimpleResponseDto> colis = colisService.getByClientExpediteur(clientId);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par client expéditeur récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par destinataire",
            description = "Récupérer tous les colis destinés à un destinataire spécifique"
    )
    @GetMapping("/destinataire/{destinataireId}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByDestinataire(
            @Parameter(description = "ID du destinataire", required = true, example = "dest-456")
            @PathVariable String destinataireId) {
        List<ColisSimpleResponseDto> colis = colisService.getByDestinataire(destinataireId);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par destinataire récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par livreur",
            description = "Récupérer tous les colis assignés à un livreur spécifique"
    )
    @GetMapping("/livreur/{livreurId}")
    public ResponseEntity<ApiResponse<List<ColisAdvancedResponseDto>>> getByLivreur(
            @Parameter(description = "ID du livreur", required = true, example = "livreur-789")
            @PathVariable String livreurId) {
        List<ColisAdvancedResponseDto> colis = colisService.getByLivreur(livreurId);

        ApiResponse<List<ColisAdvancedResponseDto>> response = ApiResponse.<List<ColisAdvancedResponseDto>>builder()
                .success(true)
                .message("Colis par livreur récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par zone",
            description = "Récupérer tous les colis d'une zone spécifique"
    )
    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByZone(
            @Parameter(description = "ID de la zone", required = true, example = "zone-001")
            @PathVariable String zoneId) {
        List<ColisSimpleResponseDto> colis = colisService.getByZone(zoneId);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par zone récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par ville de destination",
            description = "Récupérer tous les colis destinés à une ville spécifique"
    )
    @GetMapping("/ville-destination/{ville}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> searchByVilleDestination(
            @Parameter(description = "Ville de destination", required = true, example = "Paris")
            @PathVariable String ville) {
        List<ColisSimpleResponseDto> colis = colisService.searchByVilleDestination(ville);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par ville de destination récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Assigner un livreur à un colis",
            description = "Assigner un livreur spécifique à un colis pour la livraison"
    )
    @PutMapping("/{colisId}/assigner-livreur/{livreurId}")
    public ResponseEntity<ApiResponse<Void>> assignerLivreur(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId,
            @Parameter(description = "ID du livreur", required = true, example = "livreur-789")
            @PathVariable String livreurId) {
        colisService.assignerLivreur(colisId, livreurId);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Livreur assigné au colis avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Changer le statut d'un colis",
            description = "Modifier le statut d'un colis avec un commentaire optionnel"
    )
    @PutMapping("/{colisId}/changer-statut")
    public ResponseEntity<ApiResponse<Void>> changerStatut(
            @Parameter(description = "ID du colis", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String colisId,
            @Parameter(description = "Nouveau statut", required = true, example = "EN_COURS")
            @RequestParam StatutColis nouveauStatut,
            @Parameter(description = "Commentaire optionnel pour le changement de statut", example = "En cours de livraison")
            @RequestParam(required = false) String commentaire) {
        colisService.changerStatut(colisId, nouveauStatut, commentaire);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Statut du colis modifié avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Supprimer un colis",
            description = "Supprimer définitivement un colis du système"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @Parameter(description = "ID du colis à supprimer", required = true, example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable String id) {
        colisService.delete(id);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .success(true)
                .message("Colis supprimé avec succès")
                .build();

        return ResponseEntity.ok(response);
    }

    // === ENDPOINTS AVANCÉS ===

    @Operation(
            summary = "Rechercher les colis par priorité",
            description = "Récupérer tous les colis ayant une priorité spécifique"
    )
    @GetMapping("/priorite/{priorite}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByPriorite(
            @Parameter(description = "Priorité des colis", required = true, example = "HAUTE")
            @PathVariable Priorite priorite) {
        List<ColisSimpleResponseDto> colis = colisService.getByPriorite(priorite);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par priorité récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par priorité et statut",
            description = "Récupérer les colis ayant une combinaison spécifique de priorité et statut"
    )
    @GetMapping("/priorite/{priorite}/statut/{statut}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByPrioriteAndStatut(
            @Parameter(description = "Priorité des colis", required = true, example = "HAUTE")
            @PathVariable Priorite priorite,
            @Parameter(description = "Statut des colis", required = true, example = "EN_COURS")
            @PathVariable StatutColis statut) {
        List<ColisSimpleResponseDto> colis = colisService.getByPrioriteAndStatut(priorite, statut);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par priorité et statut récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par livreur et statut",
            description = "Récupérer les colis d'un livreur spécifique avec un statut donné"
    )
    @GetMapping("/livreur/{livreurId}/statut/{statut}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByLivreurAndStatut(
            @Parameter(description = "ID du livreur", required = true, example = "livreur-789")
            @PathVariable String livreurId,
            @Parameter(description = "Statut des colis", required = true, example = "EN_COURS")
            @PathVariable StatutColis statut) {
        List<ColisSimpleResponseDto> colis = colisService.getByLivreurAndStatut(livreurId, statut);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par livreur et statut récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par zone et statut",
            description = "Récupérer les colis d'une zone spécifique avec un statut donné"
    )
    @GetMapping("/zone/{zoneId}/statut/{statut}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByZoneAndStatut(
            @Parameter(description = "ID de la zone", required = true, example = "zone-001")
            @PathVariable String zoneId,
            @Parameter(description = "Statut des colis", required = true, example = "CREE")
            @PathVariable StatutColis statut) {
        List<ColisSimpleResponseDto> colis = colisService.getByZoneAndStatut(zoneId, statut);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par zone et statut récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par ville destination et statut",
            description = "Récupérer les colis destinés à une ville spécifique avec un statut donné"
    )
    @GetMapping("/ville-destination/{ville}/statut/{statut}")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByVilleDestinationAndStatut(
            @Parameter(description = "Ville de destination", required = true, example = "Paris")
            @PathVariable String ville,
            @Parameter(description = "Statut des colis", required = true, example = "EN_COURS")
            @PathVariable StatutColis statut) {
        List<ColisSimpleResponseDto> colis = colisService.getByVilleDestinationAndStatut(ville, statut);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par ville destination et statut récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par mot-clé",
            description = "Rechercher des colis par mot-clé (description, ville, etc.)"
    )
    @GetMapping("/search/keyword")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> searchByKeyword(
            @Parameter(description = "Mot-clé de recherche", required = true, example = "electronique")
            @RequestParam String keyword) {
        List<ColisSimpleResponseDto> colis = colisService.searchByKeyword(keyword);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Recherche par mot-clé effectuée avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir les colis en retard",
            description = "Récupérer la liste des colis considérés comme en retard"
    )
    @GetMapping("/en-retard")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getColisEnRetard() {
        List<ColisSimpleResponseDto> colis = colisService.getColisEnRetard();

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis en retard récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Rechercher les colis par période de création",
            description = "Récupérer les colis créés dans une période spécifique"
    )
    @GetMapping("/date-creation")
    public ResponseEntity<ApiResponse<List<ColisSimpleResponseDto>>> getByDateCreationBetween(
            @Parameter(description = "Date de début (format ISO)", required = true, example = "2024-01-01T00:00:00")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Date de fin (format ISO)", required = true, example = "2024-12-31T23:59:59")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<ColisSimpleResponseDto> colis = colisService.getByDateCreationBetween(startDate, endDate);

        ApiResponse<List<ColisSimpleResponseDto>> response = ApiResponse.<List<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par période de création récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir les colis par zone (paginés)",
            description = "Récupérer les colis d'une zone spécifique avec pagination"
    )
    @GetMapping("/zone/{zoneId}/paginated")
    public ResponseEntity<ApiResponse<Page<ColisSimpleResponseDto>>> getByZoneId(
            @Parameter(description = "ID de la zone", required = true, example = "zone-001")
            @PathVariable String zoneId,
            @Parameter(description = "Paramètres de pagination")
            Pageable pageable) {
        Page<ColisSimpleResponseDto> colis = colisService.getByZoneId(zoneId, pageable);

        ApiResponse<Page<ColisSimpleResponseDto>> response = ApiResponse.<Page<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par zone paginés récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Obtenir les colis par statut (paginés)",
            description = "Récupérer les colis d'un statut spécifique avec pagination"
    )
    @GetMapping("/statut/{statut}/paginated")
    public ResponseEntity<ApiResponse<Page<ColisSimpleResponseDto>>> getByStatutPaginated(
            @Parameter(description = "Statut des colis", required = true, example = "CREE")
            @PathVariable StatutColis statut,
            @Parameter(description = "Paramètres de pagination")
            Pageable pageable) {
        Page<ColisSimpleResponseDto> colis = colisService.getByStatut(statut, pageable);

        ApiResponse<Page<ColisSimpleResponseDto>> response = ApiResponse.<Page<ColisSimpleResponseDto>>builder()
                .success(true)
                .message("Colis par statut paginés récupérés avec succès")
                .data(colis)
                .build();

        return ResponseEntity.ok(response);
    }

    // === ENDPOINTS DE STATISTIQUES ===

    @Operation(
            summary = "Compter les colis par statut",
            description = "Obtenir le nombre total de colis pour un statut spécifique"
    )
    @GetMapping("/statistiques/statut/{statut}/count")
    public ResponseEntity<ApiResponse<Long>> countByStatut(
            @Parameter(description = "Statut des colis", required = true, example = "CREE")
            @PathVariable StatutColis statut) {
        long count = colisService.countByStatut(statut);

        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .success(true)
                .message("Nombre de colis par statut récupéré avec succès")
                .data(count)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Compter les colis par zone et statut",
            description = "Obtenir le nombre de colis pour une combinaison zone/statut"
    )
    @GetMapping("/statistiques/zone/{zoneId}/statut/{statut}/count")
    public ResponseEntity<ApiResponse<Long>> countByZoneAndStatut(
            @Parameter(description = "ID de la zone", required = true, example = "zone-001")
            @PathVariable String zoneId,
            @Parameter(description = "Statut des colis", required = true, example = "CREE")
            @PathVariable StatutColis statut) {
        long count = colisService.countByZoneAndStatut(zoneId, statut);

        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .success(true)
                .message("Nombre de colis par zone et statut récupéré avec succès")
                .data(count)
                .build();

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Compter les colis par livreur et statut",
            description = "Obtenir le nombre de colis pour une combinaison livreur/statut"
    )
    @GetMapping("/statistiques/livreur/{livreurId}/statut/{statut}/count")
    public ResponseEntity<ApiResponse<Long>> countByLivreurAndStatut(
            @Parameter(description = "ID du livreur", required = true, example = "livreur-789")
            @PathVariable String livreurId,
            @Parameter(description = "Statut des colis", required = true, example = "EN_COURS")
            @PathVariable StatutColis statut) {
        long count = colisService.countByLivreurAndStatut(livreurId, statut);

        ApiResponse<Long> response = ApiResponse.<Long>builder()
                .success(true)
                .message("Nombre de colis par livreur et statut récupéré avec succès")
                .data(count)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/poidstotal/colis/{colisId}")
    public ResponseEntity<ApiResponse<Double>> poidsTotal(@PathVariable String colisId) {
        Double poids = colisService.calculateTotal(colisId);
        ApiResponse<Double> response = ApiResponse.<Double>builder()
                .success(true)
                .message("le Poids recupere successfully")
                .data(poids)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/prixtotal/colis/{colisId}")
    public ResponseEntity<ApiResponse<Double>> getPrixTotal(@PathVariable String colisId) {
        Double poids = colisService.calculateTotalPrix(colisId);

        ApiResponse<Double> response = ApiResponse.<Double>builder()
                .success(true)
                .message("le prix recupere successfully")
                .data(poids)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/poids-par-livreur")
    public ResponseEntity<ApiResponse<List<PoidsParLivreurDTO>>> getPoidsTotalParLivreur() {
        List<PoidsParLivreurDTO> result = colisService.getPoidsTotalParLivreur();

        ApiResponse<List<PoidsParLivreurDTO>> response = ApiResponse.<List<PoidsParLivreurDTO>>builder()
                .success(true)
                .message("Poids total par livreur récupéré avec succès")
                .data(result)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/poids-par-livreur/detail")
    public ResponseEntity<ApiResponse<List<PoidsParLivreurDetailDTO>>> getPoidsDetailParLivreur() {
        List<PoidsParLivreurDetailDTO> result = colisService.getPoidsDetailParLivreur();

        ApiResponse<List<PoidsParLivreurDetailDTO>> response = ApiResponse.<List<PoidsParLivreurDetailDTO>>builder()
                .success(true)
                .message("Détail poids par livreur récupéré avec succès")
                .data(result)
                .build();
        return ResponseEntity.ok(response);


    }
}