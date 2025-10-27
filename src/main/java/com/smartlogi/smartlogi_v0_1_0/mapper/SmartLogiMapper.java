package com.smartlogi.smartlogi_v0_1_0.mapper;

import com.smartlogi.smartlogi_v0_1_0.dto.*;
import com.smartlogi.smartlogi_v0_1_0.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SmartLogiMapper {

    SmartLogiMapper INSTANCE = Mappers.getMapper(SmartLogiMapper.class);

    // ClientExpediteur
    ClientExpediteurDto toClientExpediteurDto(ClientExpediteur clientExpediteur);
    ClientExpediteur toClientExpediteur(ClientExpediteurDto clientExpediteurDto);
    List<ClientExpediteurDto> toClientExpediteurDtos(List<ClientExpediteur> clientExpediteurs);

    // Destinataire
    DestinataireDto toDestinataireDto(Destinataire destinataire);
    Destinataire toDestinataire(DestinataireDto destinataireDto);
    List<DestinataireDto> toDestinataireDtos(List<Destinataire> destinataires);

    // Livreur
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.nom", target = "zoneNom")
    @Mapping(source = "colisAssignes", target = "nombreColisAssignes", qualifiedByName = "countColisAssignes")
    LivreurDto toLivreurDto(Livreur livreur);
    Livreur toLivreur(LivreurDto livreurDto);
    List<LivreurDto> toLivreurDtos(List<Livreur> livreurs);

    // Zone
    @Mapping(source = "livreurs", target = "nombreLivreurs", qualifiedByName = "countLivreurs")
    @Mapping(source = "colis", target = "nombreColis", qualifiedByName = "countColis")
    ZoneDto toZoneDto(Zone zone);
    Zone toZone(ZoneDto zoneDto);
    List<ZoneDto> toZoneDtos(List<Zone> zones);

    // Produit
    ProduitDto toProduitDto(Produit produit);
    Produit toProduit(ProduitDto produitDto);
    List<ProduitDto> toProduitDtos(List<Produit> produits);

    // Colis
    @Mapping(source = "clientExpediteur.id", target = "clientExpediteurId")
    @Mapping(source = "clientExpediteur.nom", target = "clientExpediteurNom")
    @Mapping(source = "destinataire.id", target = "destinataireId")
    @Mapping(source = "destinataire.nom", target = "destinataireNom")
    @Mapping(source = "livreur.id", target = "livreurId")
    @Mapping(source = "livreur.nom", target = "livreurNom")
    @Mapping(source = "zone.id", target = "zoneId")
    @Mapping(source = "zone.nom", target = "zoneNom")
    ColisDto toColisDto(Colis colis);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "statut", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "historique", ignore = true)
    @Mapping(target = "produits", ignore = true)
    @Mapping(target = "clientExpediteur", ignore = true)
    @Mapping(target = "destinataire", ignore = true)
    @Mapping(target = "livreur", ignore = true)
    @Mapping(target = "zone", ignore = true)
    Colis toColis(ColisCreateDto colisCreateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientExpediteur", ignore = true)
    @Mapping(target = "destinataire", ignore = true)
    @Mapping(target = "dateCreation", ignore = true)
    @Mapping(target = "dateModification", ignore = true)
    @Mapping(target = "historique", ignore = true)
    @Mapping(target = "produits", ignore = true)
    void updateColisFromDto(ColisUpdateDto colisUpdateDto, @org.mapstruct.MappingTarget Colis colis);

    List<ColisDto> toColisDtos(List<Colis> colis);

    // HistoriqueLivraison
    @Mapping(source = "colis.id", target = "colisId")
    HistoriqueLivraisonDto toHistoriqueLivraisonDto(HistoriqueLivraison historiqueLivraison);
    List<HistoriqueLivraisonDto> toHistoriqueLivraisonDtos(List<HistoriqueLivraison> historiqueLivraisons);

    // ColisProduit
    @Mapping(source = "colis.id", target = "colisId")
    @Mapping(source = "produit.id", target = "produitId")
    @Mapping(source = "produit.nom", target = "produitNom")
    @Mapping(source = "produit.categorie", target = "produitCategorie")
    ColisProduitDto toColisProduitDto(ColisProduit colisProduit);
    List<ColisProduitDto> toColisProduitDtos(List<ColisProduit> colisProduits);

    // Méthodes utilitaires pour les counts
    default Integer countColisAssignes(List<Colis> colisAssignes) {
        return colisAssignes != null ? colisAssignes.size() : 0;
    }

    default Integer countLivreurs(List<Livreur> livreurs) {
        return livreurs != null ? livreurs.size() : 0;
    }

    default Integer countColis(List<Colis> colis) {
        return colis != null ? colis.size() : 0;
    }
}