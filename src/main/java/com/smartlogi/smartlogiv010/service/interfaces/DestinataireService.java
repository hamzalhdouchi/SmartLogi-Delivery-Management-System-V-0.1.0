package com.smartlogi.smartlogiv010.service.interfaces;

import com.smartlogi.security.dto.authDto.response.UserResponse;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.DestinataireUpdateDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DestinataireService {
    UserResponse update(String id, DestinataireUpdateDto requestDto);
    UserResponse getById(String id);
    UserResponse findByKeyWord(String keyword);
    Page<UserResponse> getAll(Pageable pageable);
    void delete(String id);
    boolean existsById(String id);
}