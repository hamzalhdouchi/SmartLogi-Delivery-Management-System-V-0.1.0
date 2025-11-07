package com.smartlogi.smartlogi_v0_1_0.service;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.DestinataireCreateDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.DestinataireUpdateDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Destinataire.DestinataireSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.entity.Destinataire;
import com.smartlogi.smartlogi_v0_1_0.exception.ArgementNotFoundExption;
import com.smartlogi.smartlogi_v0_1_0.exception.EmailAlreadyExistsException;
import com.smartlogi.smartlogi_v0_1_0.exception.ResourceNotFoundException;
import com.smartlogi.smartlogi_v0_1_0.mapper.SmartLogiMapper;
import com.smartlogi.smartlogi_v0_1_0.repository.DestinataireRepository;
import com.smartlogi.smartlogi_v0_1_0.service.interfaces.DestinataireService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DestinataireServiceImpl implements DestinataireService {

    private final DestinataireRepository destinataireRepository;
    private final SmartLogiMapper smartLogiMapper;

    @Override
    public DestinataireSimpleResponseDto create(DestinataireCreateDto requestDto) throws EmailAlreadyExistsException {

        if (destinataireRepository.existsDestinataireByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException(requestDto.getEmail());
        }
        Destinataire destinataire = smartLogiMapper.toEntity(requestDto);
        Destinataire savedDestinataire = destinataireRepository.save(destinataire);
        return smartLogiMapper.toSimpleResponseDto(savedDestinataire);
    }

    @Override
    public DestinataireSimpleResponseDto update(String id, DestinataireUpdateDto requestDto) {
        Destinataire destinataire = destinataireRepository.findById(id)
                .orElseThrow(() -> new ArgementNotFoundExption(id,"the destinataire id not found"));
        smartLogiMapper.updateEntityFromDto(requestDto, destinataire);
        Destinataire updatedDestinataire = destinataireRepository.save(destinataire);
        return smartLogiMapper.toSimpleResponseDto(updatedDestinataire);
    }

    @Override
    public DestinataireSimpleResponseDto getById(String id) {
        Destinataire destinataire = destinataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));
        return smartLogiMapper.toSimpleResponseDto(destinataire);
    }

////    @Override
//    public DestinataireAdvancedResponseDto getByIdWithColis(String id) {
//        Destinataire destinataire = destinataireRepository.findByIdWithColis(id)
//                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));
//        return smartLogiMapper.toAdvancedResponseDto(destinataire);
//    }

    @Override
    public Page<DestinataireSimpleResponseDto> getAll(Pageable pageable) {
        return destinataireRepository.findAll(pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    @Override
    public List<DestinataireSimpleResponseDto> searchByNom(String nom) {
        return destinataireRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public DestinataireSimpleResponseDto findByKeyWord(String keyword) {

        Destinataire destinataire = destinataireRepository.searchByKeyword(keyword);
        if (destinataire == null) {
            throw new ResourceNotFoundException("le Destinataire pas trouve");
        }
        DestinataireSimpleResponseDto dto = smartLogiMapper.toSimpleResponseDto(destinataire);
        return dto;
    }
    @Override
    public void delete(String id) {
        Destinataire destinataire = destinataireRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));
        destinataireRepository.delete(destinataire);
    }

    @Override
    public boolean existsById(String id) {
        return destinataireRepository.existsById(id);
    }
}