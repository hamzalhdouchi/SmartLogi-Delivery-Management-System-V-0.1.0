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
}
