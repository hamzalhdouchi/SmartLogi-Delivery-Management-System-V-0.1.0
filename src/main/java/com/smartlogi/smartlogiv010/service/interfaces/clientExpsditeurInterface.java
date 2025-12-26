package com.smartlogi.smartlogiv010.service.interfaces;


import com.smartlogi.security.dto.authDto.response.UserResponse;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ClientExpediteurUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface clientExpsditeurInterface {
    UserResponse update(String id, ClientExpediteurUpdateRequestDto requestDto);
    UserResponse getById(String id);
    UserResponse findByKeyWord(String keyword);
    Page<UserResponse> getAll(Pageable pageable);
    void delete(String id);
    boolean existsById(String id);
}