package com.smartlogi.smartlogi_v0_1_0.service.interfaces;


import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.HistoriqueLivraisonCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.HistoriqueLivraisonUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.HistoriqueLivraison.HistoriqueLivraisonResponseDto;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface HistoriqueLivraisonService {

    HistoriqueLivraisonResponseDto getById(String id);

    // === Listing ===
    Page<HistoriqueLivraisonResponseDto> getAll(Pageable pageable);
    List<HistoriqueLivraisonResponseDto> getAll();

    // === Filtering ===
    List<HistoriqueLivraisonResponseDto> getByColis(String colisId);
    List<HistoriqueLivraisonResponseDto> getByColisOrderByDateAsc(String colisId);
    List<HistoriqueLivraisonResponseDto> getByColisOrderByDateDesc(String colisId);
    List<HistoriqueLivraisonResponseDto> getByStatut(StatutColis statut);
    List<HistoriqueLivraisonResponseDto> getByColisIdAndStatut(String colisId, StatutColis statut);
    List<HistoriqueLivraisonResponseDto> getByDateChangementBetween(LocalDateTime startDate, LocalDateTime endDate);
}
