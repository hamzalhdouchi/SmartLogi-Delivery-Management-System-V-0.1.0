package com.smartlogi.smartlogi_v0_1_0.service.interfaces;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.LivreurCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.LivreurUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur.LivreurAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur.LivreurDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur.LivreurSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LivreurService {

    LivreurSimpleResponseDto create(LivreurCreateRequestDto requestDto);
    LivreurSimpleResponseDto update(String id, LivreurUpdateRequestDto requestDto);
    LivreurSimpleResponseDto getById(String id);
    void delete(String id);
    boolean existsById(String id);
    LivreurAdvancedResponseDto getByIdWithStats(String id);
    LivreurDetailedResponseDto getByIdWithColis(String id);
    Page<LivreurSimpleResponseDto> getAll(Pageable pageable);
    List<LivreurSimpleResponseDto> getAll();
    List<LivreurSimpleResponseDto> getByZone(String zoneId);
    List<LivreurSimpleResponseDto> searchByNom(String nom);
    List<LivreurSimpleResponseDto> searchByKeyword(String keyword);
    Optional<LivreurSimpleResponseDto> getByTelephone(String telephone);
    long countByZone(String zoneId);
    boolean existsByTelephone(String telephone);
}