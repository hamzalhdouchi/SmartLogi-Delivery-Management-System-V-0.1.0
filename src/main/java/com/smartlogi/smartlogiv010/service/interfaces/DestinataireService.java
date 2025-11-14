package com.smartlogi.smartlogiv010.service.interfaces;

import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.DestinataireCreateDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.DestinataireUpdateDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Destinataire.DestinataireSimpleResponseDto;
import com.smartlogi.smartlogiv010.exception.EmailAlreadyExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DestinataireService {
    DestinataireSimpleResponseDto create(DestinataireCreateDto requestDto) throws EmailAlreadyExistsException;
    DestinataireSimpleResponseDto update(String id, DestinataireUpdateDto requestDto);
    DestinataireSimpleResponseDto getById(String id);
    DestinataireSimpleResponseDto findByKeyWord(String keyword);
    Page<DestinataireSimpleResponseDto> getAll(Pageable pageable);
    List<DestinataireSimpleResponseDto> searchByNom(String nom);
    void delete(String id);
    boolean existsById(String id);
}