package com.smartlogi.smartlogiv010.service;

import com.smartlogi.security.dto.authDto.response.UserResponse;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.LivreurUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurDetailedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurSimpleResponseDto;
import com.smartlogi.smartlogiv010.entity.Livreur;
import com.smartlogi.smartlogiv010.entity.User;
import com.smartlogi.smartlogiv010.entity.Zone;
import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
import com.smartlogi.smartlogiv010.repository.UserRepository;
import com.smartlogi.smartlogiv010.repository.ZoneRepository;
import com.smartlogi.smartlogiv010.service.interfaces.LivreurService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class LivreurServiceImpl implements LivreurService {

    private final UserRepository livreurRepository;
    private final ZoneRepository zoneRepository;
    private final SmartLogiMapper smartLogiMapper;




    @Override
    public UserResponse update(String id, LivreurUpdateRequestDto requestDto) {
        User livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));

        smartLogiMapper.updateEntityFromDto(requestDto, livreur);

        if (requestDto.getZoneId() != null) {
            Zone zone = zoneRepository.findById(requestDto.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
            livreur.setZone(zone);
        }

        User updatedLivreur = livreurRepository.save(livreur);
        return smartLogiMapper.toSimpleResponseDto(updatedLivreur);
    }


    @Override
    public UserResponse getById(String id) {
        User livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        return smartLogiMapper.toSimpleResponseDto(livreur);
    }

    @Override
    public UserResponse getByIdWithStats(String id) {
        User livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        return smartLogiMapper.toAdvancedResponseDto(livreur);
    }

    @Override
    public UserResponse getByIdWithColis(String id) {
        User livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        return smartLogiMapper.toDetailedResponseDto(livreur);
    }

    @Override
    public Page<UserResponse> getAll(Pageable pageable) {
        return livreurRepository.findAll(pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    @Override
    public List<UserResponse> getAll() {
        return livreurRepository.findAll()
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getByZone(String zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
        return livreurRepository.findByZone(zone)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

//
//    @Override
//    public List<L> searchByKeyword(String keyword) {
//        return livreurRepository.searchByKeyword(keyword)
//                .stream()
//                .map(smartLogiMapper::toSimpleResponseDto)
//                .collect(Collectors.toList());
//    }


    @Override
    public void delete(String id) {
        User livreur = livreurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        livreurRepository.delete(livreur);
    }

    @Override
    public boolean existsById(String id) {
        return livreurRepository.existsById(id);
    }

}
