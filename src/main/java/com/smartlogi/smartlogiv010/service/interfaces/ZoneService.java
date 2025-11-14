package com.smartlogi.smartlogiv010.service.interfaces;

import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ZoneCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ZoneUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Zone.ZoneDetailedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Zone.ZoneSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ZoneService {

    ZoneSimpleResponseDto create(ZoneCreateRequestDto requestDto);
    ZoneSimpleResponseDto update(String id, ZoneUpdateRequestDto requestDto);
    ZoneSimpleResponseDto getById(String id);
    void delete(String id);
    boolean existsById(String id);
    ZoneDetailedResponseDto getByIdWithDetails(String id);
    Page<ZoneSimpleResponseDto> getAll(Pageable pageable);
    List<ZoneSimpleResponseDto> getAll();
    Optional<ZoneSimpleResponseDto> getByCodePostal(String codePostal);
    Optional<ZoneSimpleResponseDto> getByNom(String nom);
    List<ZoneSimpleResponseDto> searchByNom(String nom);
    List<ZoneSimpleResponseDto> searchByKeyword(String keyword);
    boolean existsByCodePostal(String codePostal);
    boolean existsByNom(String nom);
}