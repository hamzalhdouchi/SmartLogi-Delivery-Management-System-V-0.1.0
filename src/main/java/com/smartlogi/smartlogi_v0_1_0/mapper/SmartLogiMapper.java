package com.smartlogi.smartlogi_v0_1_0.mapper;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.*;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.*;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.ClientExpediteur.*;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis.ColisAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis.ColisSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.ColisProduit.ColisProduitResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Destinataire.DestinataireAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Destinataire.DestinataireSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.HistoriqueLivraison.HistoriqueLivraisonResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur.LivreurAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur.LivreurDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur.LivreurSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone.ZoneDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone.ZoneSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.entity.*;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface SmartLogiMapper {

    SmartLogiMapper INSTANCE = Mappers.getMapper(SmartLogiMapper.class);

    // ============ CLIENT EXPEDITEUR ============

    // Request Mappings
    ClientExpediteur toEntity(ClientExpediteurCreateRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "colis", ignore = true)
    void updateEntityFromDto(ClientExpediteurUpdateRequestDto requestDto, @MappingTarget ClientExpediteur client);

    // Response Mappings
    ClientExpediteurSimpleResponseDto toSimpleResponseDto(ClientExpediteur client);

    @Mapping(source = "colis", target = "colis")
    ClientExpediteurAdvancedResponseDto toAdvancedResponseDto(ClientExpediteur client);

    List<ClientExpediteurSimpleResponseDto> toClientExpediteurSimpleResponseDtos(List<ClientExpediteur> clients);

    // ============ DESTINATAIRE ============

    // Request Mappings
    Destinataire toEntity(DestinataireCreateDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "colis", ignore = true)
    void updateEntityFromDto(DestinataireUpdateDto requestDto, @MappingTarget Destinataire destinataire);

    // Response Mappings
    DestinataireSimpleResponseDto toSimpleResponseDto(Destinataire destinataire);

    @Mapping(source = "colis", target = "colis")
    DestinataireAdvancedResponseDto toAdvancedResponseDto(Destinataire destinataire);

    List<DestinataireSimpleResponseDto> toDestinataireSimpleResponseDtos(List<Destinataire> destinataires);

    // ============ LIVREUR ============

    // Request Mappings
    @Mapping(target = "zone", ignore = true)
    @Mapping(target = "colisAssignes", ignore = true)
    Livreur toEntity(LivreurCreateRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "colisAssignes", ignore = true)
    void updateEntityFromDto(LivreurUpdateRequestDto requestDto, @MappingTarget Livreur livreur);

    // Response Mappings
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.nom", target = "zoneNom")
    LivreurSimpleResponseDto toSimpleResponseDto(Livreur livreur);

    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.nom", target = "zoneNom")
    @Mapping(source = "colisAssignes", target = "nombreColisAssignes", qualifiedByName = "countColis")
    @Mapping(source = "colisAssignes", target = "nombreColisLives", qualifiedByName = "countColisLives")
    @Mapping(source = "colisAssignes", target = "nombreColisEnCours", qualifiedByName = "countColisEnCours")
    LivreurAdvancedResponseDto toAdvancedResponseDto(Livreur livreur);

    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.nom", target = "zoneNom")
    @Mapping(source = "colisAssignes", target = "nombreColisAssignes", qualifiedByName = "countColis")
    @Mapping(source = "colisAssignes", target = "nombreColisLives", qualifiedByName = "countColisLives")
    @Mapping(source = "colisAssignes", target = "nombreColisEnCours", qualifiedByName = "countColisEnCours")
    @Mapping(source = "colisAssignes", target = "colisAssignes")
    LivreurDetailedResponseDto toDetailedResponseDto(Livreur livreur);

    List<LivreurSimpleResponseDto> toLivreurSimpleResponseDtos(List<Livreur> livreurs);

    // ============ ZONE ============

    // Request Mappings
    Zone toEntity(ZoneCreateRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "livreurs", ignore = true)
    @Mapping(target = "colis", ignore = true)
    void updateEntityFromDto(ZoneUpdateRequestDto requestDto, @MappingTarget Zone zone);

    // Response Mappings
    ZoneSimpleResponseDto toSimpleResponseDto(Zone zone);

    @Mapping(source = "livreurs", target = "nombreLivreurs", qualifiedByName = "countLivreurs")
    @Mapping(source = "colis", target = "nombreColis", qualifiedByName = "countColis")
    @Mapping(source = "livreurs", target = "livreurs")
    @Mapping(source = "colis", target = "colis")
    ZoneDetailedResponseDto toDetailedResponseDto(Zone zone);

    List<ZoneSimpleResponseDto> toZoneSimpleResponseDtos(List<Zone> zones);

    // ============ PRODUIT ============

    // Request Mappings
    Produit toEntity(ProduitCreateRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "colisProduits", ignore = true)
    void updateEntityFromDto(ProduitUpdateRequestDto requestDto, @MappingTarget Produit produit);

    // Response Mappings
    ProduitSimpleResponseDto toSimpleResponseDto(Produit produit);

    @Mapping(source = "colisProduits", target = "nombreColisAssocies", qualifiedByName = "countColisProduits")
    @Mapping(source = "colisProduits", target = "quantiteTotaleVendue", qualifiedByName = "sumQuantite")
    @Mapping(source = "colisProduits", target = "chiffreAffaireTotal", qualifiedByName = "sumChiffreAffaire")
    ProduitAdvancedResponseDto toAdvancedResponseDto(Produit produit);

    @Mapping(source = "colisProduits", target = "nombreColisAssocies", qualifiedByName = "countColisProduits")
    @Mapping(source = "colisProduits", target = "quantiteTotaleVendue", qualifiedByName = "sumQuantite")
    @Mapping(source = "colisProduits", target = "chiffreAffaireTotal", qualifiedByName = "sumChiffreAffaire")
    @Mapping(source = "colisProduits", target = "colisProduits")
    ProduitDetailedResponseDto toDetailedResponseDto(Produit produit);

    List<ProduitSimpleResponseDto> toProduitSimpleResponseDtos(List<Produit> produits);

    // ============ COLIS ============

    // Request Mappings
    @Mapping(target = "clientExpediteur", ignore = true)
    @Mapping(target = "destinataire", ignore = true)
    @Mapping(target = "livreur", ignore = true)
    @Mapping(target = "zone", ignore = true)
    @Mapping(target = "historique", ignore = true)
    @Mapping(target = "produits", ignore = true)
    Colis toEntity(ColisCreateRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientExpediteur", ignore = true)
    @Mapping(target = "destinataire", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "historique", ignore = true)
    @Mapping(target = "produits", ignore = true)
    void updateEntityFromDto(ColisUpdateRequestDto requestDto, @MappingTarget Colis colis);

    // Response Mappings
    @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId")
    @Mapping(source = "clientExpediteur.nom", target = "clientExpediteurNom")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(source = "destinataire.nom", target = "destinataireNom")
    @Mapping(source = "livreur.id", target = "livreurId")
    @Mapping(source = "livreur.nom", target = "livreurNom")
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.nom", target = "zoneNom")
    ColisSimpleResponseDto toSimpleResponseDto(Colis colis);

    @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId")
    @Mapping(source = "clientExpediteur.nom", target = "clientExpediteurNom")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(source = "destinataire.nom", target = "destinataireNom")
    @Mapping(source = "livreur.id", target = "livreurId")
    @Mapping(source = "livreur.nom", target = "livreurNom")
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.nom", target = "zoneNom")
    @Mapping(source = "historique", target = "historique")
    @Mapping(source = "produits", target = "produits")
    ColisAdvancedResponseDto toAdvancedResponseDto(Colis colis);


    List<ColisSimpleResponseDto> toColisSimpleResponseDtos(List<Colis> colis);

    // ============ HISTORIQUE LIVRAISON ============

    // Request Mappings
    @Mapping(target = "colis", ignore = true)
    HistoriqueLivraison toEntity(HistoriqueLivraisonCreateRequestDto requestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "colis", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    void updateEntityFromDto(HistoriqueLivraisonUpdateRequestDto requestDto, @MappingTarget HistoriqueLivraison historique);

    // Response Mappings
    @Mapping(source = "colis.id", target = "colisId")
    HistoriqueLivraisonResponseDto toResponseDto(HistoriqueLivraison historique);

    List<HistoriqueLivraisonResponseDto> toHistoriqueLivraisonResponseDtos(List<HistoriqueLivraison> historiques);

    // ============ COLIS PRODUIT ============

    // Request Mappings
    @Mapping(target = "colis", ignore = true)
    @Mapping(target = "produit", ignore = true)
    ColisProduit toEntity(ColisProduitCreateRequestDto requestDto);

    @Mapping(target = "colis", ignore = true)
    @Mapping(target = "produit", ignore = true)
    @Mapping(target = "dateAjout", ignore = true)
    void updateEntityFromDto(ColisProduitUpdateRequestDto requestDto, @MappingTarget ColisProduit colisProduit);

    // Response Mappings
    @Mapping(source = "colis.id", target = "colisId")
    @Mapping(source = "produit.id", target = "produitId")
    @Mapping(source = "produit.nom", target = "produitNom")
    @Mapping(source = "produit.categorie", target = "produitCategorie")
    @Mapping(source = "prix", target = "prixUnitaire")
    @Mapping(expression = "java(colisProduit.getPrix().multiply(java.math.BigDecimal.valueOf(colisProduit.getQuantite())))", target = "prixTotal")
    ColisProduitResponseDto toResponseDto(ColisProduit colisProduit);

    List<ColisProduitResponseDto> toColisProduitResponseDtos(List<ColisProduit> colisProduits);


    @Named("countColis")
    default Integer countColis(List<Colis> colis) {
        return colis != null ? colis.size() : 0;
    }

    @Named("countLivreurs")
    default Integer countLivreurs(List<Livreur> livreurs) {
        return livreurs != null ? livreurs.size() : 0;
    }

    @Named("countColisProduits")
    default Integer countColisProduits(List<ColisProduit> colisProduits) {
        return colisProduits != null ? colisProduits.size() : 0;
    }

    @Named("countColisLives")
    default Integer countColisLives(List<Colis> colisAssignes) {
        if (colisAssignes == null) return 0;
        return (int) colisAssignes.stream()
                .filter(colis -> colis.getStatut().name().equals("LIVRE"))
                .count();
    }

    @Named("countColisEnCours")
    default Integer countColisEnCours(List<Colis> colisAssignes) {
        if (colisAssignes == null) return 0;
        return (int) colisAssignes.stream()
                .filter(colis -> !colis.getStatut().name().equals("LIVRE") && !colis.getStatut().name().equals("CREE"))
                .count();
    }

    @Named("sumQuantite")
    default Integer sumQuantite(List<ColisProduit> colisProduits) {
        if (colisProduits == null) return 0;
        return colisProduits.stream()
                .mapToInt(ColisProduit::getQuantite)
                .sum();
    }

    @Named("sumChiffreAffaire")
    default java.math.BigDecimal sumChiffreAffaire(List<ColisProduit> colisProduits) {
        if (colisProduits == null) return java.math.BigDecimal.ZERO;
        return colisProduits.stream()
                .map(cp -> cp.getPrix().multiply(java.math.BigDecimal.valueOf(cp.getQuantite())))
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
    }
}