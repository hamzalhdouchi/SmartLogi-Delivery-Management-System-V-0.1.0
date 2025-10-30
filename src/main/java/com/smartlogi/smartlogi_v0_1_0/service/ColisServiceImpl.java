package com.smartlogi.smartlogi_v0_1_0.service;

import com.smartlogi.smartlogi_v0_1_0.dto.ColisCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ColisUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis.ColisAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis.ColisSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.entity.*;
import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import com.smartlogi.smartlogi_v0_1_0.mapper.SmartLogiMapper;
import com.smartlogi.smartlogi_v0_1_0.repository.*;
import com.smartlogi.smartlogi_v0_1_0.service.interfaces.ColisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColisServiceImpl implements ColisService {

    private final ColisRepository colisRepository;
    private final ClientExpediteurRepository clientExpediteurRepository;
    private final DestinataireRepository destinataireRepository;
    private final LivreurRepository livreurRepository;
    private final ZoneRepository zoneRepository;
    private final HistoriqueLivraisonRepository historiqueLivraisonRepository;
    private final SmartLogiMapper smartLogiMapper;

    @Override
    @Transactional
    public ColisSimpleResponseDto create(ColisCreateRequestDto requestDto) {
        Colis colis = smartLogiMapper.toEntity(requestDto);

        // Set relations
        ClientExpediteur client = clientExpediteurRepository.findById(requestDto.getClientExpediteurId())
                .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));
        Destinataire destinataire = destinataireRepository.findById(requestDto.getDestinataireId())
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));

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

        // Create initial historique
        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setColis(savedColis);
        historique.setStatut(StatutColis.CREE);
        historique.setDateChangement(LocalDateTime.now());
        historique.setCommentaire("Colis créé");
        historiqueLivraisonRepository.save(historique);

        return smartLogiMapper.toSimpleResponseDto(savedColis);
    }

    @Override
    @Transactional
    public ColisSimpleResponseDto update(String id, ColisUpdateRequestDto requestDto) {
        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        smartLogiMapper.updateEntityFromDto(requestDto, colis);

        // Update relations if provided
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
        // Utiliser la méthode searchByKeyword ou une autre méthode pour récupérer les détails
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
        // Utiliser searchByKeyword avec le nom du client ou implémenter une méthode spécifique
        return colisRepository.searchByKeyword(clientExpediteurId)
                .stream()
                .filter(colis -> colis.getClientExpediteur().getId().equals(clientExpediteurId))
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ColisSimpleResponseDto> getByDestinataire(String destinataireId) {
        // Utiliser searchByKeyword avec le nom du destinataire ou implémenter une méthode spécifique
        return colisRepository.searchByKeyword(destinataireId)
                .stream()
                .filter(colis -> colis.getDestinataire().getId().equals(destinataireId))
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ColisSimpleResponseDto> getByLivreur(String livreurId) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        return colisRepository.findByLivreur(livreur)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
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

        // Add to historique
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

        StatutColis ancienStatut = colis.getStatut();
        colis.setStatut(nouveauStatut);
        colisRepository.save(colis);

        // Add to historique
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

    // === NOUVELLES MÉTHODES POUR UTILISER LES FONCTIONNALITÉS DU REPOSITORY ===

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
        return colisRepository.findByZoneId(Long.parseLong(zoneId), pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    public Page<ColisSimpleResponseDto> getByStatut(StatutColis statut, Pageable pageable) {
        return colisRepository.findByStatut(statut, pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    // Méthodes de statistiques
    public long countByStatut(StatutColis statut) {
        return colisRepository.countByStatut(statut);
    }

    public long countByZoneAndStatut(String zoneId, StatutColis statut) {
        return colisRepository.countByZoneAndStatut(Long.parseLong(zoneId), statut);
    }

    public long countByLivreurAndStatut(String livreurId, StatutColis statut) {
        return colisRepository.countByLivreurAndStatut(Long.parseLong(livreurId), statut);
    }
}