package com.smartlogi.smartlogiv010.service.interfaces;


import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ClientExpediteurCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ClientExpediteurUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface clientExpsditeurInterface {
    ClientExpediteurSimpleResponseDto create(ClientExpediteurCreateRequestDto requestDto);
    ClientExpediteurSimpleResponseDto update(String id, ClientExpediteurUpdateRequestDto requestDto);
    ClientExpediteurSimpleResponseDto getById(String id);
    ClientExpediteurSimpleResponseDto findByKeyWord(String keyword);
    Page<ClientExpediteurSimpleResponseDto> getAll(Pageable pageable);
    List<ClientExpediteurSimpleResponseDto> searchByNom(String nom);
    void delete(String id);
    boolean existsById(String id);
}