package com.smartlogi.smartlogi_v0_1_0.serviceTest;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ZoneCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ZoneUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone.ZoneDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Zone.ZoneSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.entity.Zone;
import com.smartlogi.smartlogi_v0_1_0.mapper.SmartLogiMapper;
import com.smartlogi.smartlogi_v0_1_0.repository.ZoneRepository;
import com.smartlogi.smartlogi_v0_1_0.service.ZoneServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Zone Service Implementation - Complete Test Suite")
class ZoneServiceImplTest {

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private SmartLogiMapper smartLogiMapper;

    @InjectMocks
    private ZoneServiceImpl zoneService;

    private Zone zone;
    private ZoneCreateRequestDto createRequestDto;
    private ZoneUpdateRequestDto updateRequestDto;
    private ZoneSimpleResponseDto simpleResponseDto;
    private ZoneDetailedResponseDto detailedResponseDto;

    @BeforeEach
    void setUp() {
        zone = new Zone();
        zone.setId("zone-123");
        zone.setNom("Zone Test");
        zone.setCodePostal("75001");
        zone.setLivreurs(new ArrayList<>());
        zone.setColis(new ArrayList<>());

        createRequestDto = new ZoneCreateRequestDto();
        createRequestDto.setNom("Zone Test");
        createRequestDto.setCodePostal("75001");

        updateRequestDto = new ZoneUpdateRequestDto();
        updateRequestDto.setNom("Zone Updated");
        updateRequestDto.setCodePostal("75002");

        simpleResponseDto = new ZoneSimpleResponseDto();
        simpleResponseDto.setId("zone-123");
        simpleResponseDto.setNom("Zone Test");
        simpleResponseDto.setCodePostal("75001");

        detailedResponseDto = new ZoneDetailedResponseDto();
        detailedResponseDto.setId("zone-123");
        detailedResponseDto.setNom("Zone Test");
        detailedResponseDto.setCodePostal("75001");
    }

    @Test
    @DisplayName("Create - Should create zone successfully")
    void testCreate_Success() {
        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.empty());
        when(smartLogiMapper.toEntity(any(ZoneCreateRequestDto.class))).thenReturn(zone);
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);

        ZoneSimpleResponseDto result = zoneService.create(createRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("zone-123");
        assertThat(result.getNom()).isEqualTo("Zone Test");
        assertThat(result.getCodePostal()).isEqualTo("75001");

        verify(zoneRepository, times(1)).existsByCodePostal(createRequestDto.getCodePostal());
        verify(zoneRepository, times(1)).findByNom(createRequestDto.getNom());
        verify(zoneRepository, times(1)).save(any(Zone.class));
        verify(smartLogiMapper, times(1)).toEntity(createRequestDto);
        verify(smartLogiMapper, times(1)).toSimpleResponseDto(zone);
    }

    @Test
    @DisplayName("Create - Should throw exception when code postal already exists")
    void testCreate_CodePostalAlreadyExists() {
        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(true);

        assertThatThrownBy(() -> zoneService.create(createRequestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Une zone avec ce code postal existe déjà");

        verify(zoneRepository, times(1)).existsByCodePostal(createRequestDto.getCodePostal());
        verify(zoneRepository, never()).save(any(Zone.class));
    }

    @Test
    @DisplayName("Test_create should throw exeption whene nom already existe")
    void testCreate_NomExists() {
        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.of(zone));

        assertThatThrownBy(() -> zoneService.create(createRequestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Une zone avec ce nom existe déjà");

        verify(zoneRepository, times(1)).existsByCodePostal(createRequestDto.getCodePostal());
        verify(zoneRepository, times(1)).findByNom(createRequestDto.getNom());
        verify(zoneRepository, never()).save(any(Zone.class));

    }

    @Test
    @DisplayName("Create - Should handle special characters in code postal")
    void testCreate_SpecialCharactersCodePostal() {
        createRequestDto.setCodePostal("75001-@#");
        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.empty());
        when(smartLogiMapper.toEntity(any(ZoneCreateRequestDto.class))).thenReturn(zone);
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);

        ZoneSimpleResponseDto result = zoneService.create(createRequestDto);

        assertThat(result).isNotNull();
        verify(zoneRepository, times(1)).existsByCodePostal("75001-@#");
    }

    @Test
    @DisplayName("Update - Should update zone successfully")
    void testUpdate_Success() {
        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.empty());
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
        doNothing().when(smartLogiMapper).updateEntityFromDto((ZoneUpdateRequestDto) any(), any());

        ZoneSimpleResponseDto result = zoneService.update("zone-123", updateRequestDto);

        assertThat(result).isNotNull();
        verify(zoneRepository, times(1)).findById("zone-123");
        verify(smartLogiMapper, times(1)).updateEntityFromDto(updateRequestDto, zone);
        verify(zoneRepository, times(1)).save(zone);
    }

    @Test
    @DisplayName("Update - Should throw exception when zone not found")
    void testUpdate_ZoneNotFound() {
        when(zoneRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> zoneService.update("zone-999", updateRequestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Zone non trouvée");

        verify(zoneRepository, times(1)).findById("zone-999");
        verify(zoneRepository, never()).save(any(Zone.class));
    }

    @Test
    @DisplayName("Update - Should throw exception when code postal already exists")
    void testUpdate_CodePostalAlreadyExists() {
        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(true);

        assertThatThrownBy(() -> zoneService.update("zone-123", updateRequestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Une zone avec ce code postal existe déjà");

        verify(zoneRepository, never()).save(any(Zone.class));
    }

    @Test
    @DisplayName("Update - Should throw exception when nom already exists")
    void testUpdate_NomAlreadyExists() {
        Zone anotherZone = new Zone();
        anotherZone.setId("zone-456");
        anotherZone.setNom("Another Zone");

        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.of(anotherZone));

        assertThatThrownBy(() -> zoneService.update("zone-123", updateRequestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Une zone avec ce nom existe déjà");

        verify(zoneRepository, never()).save(any(Zone.class));
    }

    @Test
    @DisplayName("Update - Should allow update with same code postal")
    void testUpdate_SameCodePostal() {
        updateRequestDto.setCodePostal("75001");
        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.empty());
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
        doNothing().when(smartLogiMapper).updateEntityFromDto((ZoneUpdateRequestDto) any(), any());

        ZoneSimpleResponseDto result = zoneService.update("zone-123", updateRequestDto);

        assertThat(result).isNotNull();
        verify(zoneRepository, never()).existsByCodePostal(anyString());
        verify(zoneRepository, times(1)).save(zone);
    }

    @Test
    @DisplayName("Update - Should handle null values in update DTO")
    void testUpdate_NullValues() {
        updateRequestDto.setCodePostal(null);
        updateRequestDto.setNom(null);
        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
        doNothing().when(smartLogiMapper).updateEntityFromDto((ZoneUpdateRequestDto) any(), any());

        ZoneSimpleResponseDto result = zoneService.update("zone-123", updateRequestDto);

        assertThat(result).isNotNull();
        verify(zoneRepository, never()).existsByCodePostal(anyString());
        verify(zoneRepository, never()).findByNom(anyString());
    }

    @Test
    @DisplayName("GetById - Should get zone by id successfully")
    void testGetById_Success() {
        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);

        ZoneSimpleResponseDto result = zoneService.getById("zone-123");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("zone-123");
        verify(zoneRepository, times(1)).findById("zone-123");
        verify(smartLogiMapper, times(1)).toSimpleResponseDto(zone);
    }

    @Test
    @DisplayName("GetById - Should throw exception when zone not found")
    void testGetById_NotFound() {
        when(zoneRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> zoneService.getById("zone-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Zone non trouvée");

        verify(zoneRepository, times(1)).findById("zone-999");
    }


    @Test
    @DisplayName("GetByIdWithDetails - Should get zone with details successfully")
    void testGetByIdWithDetails_Success() {
        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
        when(smartLogiMapper.toDetailedResponseDto(any(Zone.class))).thenReturn(detailedResponseDto);

        ZoneDetailedResponseDto result = zoneService.getByIdWithDetails("zone-123");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("zone-123");
        verify(zoneRepository, times(1)).findById("zone-123");
        verify(smartLogiMapper, times(1)).toDetailedResponseDto(zone);
    }

}