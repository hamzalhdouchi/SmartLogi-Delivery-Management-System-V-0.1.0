package com.smartlogi.smartlogi_v0_1_0.service;


import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.HistoriqueLivraisonCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.HistoriqueLivraisonUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.HistoriqueLivraison.HistoriqueLivraisonResponseDto;
import com.smartlogi.smartlogi_v0_1_0.entity.Colis;
import com.smartlogi.smartlogi_v0_1_0.entity.HistoriqueLivraison;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import com.smartlogi.smartlogi_v0_1_0.mapper.SmartLogiMapper;
import com.smartlogi.smartlogi_v0_1_0.repository.ColisRepository;
import com.smartlogi.smartlogi_v0_1_0.repository.HistoriqueLivraisonRepository;
import com.smartlogi.smartlogi_v0_1_0.service.interfaces.HistoriqueLivraisonService;
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
public class HistoriqueLivraisonServiceImpl implements HistoriqueLivraisonService {

    private final HistoriqueLivraisonRepository historiqueLivraisonRepository;
    private final ColisRepository colisRepository;
    private final SmartLogiMapper smartLogiMapper;


    @Override
    public HistoriqueLivraisonResponseDto getById(String id) {
        HistoriqueLivraison historique = historiqueLivraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Historique non trouvé"));
        return smartLogiMapper.toResponseDto(historique);
    }



    @Override
    public Page<HistoriqueLivraisonResponseDto> getAll(Pageable pageable) {
        return historiqueLivraisonRepository.findAll(pageable)
                .map(smartLogiMapper::toResponseDto);
    }

    @Override
    public List<HistoriqueLivraisonResponseDto> getAll() {
        return historiqueLivraisonRepository.findAll()
                .stream()
                .map(smartLogiMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoriqueLivraisonResponseDto> getByColis(String colisId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        return historiqueLivraisonRepository.findByColis(colis)
                .stream()
                .map(smartLogiMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoriqueLivraisonResponseDto> getByColisOrderByDateAsc(String colisId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        return historiqueLivraisonRepository.findByColisOrderByDateChangementAsc(colis)
                .stream()
                .map(smartLogiMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoriqueLivraisonResponseDto> getByColisOrderByDateDesc(String colisId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        return historiqueLivraisonRepository.findByColisOrderByDateChangementDesc(colis)
                .stream()
                .map(smartLogiMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoriqueLivraisonResponseDto> getByStatut(StatutColis statut) {
        return historiqueLivraisonRepository.findByStatut(statut)
                .stream()
                .map(smartLogiMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoriqueLivraisonResponseDto> getByColisIdAndStatut(String colisId, StatutColis statut) {
        return historiqueLivraisonRepository.findByColisIdAndStatut(colisId, statut)
                .stream()
                .map(smartLogiMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<HistoriqueLivraisonResponseDto> getByDateChangementBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return historiqueLivraisonRepository.findByDateChangementBetween(startDate, endDate)
                .stream()
                .map(smartLogiMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // === MÉTHODES UTILITAIRES ===

    public HistoriqueLivraisonResponseDto getDernierStatut(String colisId) {
        List<HistoriqueLivraisonResponseDto> historique = getByColisOrderByDateDesc(colisId);
        return historique.isEmpty() ? null : historique.get(0);
    }


}
