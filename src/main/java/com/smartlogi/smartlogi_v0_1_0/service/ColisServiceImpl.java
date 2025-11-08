package com.smartlogi.smartlogi_v0_1_0.service;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ColisCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ProduitCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ColisUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis.ColisAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis.ColisSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.PoidsParLivreur.PoidsParLivreurDTO;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.PoidsParLivreur.PoidsParLivreurDetailDTO;
import com.smartlogi.smartlogi_v0_1_0.entity.ColisProduitId;
import com.smartlogi.smartlogi_v0_1_0.entity.*;
import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import com.smartlogi.smartlogi_v0_1_0.exception.ArgementNotFoundExption;
import com.smartlogi.smartlogi_v0_1_0.mapper.SmartLogiMapper;
import com.smartlogi.smartlogi_v0_1_0.repository.*;
import com.smartlogi.smartlogi_v0_1_0.service.interfaces.ColisService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColisServiceImpl implements ColisService {

    private final ColisRepository colisRepository;
    private final ProduitRepository  produitRepository;
    private final ColisProduitRepository colisProduitRepository;
    private final ClientExpediteurRepository clientExpediteurRepository;
    private final DestinataireRepository destinataireRepository;
    private final LivreurRepository livreurRepository;
    private final ZoneRepository zoneRepository;
    private final HistoriqueLivraisonRepository historiqueLivraisonRepository;
    private final SmartLogiMapper smartLogiMapper;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public ColisSimpleResponseDto create(ColisCreateRequestDto requestDto) {
        ClientExpediteur client = clientExpediteurRepository.findById(requestDto.getClientExpediteurId())
                .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));
        Destinataire destinataire = destinataireRepository.findById(requestDto.getDestinataireId())
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));

        Colis colis = smartLogiMapper.toEntity(requestDto);
        colis.setClientExpediteur(client);
        colis.setDestinataire(destinataire);

        if (requestDto.getLivreurId() != null) {
            Livreur livreur = livreurRepository.findById(requestDto.getLivreurId())
                    .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
            colis.setLivreur(livreur);
        }

        if (requestDto.getZoneId() != null) {
            Zone zone = zoneRepository.findById(requestDto.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
            colis.setZone(zone);
        }

        Colis savedColis = colisRepository.save(colis);

        notificationService.envoyerNotificationCollecte(client , savedColis);

        if (requestDto.getProduits() != null && !requestDto.getProduits().isEmpty()) {
            for (ColisCreateRequestDto.ProduitColisDto produitDto : requestDto.getProduits()) {
                ajouterProduitAuColis(savedColis, produitDto);
            }
        }

        creerHistoriqueLivraison(savedColis, StatutColis.CREE, "Colis créé avec produits");


        return smartLogiMapper.toSimpleResponseDto(savedColis);
    }

    private void ajouterProduitAuColis(Colis colis, ColisCreateRequestDto.ProduitColisDto produitDto) {
        validerProduitRequest(produitDto);

        Produit produit;

        if (produitDto.getProduitId() != null && !produitDto.getProduitId().isBlank()) {
            produit = produitRepository.findById(produitDto.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + produitDto.getProduitId()));
        } else {
            produit = creerNouveauProduit(produitDto.getNouveauProduit());
        }

        if (produitDto.getQuantite() == null || produitDto.getQuantite() <= 0) {
            throw new RuntimeException("La quantité doit être supérieure à 0 pour le produit: " + produit.getNom());
        }

        BigDecimal prixTotal = produit.getPrix().multiply(BigDecimal.valueOf(produitDto.getQuantite()));

        ColisProduitId colisProduitId = new ColisProduitId(colis.getId(), produit.getId());
        ColisProduit colisProduit = new ColisProduit();
        colisProduit.setId(colisProduitId);
        colisProduit.setColis(colis);
        colisProduit.setProduit(produit);
        colisProduit.setQuantite(produitDto.getQuantite());
        colisProduit.setPrix(prixTotal);

        colisProduitRepository.save(colisProduit);
    }

    private void validerProduitRequest(ColisCreateRequestDto.ProduitColisDto produitDto) {
        boolean aProduitId = produitDto.getProduitId() != null && !produitDto.getProduitId().isBlank();
        boolean aNouveauProduit = produitDto.getNouveauProduit() != null;

        if (aProduitId && aNouveauProduit) {
            throw new RuntimeException("Vous ne pouvez pas fournir à la fois un produitId et un nouveauProduit");
        }

        if (!aProduitId && !aNouveauProduit) {
            throw new RuntimeException("Vous devez fournir soit un produitId soit un nouveauProduit");
        }

        if (aNouveauProduit) {
            ProduitCreateRequestDto nouveauProduit = produitDto.getNouveauProduit();
            if (nouveauProduit.getNom() == null || nouveauProduit.getNom().isBlank()) {
                throw new RuntimeException("Le nom du nouveau produit est obligatoire");
            }
            if (nouveauProduit.getPrix() == null || nouveauProduit.getPrix().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Le prix du nouveau produit doit être supérieur à 0");
            }
        }
    }

    private Produit creerNouveauProduit(ProduitCreateRequestDto nouveauProduitDto) {
        if (produitRepository.existsByNomContainingIgnoreCase(nouveauProduitDto.getNom())) {
            throw new RuntimeException("Un produit avec le nom '" + nouveauProduitDto.getNom() + "' existe déjà");
        }

        // Créer et sauvegarder le nouveau produit
        Produit produit = new Produit();
        produit.setNom(nouveauProduitDto.getNom());
        produit.setCategorie(nouveauProduitDto.getCategorie());
        produit.setPoids(nouveauProduitDto.getPoids());
        produit.setPrix(nouveauProduitDto.getPrix());

        return produitRepository.save(produit);
    }

    private void creerHistoriqueLivraison(Colis colis, StatutColis statut, String commentaire) {
        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setColis(colis);
        historique.setStatut(statut);
        historique.setDateChangement(LocalDateTime.now());
        historique.setCommentaire(commentaire);
        historiqueLivraisonRepository.save(historique);
    }

    @Transactional
    public void ajouterProduit(String colisId, ColisCreateRequestDto.ProduitColisDto produitDto) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        ajouterProduitAuColis(colis, produitDto);

        String produitInfo = produitDto.getProduitId() != null ?
                "Produit existant ajouté: " + produitDto.getProduitId() :
                "Nouveau produit créé et ajouté: " + produitDto.getNouveauProduit().getNom();

        creerHistoriqueLivraison(colis, colis.getStatut(), produitInfo);
    }

    @Transactional
    public void supprimerProduit(String colisId, String produitId) {
        ColisProduitId colisProduitId = new ColisProduitId(colisId, produitId);
        if (colisProduitRepository.existsById(colisProduitId)) {
            colisProduitRepository.deleteById(colisProduitId);

            Colis colis = colisRepository.findById(colisId)
                    .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
            creerHistoriqueLivraison(colis, colis.getStatut(),
                    "Produit retiré du colis: " + produitId);
        } else {
            throw new RuntimeException("Produit non trouvé dans ce colis");
        }
    }

    @Transactional
    public void mettreAJourQuantiteProduit(String colisId, String produitId, Integer nouvelleQuantite) {
        ColisProduitId colisProduitId = new ColisProduitId(colisId, produitId);
        ColisProduit colisProduit = colisProduitRepository.findById(colisProduitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé dans ce colis"));

        if (nouvelleQuantite == null || nouvelleQuantite <= 0) {
            throw new RuntimeException("La quantité doit être supérieure à 0");
        }

        BigDecimal nouveauPrix = colisProduit.getProduit().getPrix().multiply(BigDecimal.valueOf(nouvelleQuantite));

        colisProduit.setQuantite(nouvelleQuantite);
        colisProduit.setPrix(nouveauPrix);
        colisProduitRepository.save(colisProduit);

        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        creerHistoriqueLivraison(colis, colis.getStatut(),
                "Quantité mise à jour pour le produit: " + produitId + " (nouvelle quantité: " + nouvelleQuantite + ")");
    }

    public List<ColisProduit> getProduitsByColis(String colisId) {
        return colisProduitRepository.findByColisId(colisId);
    }

    @Override
    @Transactional
    public ColisSimpleResponseDto update(String id, ColisUpdateRequestDto requestDto) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new ArgementNotFoundExption(id,"Colis with this id"));

        smartLogiMapper.updateEntityFromDto(requestDto, colis);

        if (requestDto.getLivreurId() != null) {
            Livreur livreur = livreurRepository.findById(requestDto.getLivreurId())
                    .orElseThrow(() -> new ArgementNotFoundExption(requestDto.getLivreurId(),"livreur with this id"));
            colis.setLivreur(livreur);
        }

        if (requestDto.getZoneId() != null) {
            Zone zone = zoneRepository.findById(requestDto.getZoneId())
                    .orElseThrow(() -> new ArgementNotFoundExption(requestDto.getZoneId(),"zone with this id"));
            colis.setZone(zone);
        }

        Colis updatedColis = colisRepository.save(colis);
        return smartLogiMapper.toSimpleResponseDto(updatedColis);
    }

    @Override
    public ColisSimpleResponseDto getById(String id) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        return smartLogiMapper.toSimpleResponseDto(colis);
    }

    @Override
    public ColisAdvancedResponseDto getByIdWithDetails(String id) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        return smartLogiMapper.toAdvancedResponseDto(colis);
    }

    @Override
    public Page<ColisSimpleResponseDto> getAll(Pageable pageable) {
        return colisRepository.findAll(pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    @Override
    public List<ColisSimpleResponseDto> getByStatut(StatutColis statut) {
        return colisRepository.findByStatut(statut)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ColisSimpleResponseDto> getByClientExpediteur(String clientExpediteurId) {
        return colisRepository.searchByKeyword(clientExpediteurId)
                .stream()
                .filter(colis -> colis.getClientExpediteur().getId().equals(clientExpediteurId))
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ColisSimpleResponseDto> getByDestinataire(String destinataireId) {
        return colisRepository.searchByKeyword(destinataireId)
                .stream()
                .filter(colis -> colis.getDestinataire().getId().equals(destinataireId))
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ColisAdvancedResponseDto> getByLivreur(String livreurId) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        return colisRepository.findByLivreur(livreur)
                .stream()
                .map(smartLogiMapper::toAdvancedResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ColisSimpleResponseDto> getByZone(String zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
        return colisRepository.findByZone(zone)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ColisSimpleResponseDto> searchByVilleDestination(String ville) {
        return colisRepository.findByVilleDestination(ville)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void assignerLivreur(String colisId, String livreurId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));

        colis.setLivreur(livreur);
        colisRepository.save(colis);

        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setColis(colis);
        historique.setStatut(colis.getStatut());
        historique.setDateChangement(LocalDateTime.now());
        historique.setCommentaire("Livreur assigné: " + livreur.getNom() + " " + livreur.getPrenom());
        historiqueLivraisonRepository.save(historique);
    }

    @Override
    @Transactional
    public void changerStatut(String colisId, StatutColis nouveauStatut, String commentaire) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        Destinataire destinataire = colis.getDestinataire();

        if (nouveauStatut == StatutColis.LIVRE){
            notificationService.envoyerNotificationLivraison(destinataire,colis);
        }
        StatutColis ancienStatut = colis.getStatut();
        colis.setStatut(nouveauStatut);
        colisRepository.save(colis);

        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setColis(colis);
        historique.setStatut(nouveauStatut);
        historique.setDateChangement(LocalDateTime.now());
        historique.setCommentaire(commentaire != null ? commentaire :
                "Statut changé de " + ancienStatut + " à " + nouveauStatut);
        historiqueLivraisonRepository.save(historique);
    }

    @Override
    @Transactional
    public void delete(String id) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        colisRepository.delete(colis);
    }

    public List<ColisSimpleResponseDto> getByPriorite(Priorite priorite) {
        return colisRepository.findByPriorite(priorite)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    public List<ColisSimpleResponseDto> getByPrioriteAndStatut(Priorite priorite, StatutColis statut) {
        return colisRepository.findByPrioriteAndStatut(priorite, statut)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    public List<ColisSimpleResponseDto> getByLivreurAndStatut(String livreurId, StatutColis statut) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        return colisRepository.findByLivreurAndStatut(livreur, statut)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    public List<ColisSimpleResponseDto> getByZoneAndStatut(String zoneId, StatutColis statut) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
        return colisRepository.findByZoneAndStatut(zone, statut)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    public List<ColisSimpleResponseDto> getByVilleDestinationAndStatut(String ville, StatutColis statut) {
        return colisRepository.findByVilleDestinationAndStatut(ville, statut)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    public List<ColisSimpleResponseDto> searchByKeyword(String keyword) {
        return colisRepository.searchByKeyword(keyword)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    public List<ColisSimpleResponseDto> getColisEnRetard() {
        LocalDateTime dateLimite = LocalDateTime.now().minusDays(2);
        return colisRepository.findColisEnRetard(dateLimite)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    public List<ColisSimpleResponseDto> getByDateCreationBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return colisRepository.findByDateCreationBetween(startDate, endDate)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    public Page<ColisSimpleResponseDto> getByZoneId(String zoneId, Pageable pageable) {
        return colisRepository.findByZoneId(zoneId, pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    public Page<ColisSimpleResponseDto> getByStatut(StatutColis statut, Pageable pageable) {
        return colisRepository.findByStatut(statut, pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    public long countByStatut(StatutColis statut) {
        return colisRepository.countByStatut(statut);
    }

    public long countByZoneAndStatut(String zoneId, StatutColis statut) {
        return colisRepository.countByZoneAndStatut(zoneId, statut);
    }

    public long countByLivreurAndStatut(String livreurId, StatutColis statut) {
        return colisRepository.countByLivreurAndStatut(livreurId, statut);
    }

    public boolean produitExisteDansColis(String colisId, String produitId) {
        ColisProduitId colisProduitId = new ColisProduitId(colisId, produitId);
        return colisProduitRepository.existsById(colisProduitId);
    }

    public BigDecimal getPrixTotalColis(String colisId) {
        List<ColisProduit> produits = colisProduitRepository.findByColisId(colisId);
        return produits.stream()
                .map(ColisProduit::getPrix)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Double calculateTotal(String colisId){
        if(colisRepository.existsById(colisId)){
            return colisProduitRepository.calculateTotalPoidsByColis(colisId);
        }else {
          throw new ArgementNotFoundExption(colisId,"this colis is not found");
        }
    }

    public Double calculateTotalPrix(String colisId){
        if(colisRepository.existsById(colisId)){
            return colisProduitRepository.calculateTotalPrixByColis(colisId);
        }else {
            throw new ArgementNotFoundExption(colisId,"this colis is not found");
        }
    }


    public List<PoidsParLivreurDTO> getPoidsTotalParLivreur() {
        try {
            List<Object[]> results = colisRepository.findPoidsTotalParLivreur();

            return results.stream()
                    .map(PoidsParLivreurDTO::new)
                    .sorted((d1, d2) -> d2.getPoidsTotal().compareTo(d1.getPoidsTotal()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Impossible de calculer le poids par livreur", e);
        }
    }

    public List<PoidsParLivreurDetailDTO> getPoidsDetailParLivreur() {
        try {
            List<Object[]> results = colisRepository.findPoidsDetailParLivreur();

            return results.stream()
                    .map(PoidsParLivreurDetailDTO::new)
                    .sorted((d1, d2) -> d2.getPoidsTotal().compareTo(d1.getPoidsTotal()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Impossible de calculer le détail poids par livreur", e);
        }
    }
}