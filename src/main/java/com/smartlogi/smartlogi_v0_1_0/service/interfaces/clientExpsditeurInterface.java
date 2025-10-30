package com.smartlogi.smartlogi_v0_1_0.service.interfaces;


import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ClientExpediteurCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ClientExpediteurUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.ClientExpediteur.ClientExpediteurAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface clientExpsditeurInterface {
    ClientExpediteurSimpleResponseDto create(ClientExpediteurCreateRequestDto requestDto);
    ClientExpediteurSimpleResponseDto update(String id, ClientExpediteurUpdateRequestDto requestDto);
    ClientExpediteurSimpleResponseDto getById(String id);
    ClientExpediteurAdvancedResponseDto getByIdWithColis(String id);
    Page<ClientExpediteurSimpleResponseDto> getAll(Pageable pageable);
    List<ClientExpediteurSimpleResponseDto> searchByNom(String nom);
    void delete(String id);
    boolean existsById(String id);
}