package com.smartlogi.smartlogi_v0_1_0.service.interfaces;

import com.smartlogi.smartlogi_v0_1_0.dto.ColisCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ColisUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis.ColisAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Colis.ColisSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ColisService {

    ColisSimpleResponseDto create(ColisCreateRequestDto requestDto);
    ColisSimpleResponseDto update(String id, ColisUpdateRequestDto requestDto);
    ColisSimpleResponseDto getById(String id);
    ColisAdvancedResponseDto getByIdWithDetails(String id);
    Page<ColisSimpleResponseDto> getAll(Pageable pageable);
    void delete(String id);

    void assignerLivreur(String colisId, String livreurId);
    void changerStatut(String colisId, StatutColis nouveauStatut, String commentaire);

    List<ColisSimpleResponseDto> getByStatut(StatutColis statut);
    List<ColisSimpleResponseDto> getByClientExpediteur(String clientExpediteurId);
    List<ColisSimpleResponseDto> getByDestinataire(String destinataireId);
    List<ColisSimpleResponseDto> getByLivreur(String livreurId);
    List<ColisSimpleResponseDto> getByZone(String zoneId);
    List<ColisSimpleResponseDto> searchByVilleDestination(String ville);

    List<ColisSimpleResponseDto> getByPriorite(Priorite priorite);
    List<ColisSimpleResponseDto> getByPrioriteAndStatut(Priorite priorite, StatutColis statut);
    List<ColisSimpleResponseDto> getByLivreurAndStatut(String livreurId, StatutColis statut);
    List<ColisSimpleResponseDto> getByZoneAndStatut(String zoneId, StatutColis statut);
    List<ColisSimpleResponseDto> getByVilleDestinationAndStatut(String ville, StatutColis statut);

    List<ColisSimpleResponseDto> searchByKeyword(String keyword);

    List<ColisSimpleResponseDto> getByDateCreationBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<ColisSimpleResponseDto> getColisEnRetard();

    Page<ColisSimpleResponseDto> getByZoneId(String zoneId, Pageable pageable);
    Page<ColisSimpleResponseDto> getByStatut(StatutColis statut, Pageable pageable);

    long countByStatut(StatutColis statut);
    long countByZoneAndStatut(String zoneId, StatutColis statut);
    long countByLivreurAndStatut(String livreurId, StatutColis statut);
}