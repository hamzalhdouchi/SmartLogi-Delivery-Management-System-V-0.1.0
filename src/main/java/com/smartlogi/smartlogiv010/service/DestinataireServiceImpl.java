package com.smartlogi.smartlogiv010.service;

import com.smartlogi.security.dto.authDto.response.UserResponse;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.DestinataireUpdateDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Destinataire.DestinataireSimpleResponseDto;
import com.smartlogi.smartlogiv010.entity.Destinataire;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.smartlogiv010.exception.ArgementNotFoundExption;
import com.smartlogi.smartlogiv010.exception.ResourceNotFoundException;
import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
import com.smartlogi.smartlogiv010.repository.UserRepository;
import com.smartlogi.smartlogiv010.service.interfaces.DestinataireService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestinataireServiceImpl implements DestinataireService {

    private final UserRepository destinataireRepository;
    private final SmartLogiMapper smartLogiMapper;


    @Override
    public UserResponse update(String id, DestinataireUpdateDto requestDto) {
        User destinataire = destinataireRepository.findById(id)
                .orElseThrow(() -> new ArgementNotFoundExption(id,"the destinataire id not found"));
        smartLogiMapper.updateEntityFromDto(requestDto, destinataire);
        User updatedDestinataire = destinataireRepository.save(destinataire);
        return smartLogiMapper.toSimpleResponseDto(updatedDestinataire);
    }

    @Override
    public UserResponse getById(String id) {
        User destinataire = destinataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));
        return smartLogiMapper.toSimpleResponseDto(destinataire);
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return destinataireRepository.findAll(pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }
    

    @Override
    public UserResponse findByKeyWord(String keyword) {

        User destinataire = destinataireRepository.searchByKeyword(keyword);
        if (destinataire == null) {
            throw new ResourceNotFoundException("le Destinataire pas trouve");
        }
        UserResponse dto = smartLogiMapper.toSimpleResponseDto(destinataire);
        return dto;
    }
    @Override
    public void delete(String id) {
        User destinataire = destinataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));
        destinataireRepository.delete(destinataire);
    }

    @Override
    public boolean existsById(String id) {
        return destinataireRepository.existsById(id);
    }
}