package com.smartlogi.smartlogi_v0_1_0.service;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ZoneCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ZoneUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone.ZoneDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone.ZoneSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.entity.Zone;
import com.smartlogi.smartlogi_v0_1_0.mapper.SmartLogiMapper;
import com.smartlogi.smartlogi_v0_1_0.repository.ZoneRepository;
import com.smartlogi.smartlogi_v0_1_0.service.interfaces.ZoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZoneServiceImpl implements ZoneService {

    private final ZoneRepository zoneRepository;
    private final SmartLogiMapper smartLogiMapper;

    @Override
    public ZoneSimpleResponseDto create(ZoneCreateRequestDto requestDto) {
        if (zoneRepository.existsByCodePostal(requestDto.getCodePostal())) {
            throw new RuntimeException("Une zone avec ce code postal existe déjà");
        }

        if (zoneRepository.findByNom(requestDto.getNom()).isPresent()) {
            throw new RuntimeException("Une zone avec ce nom existe déjà");
        }

        Zone zone = smartLogiMapper.toEntity(requestDto);
        Zone savedZone = zoneRepository.save(zone);
        return smartLogiMapper.toSimpleResponseDto(savedZone);
    }

    @Override
    public ZoneSimpleResponseDto update(String id, ZoneUpdateRequestDto requestDto) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));

        if (requestDto.getCodePostal() != null &&
                !requestDto.getCodePostal().equals(zone.getCodePostal()) &&
                zoneRepository.existsByCodePostal(requestDto.getCodePostal())) {
            throw new RuntimeException("Une zone avec ce code postal existe déjà");
        }

        if (requestDto.getNom() != null &&
                !requestDto.getNom().equals(zone.getNom()) &&
                zoneRepository.findByNom(requestDto.getNom()).isPresent()) {
            throw new RuntimeException("Une zone avec ce nom existe déjà");
        }

        smartLogiMapper.updateEntityFromDto(requestDto, zone);
        Zone updatedZone = zoneRepository.save(zone);
        return smartLogiMapper.toSimpleResponseDto(updatedZone);
    }

    @Override
    public ZoneSimpleResponseDto getById(String id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
        return smartLogiMapper.toSimpleResponseDto(zone);
    }

    @Override
    public ZoneDetailedResponseDto getByIdWithDetails(String id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
        return smartLogiMapper.toDetailedResponseDto(zone);
    }

    @Override
    public Page<ZoneSimpleResponseDto> getAll(Pageable pageable) {
        return zoneRepository.findAll(pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    @Override
    public List<ZoneSimpleResponseDto> getAll() {
        return zoneRepository.findAll()
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ZoneSimpleResponseDto> getByCodePostal(String codePostal) {
        return zoneRepository.findByCodePostal(codePostal)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    @Override
    public Optional<ZoneSimpleResponseDto> getByNom(String nom) {
        return zoneRepository.findByNom(nom)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    @Override
    public List<ZoneSimpleResponseDto> searchByNom(String nom) {
        return zoneRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ZoneSimpleResponseDto> searchByKeyword(String keyword) {
        return zoneRepository.searchByKeyword(keyword)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByCodePostal(String codePostal) {
        return zoneRepository.existsByCodePostal(codePostal);
    }

    @Override
    public void delete(String id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));

        if (!zone.getLivreurs().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer la zone : des livreurs y sont associés");
        }

        if (!zone.getColis().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer la zone : des colis y sont associés");
        }

        zoneRepository.delete(zone);
    }

    @Override
    public boolean existsById(String id) {
        return zoneRepository.existsById(id);
    }

    @Override
    public boolean existsByNom(String nom) {
        return zoneRepository.existsByNom(nom);
    }

}