package com.smartlogi.smartlogiv010.service;

import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.LivreurCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.LivreurUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurDetailedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurSimpleResponseDto;
import com.smartlogi.smartlogiv010.entity.Livreur;
import com.smartlogi.smartlogiv010.entity.Zone;
import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
import com.smartlogi.smartlogiv010.repository.LivreurRepository;
import com.smartlogi.smartlogiv010.repository.ZoneRepository;
import com.smartlogi.smartlogiv010.service.interfaces.LivreurService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LivreurServiceImpl implements LivreurService {

    private final LivreurRepository livreurRepository;
    private final ZoneRepository zoneRepository;
    private final SmartLogiMapper smartLogiMapper;

    @Override
    public LivreurSimpleResponseDto create(LivreurCreateRequestDto requestDto) {
        Livreur livreur = smartLogiMapper.toEntity(requestDto);

        Zone zone = zoneRepository.findById(requestDto.getZoneId())
                .orElse(null);
        livreur.setZone(zone);

        Livreur savedLivreur = livreurRepository.save(livreur);
        return smartLogiMapper.toSimpleResponseDto(savedLivreur);
    }


    @Override
    public LivreurSimpleResponseDto update(String id, LivreurUpdateRequestDto requestDto) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));

        smartLogiMapper.updateEntityFromDto(requestDto, livreur);

        if (requestDto.getZoneId() != null) {
            Zone zone = zoneRepository.findById(requestDto.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
            livreur.setZone(zone);
        }

        Livreur updatedLivreur = livreurRepository.save(livreur);
        return smartLogiMapper.toSimpleResponseDto(updatedLivreur);
    }


    @Override
    public LivreurSimpleResponseDto getById(String id) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        return smartLogiMapper.toSimpleResponseDto(livreur);
    }

    @Override
    public LivreurAdvancedResponseDto getByIdWithStats(String id) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        return smartLogiMapper.toAdvancedResponseDto(livreur);
    }

    @Override
    public LivreurDetailedResponseDto getByIdWithColis(String id) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        return smartLogiMapper.toDetailedResponseDto(livreur);
    }

    @Override
    public Page<LivreurSimpleResponseDto> getAll(Pageable pageable) {
        return livreurRepository.findAll(pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    @Override
    public List<LivreurSimpleResponseDto> getAll() {
        return livreurRepository.findAll()
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LivreurSimpleResponseDto> getByZone(String zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
        return livreurRepository.findByZone(zone)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LivreurSimpleResponseDto> searchByNom(String nom) {
        return livreurRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LivreurSimpleResponseDto> searchByKeyword(String keyword) {
        return livreurRepository.searchByKeyword(keyword)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<LivreurSimpleResponseDto> getByTelephone(String telephone) {
        return livreurRepository.findByTelephone(telephone)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    @Override
    public long countByZone(String zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
        return livreurRepository.countByZone(zone);
    }

    @Override
    public void delete(String id) {
        Livreur livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        livreurRepository.delete(livreur);
    }

    @Override
    public boolean existsById(String id) {
        return livreurRepository.existsById(id);
    }


    @Override
    public boolean existsByTelephone(String telephone) {
        return livreurRepository.existsByTelephone(telephone);
    }
}
