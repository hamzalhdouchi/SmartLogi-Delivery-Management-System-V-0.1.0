package com.smartlogi.smartlogiv010.serviceTest;//package com.smartlogi.smartlogiv010.serviceTest;
//
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ZoneCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ZoneUpdateRequestDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Zone.ZoneDetailedResponseDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Zone.ZoneSimpleResponseDto;
//import com.smartlogi.smartlogiv010.entity.Colis;
//import com.smartlogi.smartlogiv010.entity.Livreur;
//import com.smartlogi.smartlogiv010.entity.Zone;
//import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
//import com.smartlogi.smartlogiv010.repository.ZoneRepository;
//import com.smartlogi.smartlogiv010.service.ZoneServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("Zone Service Implementation - Complete Test Suite")
//class ZoneServiceImplTest {
//
//    @Mock
//    private ZoneRepository zoneRepository;
//
//    @Mock
//    private SmartLogiMapper smartLogiMapper;
//
//    @InjectMocks
//    private ZoneServiceImpl zoneService;
//
//    private Zone zone;
//    private ZoneCreateRequestDto createRequestDto;
//    private ZoneUpdateRequestDto updateRequestDto;
//    private ZoneSimpleResponseDto simpleResponseDto;
//    private ZoneDetailedResponseDto detailedResponseDto;
//
//    @BeforeEach
//    void setUp() {
//        zone = new Zone();
//        zone.setId("zone-123");
//        zone.setNom("Zone Test");
//        zone.setCodePostal("75001");
//        zone.setLivreurs(new ArrayList<>());
//        zone.setColis(new ArrayList<>());
//
//        createRequestDto = new ZoneCreateRequestDto();
//        createRequestDto.setNom("Zone Test");
//        createRequestDto.setCodePostal("75001");
//
//        updateRequestDto = new ZoneUpdateRequestDto();
//        updateRequestDto.setNom("Zone Updated");
//        updateRequestDto.setCodePostal("75002");
//
//        simpleResponseDto = new ZoneSimpleResponseDto();
//        simpleResponseDto.setId("zone-123");
//        simpleResponseDto.setNom("Zone Test");
//        simpleResponseDto.setCodePostal("75001");
//
//        detailedResponseDto = new ZoneDetailedResponseDto();
//        detailedResponseDto.setId("zone-123");
//        detailedResponseDto.setNom("Zone Test");
//        detailedResponseDto.setCodePostal("75001");
//    }
//
//    @Test
//    @DisplayName("Create - Should create zone successfully")
//    void testCreate_Success() {
//        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
//        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.empty());
//        when(smartLogiMapper.toEntity(any(ZoneCreateRequestDto.class))).thenReturn(zone);
//        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        ZoneSimpleResponseDto result = zoneService.create(createRequestDto);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("zone-123");
//        assertThat(result.getNom()).isEqualTo("Zone Test");
//        assertThat(result.getCodePostal()).isEqualTo("75001");
//
//        verify(zoneRepository, times(1)).existsByCodePostal(createRequestDto.getCodePostal());
//        verify(zoneRepository, times(1)).findByNom(createRequestDto.getNom());
//        verify(zoneRepository, times(1)).save(any(Zone.class));
//        verify(smartLogiMapper, times(1)).toEntity(createRequestDto);
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(zone);
//    }
//
//    @Test
//    @DisplayName("Create - Should throw exception when code postal already exists")
//    void testCreate_CodePostalAlreadyExists() {
//        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(true);
//
//        assertThatThrownBy(() -> zoneService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Une zone avec ce code postal existe déjà");
//
//        verify(zoneRepository, times(1)).existsByCodePostal(createRequestDto.getCodePostal());
//        verify(zoneRepository, never()).save(any(Zone.class));
//    }
//
//    @Test
//    @DisplayName("Test_create should throw exeption whene nom already existe")
//    void testCreate_NomExists() {
//        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
//        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.of(zone));
//
//        assertThatThrownBy(() -> zoneService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Une zone avec ce nom existe déjà");
//
//        verify(zoneRepository, times(1)).existsByCodePostal(createRequestDto.getCodePostal());
//        verify(zoneRepository, times(1)).findByNom(createRequestDto.getNom());
//        verify(zoneRepository, never()).save(any(Zone.class));
//
//    }
//
//    @Test
//    @DisplayName("Create - Should handle special characters in code postal")
//    void testCreate_SpecialCharactersCodePostal() {
//        createRequestDto.setCodePostal("75001-@#");
//        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
//        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.empty());
//        when(smartLogiMapper.toEntity(any(ZoneCreateRequestDto.class))).thenReturn(zone);
//        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        ZoneSimpleResponseDto result = zoneService.create(createRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(zoneRepository, times(1)).existsByCodePostal("75001-@#");
//    }
//
//    @Test
//    @DisplayName("Update - Should update zone successfully")
//    void testUpdate_Success() {
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
//        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.empty());
//        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ZoneUpdateRequestDto) any(), any());
//
//        ZoneSimpleResponseDto result = zoneService.update("zone-123", updateRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(zoneRepository, times(1)).findById("zone-123");
//        verify(smartLogiMapper, times(1)).updateEntityFromDto(updateRequestDto, zone);
//        verify(zoneRepository, times(1)).save(zone);
//    }
//
//    @Test
//    @DisplayName("Update - Should throw exception when zone not found")
//    void testUpdate_ZoneNotFound() {
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> zoneService.update("zone-999", updateRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Zone non trouvée");
//
//        verify(zoneRepository, times(1)).findById("zone-999");
//        verify(zoneRepository, never()).save(any(Zone.class));
//    }
//
//    @Test
//    @DisplayName("Update - Should throw exception when code postal already exists")
//    void testUpdate_CodePostalAlreadyExists() {
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(true);
//
//        assertThatThrownBy(() -> zoneService.update("zone-123", updateRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Une zone avec ce code postal existe déjà");
//
//        verify(zoneRepository, never()).save(any(Zone.class));
//    }
//
//    @Test
//    @DisplayName("Update - Should throw exception when nom already exists")
//    void testUpdate_NomAlreadyExists() {
//        Zone anotherZone = new Zone();
//        anotherZone.setId("zone-456");
//        anotherZone.setNom("Another Zone");
//
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
//        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.of(anotherZone));
//
//        assertThatThrownBy(() -> zoneService.update("zone-123", updateRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Une zone avec ce nom existe déjà");
//
//        verify(zoneRepository, never()).save(any(Zone.class));
//    }
//
//    @Test
//    @DisplayName("Update - Should allow update with same code postal")
//    void testUpdate_SameCodePostal() {
//        updateRequestDto.setCodePostal("75001");
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.empty());
//        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ZoneUpdateRequestDto) any(), any());
//
//        ZoneSimpleResponseDto result = zoneService.update("zone-123", updateRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(zoneRepository, never()).existsByCodePostal(anyString());
//        verify(zoneRepository, times(1)).save(zone);
//    }
//
//    @Test
//    @DisplayName("Update - Should handle null values in update DTO")
//    void testUpdate_NullValues() {
//        updateRequestDto.setCodePostal(null);
//        updateRequestDto.setNom(null);
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//        when(zoneRepository.save(any(Zone.class))).thenReturn(zone);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ZoneUpdateRequestDto) any(), any());
//
//        ZoneSimpleResponseDto result = zoneService.update("zone-123", updateRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(zoneRepository, never()).existsByCodePostal(anyString());
//        verify(zoneRepository, never()).findByNom(anyString());
//    }
//
//    @Test
//    @DisplayName("GetById - Should get zone by id successfully")
//    void testGetById_Success() {
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        ZoneSimpleResponseDto result = zoneService.getById("zone-123");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("zone-123");
//        verify(zoneRepository, times(1)).findById("zone-123");
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(zone);
//    }
//
//    @Test
//    @DisplayName("GetById - Should throw exception when zone not found")
//    void testGetById_NotFound() {
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> zoneService.getById("zone-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Zone non trouvée");
//
//        verify(zoneRepository, times(1)).findById("zone-999");
//    }
//
//
//    @Test
//    @DisplayName("GetByIdWithDetails - Should get zone with details successfully")
//    void testGetByIdWithDetails_Success() {
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toDetailedResponseDto(any(Zone.class))).thenReturn(detailedResponseDto);
//
//        ZoneDetailedResponseDto result = zoneService.getByIdWithDetails("zone-123");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("zone-123");
//        verify(zoneRepository, times(1)).findById("zone-123");
//        verify(smartLogiMapper, times(1)).toDetailedResponseDto(zone);
//    }
//
//
//    @Test
//    @DisplayName("GetByIdWithDetails - Should throw exception when zone not found")
//    void testGetByIdWithDetails_NotFound() {
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> zoneService.getByIdWithDetails("zone-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Zone non trouvée");
//
//        verify(zoneRepository, times(1)).findById("zone-999");
//    }
//
//
//    @Test
//    @DisplayName("GetAll - Should get all zones with pagination")
//    void testGetAllWithPagination_Success() {
//        Pageable pageable = PageRequest.of(0, 10);
//        List<Zone> zones = Arrays.asList(zone);
//        Page<Zone> zonePage = new PageImpl<>(zones, pageable, zones.size());
//
//        when(zoneRepository.findAll(any(Pageable.class))).thenReturn(zonePage);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        Page<ZoneSimpleResponseDto> result = zoneService.getAll(pageable);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).hasSize(1);
//        assertThat(result.getTotalElements()).isEqualTo(1);
//        verify(zoneRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should return empty page when no zones exist")
//    void testGetAllWithPagination_EmptyPage() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Zone> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
//
//        when(zoneRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);
//
//        Page<ZoneSimpleResponseDto> result = zoneService.getAll(pageable);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).isEmpty();
//        assertThat(result.getTotalElements()).isZero();
//        verify(zoneRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should handle multiple pages")
//    void testGetAllWithPagination_MultiplePages() {
//        Pageable pageable = PageRequest.of(1, 5);
//        List<Zone> zones = Arrays.asList(zone);
//        Page<Zone> zonePage = new PageImpl<>(zones, pageable, 10);
//
//        when(zoneRepository.findAll(any(Pageable.class))).thenReturn(zonePage);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        Page<ZoneSimpleResponseDto> result = zoneService.getAll(pageable);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getTotalPages()).isEqualTo(2);
//        assertThat(result.getNumber()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should get all zones without pagination")
//    void testGetAllList_Success() {
//        List<Zone> zones = Arrays.asList(zone);
//        when(zoneRepository.findAll()).thenReturn(zones);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        List<ZoneSimpleResponseDto> result = zoneService.getAll();
//
//        assertThat(result).isNotNull()
//                .hasSize(1);
//        verify(zoneRepository, times(1)).findAll();
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(zone);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should return empty list when no zones exist")
//    void testGetAllList_EmptyList() {
//        when(zoneRepository.findAll()).thenReturn(new ArrayList<>());
//
//        List<ZoneSimpleResponseDto> result = zoneService.getAll();
//
//        assertThat(result).isNotNull()
//                .isEmpty();
//        verify(zoneRepository, times(1)).findAll();
//    }
//
//
//    @Test
//    @DisplayName("GetAll - Should handle multiple zones")
//    void testGetAllList_MultipleZones() {
//        Zone zone2 = new Zone();
//        zone2.setId("zone-456");
//        zone2.setNom("Zone 2");
//
//        ZoneSimpleResponseDto dto2 = new ZoneSimpleResponseDto();
//        dto2.setId("zone-456");
//        dto2.setNom("Zone 2");
//
//        List<Zone> zones = Arrays.asList(zone, zone2);
//        when(zoneRepository.findAll()).thenReturn(zones);
//        when(smartLogiMapper.toSimpleResponseDto(zone)).thenReturn(simpleResponseDto);
//        when(smartLogiMapper.toSimpleResponseDto(zone2)).thenReturn(dto2);
//
//        List<ZoneSimpleResponseDto> result = zoneService.getAll();
//
//        assertThat(result).hasSize(2);
//        verify(smartLogiMapper, times(2)).toSimpleResponseDto(any(Zone.class));
//    }
//
//
//    @Test
//    @DisplayName("GetByCodePostal - Should find zone by code postal")
//    void testGetByCodePostal_Found() {
//        when(zoneRepository.findByCodePostal(anyString())).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        Optional<ZoneSimpleResponseDto> result = zoneService.getByCodePostal("75001");
//
//        assertThat(result).isPresent();
//        assertThat(result.get().getCodePostal()).isEqualTo("75001");
//        verify(zoneRepository, times(1)).findByCodePostal("75001");
//    }
//
//    @Test
//    @DisplayName("GetByCodePostal - Should return empty when zone not found")
//    void testGetByCodePostal_NotFound() {
//        when(zoneRepository.findByCodePostal(anyString())).thenReturn(Optional.empty());
//
//        Optional<ZoneSimpleResponseDto> result = zoneService.getByCodePostal("99999");
//
//        assertThat(result).isEmpty();
//        verify(zoneRepository, times(1)).findByCodePostal("99999");
//    }
//
//    @Test
//    @DisplayName("GetByNom - Should find zone by nom")
//    void testGetByNom_Found() {
//        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        Optional<ZoneSimpleResponseDto> result = zoneService.getByNom("Zone Test");
//
//        assertThat(result).isPresent();
//        assertThat(result.get().getNom()).isEqualTo("Zone Test");
//        verify(zoneRepository, times(1)).findByNom("Zone Test");
//    }
//
//    @Test
//    @DisplayName("GetByNom - Should return empty when zone not found")
//    void testGetByNom_NotFound() {
//        when(zoneRepository.findByNom(anyString())).thenReturn(Optional.empty());
//
//        Optional<ZoneSimpleResponseDto> result = zoneService.getByNom("Non Existent");
//
//        assertThat(result).isEmpty();
//        verify(zoneRepository, times(1)).findByNom("Non Existent");
//    }
//
//
//    @Test
//    @DisplayName("SearchByNom - Should find zones containing nom")
//    void testSearchByNom_Found() {
//        List<Zone> zones = Arrays.asList(zone);
//        when(zoneRepository.findByNomContainingIgnoreCase(anyString())).thenReturn(zones);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        List<ZoneSimpleResponseDto> result = zoneService.searchByNom("Test");
//
//        assertThat(result).isNotNull()
//                .hasSize(1);
//        verify(zoneRepository, times(1)).findByNomContainingIgnoreCase("Test");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should return empty list when no zones match")
//    void testSearchByNom_NotFound() {
//        when(zoneRepository.findByNomContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
//
//        List<ZoneSimpleResponseDto> result = zoneService.searchByNom("NonExistent");
//
//        assertThat(result).isNotNull()
//                .isEmpty();
//        verify(zoneRepository, times(1)).findByNomContainingIgnoreCase("NonExistent");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should be case insensitive")
//    void testSearchByNom_CaseInsensitive() {
//        List<Zone> zones = Arrays.asList(zone);
//        when(zoneRepository.findByNomContainingIgnoreCase(anyString())).thenReturn(zones);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        List<ZoneSimpleResponseDto> result = zoneService.searchByNom("test");
//
//        assertThat(result).hasSize(1);
//        verify(zoneRepository, times(1)).findByNomContainingIgnoreCase("test");
//    }
//
//
//    @Test
//    @DisplayName("SearchByKeyword - Should find zones by keyword")
//    void testSearchByKeyword_Found() {
//        List<Zone> zones = Arrays.asList(zone);
//        when(zoneRepository.searchByKeyword(anyString())).thenReturn(zones);
//        when(smartLogiMapper.toSimpleResponseDto(any(Zone.class))).thenReturn(simpleResponseDto);
//
//        List<ZoneSimpleResponseDto> result = zoneService.searchByKeyword("Test");
//
//        assertThat(result).isNotNull()
//                .hasSize(1);
//        verify(zoneRepository, times(1)).searchByKeyword("Test");
//    }
//
//    @Test
//    @DisplayName("SearchByKeyword - Should return empty list when no zones match")
//    void testSearchByKeyword_NotFound() {
//        when(zoneRepository.searchByKeyword(anyString())).thenReturn(new ArrayList<>());
//
//        List<ZoneSimpleResponseDto> result = zoneService.searchByKeyword("NonExistent");
//
//        assertThat(result).isEmpty();
//        verify(zoneRepository, times(1)).searchByKeyword("NonExistent");
//    }
//
//
//    @Test
//    @DisplayName("ExistsByCodePostal - Should return true when zone exists")
//    void testExistsByCodePostal_True() {
//        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(true);
//
//        boolean result = zoneService.existsByCodePostal("75001");
//
//        assertThat(result).isTrue();
//        verify(zoneRepository, times(1)).existsByCodePostal("75001");
//    }
//
//    @Test
//    @DisplayName("ExistsByCodePostal - Should return false when zone does not exist")
//    void testExistsByCodePostal_False() {
//        when(zoneRepository.existsByCodePostal(anyString())).thenReturn(false);
//
//        boolean result = zoneService.existsByCodePostal("99999");
//
//        assertThat(result).isFalse();
//        verify(zoneRepository, times(1)).existsByCodePostal("99999");
//    }
//
//
//    @Test
//    @DisplayName("ExistsById - Should return true when zone exists")
//    void testExistsById_True() {
//        when(zoneRepository.existsById(anyString())).thenReturn(true);
//
//        boolean result = zoneService.existsById("zone-123");
//
//        assertThat(result).isTrue();
//        verify(zoneRepository, times(1)).existsById("zone-123");
//    }
//
//    @Test
//    @DisplayName("ExistsById - Should return false when zone does not exist")
//    void testExistsById_False() {
//        when(zoneRepository.existsById(anyString())).thenReturn(false);
//
//        boolean result = zoneService.existsById("zone-999");
//
//        assertThat(result).isFalse();
//        verify(zoneRepository, times(1)).existsById("zone-999");
//    }
//
//
//    @Test
//    @DisplayName("ExistsByNom - Should return true when zone exists")
//    void testExistsByNom_True() {
//        when(zoneRepository.existsByNom(anyString())).thenReturn(true);
//
//        boolean result = zoneService.existsByNom("Zone Test");
//
//        assertThat(result).isTrue();
//        verify(zoneRepository, times(1)).existsByNom("Zone Test");
//    }
//
//    @Test
//    @DisplayName("ExistsByNom - Should return false when zone does not exist")
//    void testExistsByNom_False() {
//        when(zoneRepository.existsByNom(anyString())).thenReturn(false);
//
//        boolean result = zoneService.existsByNom("Non Existent");
//
//        assertThat(result).isFalse();
//        verify(zoneRepository, times(1)).existsByNom("Non Existent");
//    }
//
//
//    @Test
//    @DisplayName("Delete - Should delete zone successfully")
//    void testDelete_Success() {
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//        doNothing().when(zoneRepository).delete(any(Zone.class));
//
//        zoneService.delete("zone-123");
//
//        verify(zoneRepository, times(1)).findById("zone-123");
//        verify(zoneRepository, times(1)).delete(zone);
//    }
//
//    @Test
//    @DisplayName("Delete - Should throw exception when zone not found")
//    void testDelete_ZoneNotFound() {
//        // Given
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.empty());
//
//        // When & Then
//        assertThatThrownBy(() -> zoneService.delete("zone-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Zone non trouvée");
//
//        verify(zoneRepository, times(1)).findById("zone-999");
//        verify(zoneRepository, never()).delete(any(Zone.class));
//    }
//
//
//    @Test
//    @DisplayName("Delete - Should throw exception when zone has livreurs")
//    void testDelete_HasLivreurs() {
//        Livreur mockLivreur = mock(Livreur.class);
//        zone.getLivreurs().add(mockLivreur);
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//
//        assertThatThrownBy(() -> zoneService.delete("zone-123"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Impossible de supprimer la zone : des livreurs y sont associés");
//
//        verify(zoneRepository, times(1)).findById("zone-123");
//        verify(zoneRepository, never()).delete(any(Zone.class));
//    }
//
//    @Test
//    @DisplayName("Delete - Should throw exception when zone has colis")
//    void testDelete_HasColis() {
//        Colis mockColis = mock(Colis.class);
//        zone.getColis().add(mockColis);
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//
//        assertThatThrownBy(() -> zoneService.delete("zone-123"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Impossible de supprimer la zone : des colis y sont associés");
//
//        verify(zoneRepository, times(1)).findById("zone-123");
//        verify(zoneRepository, never()).delete(any(Zone.class));
//    }
//
//    @Test
//    @DisplayName("Delete - Should throw exception when zone has both livreurs and colis")
//    void testDelete_HasBothLivreursAndColis() {
//        Livreur mockLivreur = mock(Livreur.class);
//        Colis mockColis = mock(Colis.class);
//        zone.getLivreurs().add(mockLivreur);
//        zone.getColis().add(mockColis);
//        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
//
//        assertThatThrownBy(() -> zoneService.delete("zone-123"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Impossible de supprimer la zone : des livreurs y sont associés");
//
//        verify(zoneRepository, never()).delete(any(Zone.class));
//    }
//}