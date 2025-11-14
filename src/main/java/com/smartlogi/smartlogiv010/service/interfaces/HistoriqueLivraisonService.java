package com.smartlogi.smartlogiv010.service.interfaces;


import com.smartlogi.smartlogiv010.dto.responseDTO.HistoriqueLivraison.HistoriqueLivraisonResponseDto;
import com.smartlogi.smartlogiv010.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoriqueLivraisonService {

    HistoriqueLivraisonResponseDto getById(String id);
    Page<HistoriqueLivraisonResponseDto> getAll(Pageable pageable);
    List<HistoriqueLivraisonResponseDto> getAll();
    List<HistoriqueLivraisonResponseDto> getByColis(String colisId);
    List<HistoriqueLivraisonResponseDto> getByColisOrderByDateAsc(String colisId);
    List<HistoriqueLivraisonResponseDto> getByColisOrderByDateDesc(String colisId);
    List<HistoriqueLivraisonResponseDto> getByStatut(StatutColis statut);
    List<HistoriqueLivraisonResponseDto> getByColisIdAndStatut(String colisId, StatutColis statut);
    List<HistoriqueLivraisonResponseDto> getByDateChangementBetween(LocalDateTime startDate, LocalDateTime endDate);
}
