package com.smartlogi.smartlogi_v0_1_0.service.interfaces;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ZoneCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ZoneUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone.ZoneDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone.ZoneSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ZoneService {

    // === CRUD Operations ===
    ZoneSimpleResponseDto create(ZoneCreateRequestDto requestDto);
    ZoneSimpleResponseDto update(String id, ZoneUpdateRequestDto requestDto);
    ZoneSimpleResponseDto getById(String id);
    void delete(String id);
    boolean existsById(String id);

    // === Advanced Queries ===
    ZoneDetailedResponseDto getByIdWithDetails(String id);

    // === Listing & Pagination ===
    Page<ZoneSimpleResponseDto> getAll(Pageable pageable);
    List<ZoneSimpleResponseDto> getAll();

    // === Filtering & Search ===
    Optional<ZoneSimpleResponseDto> getByCodePostal(String codePostal);
    Optional<ZoneSimpleResponseDto> getByNom(String nom);
    List<ZoneSimpleResponseDto> searchByNom(String nom);
    List<ZoneSimpleResponseDto> searchByKeyword(String keyword);

    // === Validation ===
    boolean existsByCodePostal(String codePostal);
    boolean existsByNom(String nom);
}