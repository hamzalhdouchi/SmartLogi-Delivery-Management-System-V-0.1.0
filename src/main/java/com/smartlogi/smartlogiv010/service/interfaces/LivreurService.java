package com.smartlogi.smartlogiv010.service.interfaces;

import com.smartlogi.security.dto.authDto.response.UserResponse;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.LivreurUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurDetailedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LivreurService {

    UserResponse update(String id, LivreurUpdateRequestDto requestDto);
    UserResponse getById(String id);
    void delete(String id);
    boolean existsById(String id);
    UserResponse getByIdWithStats(String id);
    UserResponse getByIdWithColis(String id);
    Page<UserResponse> getAll(Pageable pageable);
    List<UserResponse> getAll();
    List<UserResponse> getByZone(String zoneId);
//    List<UserResponse> searchByKeyword(String keyword);
}