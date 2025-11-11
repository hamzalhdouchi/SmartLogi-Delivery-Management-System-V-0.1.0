package com.smartlogi.smartlogi_v0_1_0.serviceTest;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.LivreurCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.LivreurUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur.LivreurAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur.LivreurDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Livreur.LivreurSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.entity.Colis;
import com.smartlogi.smartlogi_v0_1_0.entity.Livreur;
import com.smartlogi.smartlogi_v0_1_0.entity.Zone;
import com.smartlogi.smartlogi_v0_1_0.mapper.SmartLogiMapper;
import com.smartlogi.smartlogi_v0_1_0.repository.LivreurRepository;
import com.smartlogi.smartlogi_v0_1_0.repository.ZoneRepository;
import com.smartlogi.smartlogi_v0_1_0.service.LivreurServiceImpl;
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

import java.time.LocalDateTime;
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
@DisplayName("Livreur Service Implementation - Complete Test Suite")
class LivreurServiceImplTest {

    @Mock
    private LivreurRepository livreurRepository;

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private SmartLogiMapper smartLogiMapper;

    @InjectMocks
    private LivreurServiceImpl livreurService;

    private Livreur livreur;
    private Zone zone;
    private LivreurCreateRequestDto createRequestDto;
    private LivreurUpdateRequestDto updateRequestDto;
    private LivreurSimpleResponseDto simpleResponseDto;
    private LivreurAdvancedResponseDto advancedResponseDto;
    private LivreurDetailedResponseDto detailedResponseDto;

    @BeforeEach
    void setUp() {
        zone = new Zone();
        zone.setId("zone-123");
        zone.setNom("Zone Test");
        zone.setCodePostal("75001");

        livreur = new Livreur();
        livreur.setId("livreur-123");
        livreur.setNom("Dupont");
        livreur.setPrenom("Jean");
        livreur.setTelephone("0612345678");
        livreur.setVehicule("Moto");
        livreur.setZone(zone);
        livreur.setDateCreation(LocalDateTime.now());
        livreur.setColisAssignes(new ArrayList<>());

        createRequestDto = new LivreurCreateRequestDto();
        createRequestDto.setNom("Dupont");
        createRequestDto.setPrenom("Jean");
        createRequestDto.setTelephone("0612345678");
        createRequestDto.setVehicule("Moto");
        createRequestDto.setZoneId("zone-123");

        updateRequestDto = new LivreurUpdateRequestDto();
        updateRequestDto.setNom("Martin");
        updateRequestDto.setPrenom("Pierre");
        updateRequestDto.setTelephone("0698765432");
        updateRequestDto.setVehicule("Voiture");
        updateRequestDto.setZoneId("zone-456");

        simpleResponseDto = new LivreurSimpleResponseDto();
        simpleResponseDto.setId("livreur-123");
        simpleResponseDto.setNom("Dupont");
        simpleResponseDto.setPrenom("Jean");
        simpleResponseDto.setTelephone("0612345678");

        advancedResponseDto = new LivreurAdvancedResponseDto();
        advancedResponseDto.setId("livreur-123");
        advancedResponseDto.setNom("Dupont");
        advancedResponseDto.setPrenom("Jean");

        detailedResponseDto = new LivreurDetailedResponseDto();
        detailedResponseDto.setId("livreur-123");
        detailedResponseDto.setNom("Dupont");
        detailedResponseDto.setPrenom("Jean");
    }


    @Test
    @DisplayName("Create - Should create livreur successfully with zone")
    void testCreate_Success_WithZone() {
        when(smartLogiMapper.toEntity(any(LivreurCreateRequestDto.class))).thenReturn(livreur);
        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);

        LivreurSimpleResponseDto result = livreurService.create(createRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("livreur-123");
        assertThat(result.getNom()).isEqualTo("Dupont");
        assertThat(result.getPrenom()).isEqualTo("Jean");
        assertThat(result.getTelephone()).isEqualTo("0612345678");

        verify(smartLogiMapper, times(1)).toEntity(createRequestDto);
        verify(zoneRepository, times(1)).findById("zone-123");
        verify(livreurRepository, times(1)).save(livreur);
        verify(smartLogiMapper, times(1)).toSimpleResponseDto(livreur);
    }

    @Test
    @DisplayName("Create - Should create livreur with null zone when zone not found")
    void testCreate_Success_ZoneNotFound() {
        when(smartLogiMapper.toEntity(any(LivreurCreateRequestDto.class))).thenReturn(livreur);
        when(zoneRepository.findById("zone-123")).thenReturn(Optional.empty());
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);

        LivreurSimpleResponseDto result = livreurService.create(createRequestDto);

        assertThat(result).isNotNull();
        verify(zoneRepository, times(1)).findById("zone-123");
        verify(livreurRepository, times(1)).save(livreur);
    }

    @Test
    @DisplayName("Create - Should create livreur without zone when zoneId is null")
    void testCreate_Success_NullZoneId() {
        createRequestDto.setZoneId(null);
        when(smartLogiMapper.toEntity(any(LivreurCreateRequestDto.class))).thenReturn(livreur);
        when(zoneRepository.findById(null)).thenReturn(Optional.empty());
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);

        LivreurSimpleResponseDto result = livreurService.create(createRequestDto);

        assertThat(result).isNotNull();
        verify(zoneRepository, times(1)).findById(null);
        verify(livreurRepository, times(1)).save(livreur);
    }

    @Test
    @DisplayName("Create - Should handle special characters in nom")
    void testCreate_SpecialCharacters() {
        createRequestDto.setNom("Jean-Pierre O'Connor");
        when(smartLogiMapper.toEntity(any(LivreurCreateRequestDto.class))).thenReturn(livreur);
        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);

        LivreurSimpleResponseDto result = livreurService.create(createRequestDto);

        assertThat(result).isNotNull();
        verify(livreurRepository, times(1)).save(livreur);
    }

    @Test
    @DisplayName("Create - Should handle Moroccan phone number format")
    void testCreate_MoroccanPhoneNumber() {
        createRequestDto.setTelephone("+212612345678");
        when(smartLogiMapper.toEntity(any(LivreurCreateRequestDto.class))).thenReturn(livreur);
        when(zoneRepository.findById(anyString())).thenReturn(Optional.of(zone));
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);

        LivreurSimpleResponseDto result = livreurService.create(createRequestDto);

        assertThat(result).isNotNull();
        verify(livreurRepository, times(1)).save(livreur);
    }


    @Test
    @DisplayName("Update - Should update livreur successfully with new zone")
    void testUpdate_Success_WithNewZone() {
        Zone newZone = new Zone();
        newZone.setId("zone-456");
        newZone.setNom("New Zone");

        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
        doNothing().when(smartLogiMapper).updateEntityFromDto((LivreurUpdateRequestDto) any(), any());
        when(zoneRepository.findById("zone-456")).thenReturn(Optional.of(newZone));
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);

        LivreurSimpleResponseDto result = livreurService.update("livreur-123", updateRequestDto);

        assertThat(result).isNotNull();
        verify(livreurRepository, times(1)).findById("livreur-123");
        verify(smartLogiMapper, times(1)).updateEntityFromDto(updateRequestDto, livreur);
        verify(zoneRepository, times(1)).findById("zone-456");
        verify(livreurRepository, times(1)).save(livreur);
    }

    @Test
    @DisplayName("Update - Should throw exception when livreur not found")
    void testUpdate_LivreurNotFound() {
        when(livreurRepository.findById("livreur-999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livreurService.update("livreur-999", updateRequestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livreur non trouvé");

        verify(livreurRepository, times(1)).findById("livreur-999");
        verify(livreurRepository, never()).save(any(Livreur.class));
    }

    @Test
    @DisplayName("Update - Should throw exception when zone not found")
    void testUpdate_ZoneNotFound() {
        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
        doNothing().when(smartLogiMapper).updateEntityFromDto( (LivreurUpdateRequestDto) any(), any());
        when(zoneRepository.findById("zone-456")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> livreurService.update("livreur-123", updateRequestDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Zone non trouvée");

        verify(zoneRepository, times(1)).findById("zone-456");
        verify(livreurRepository, never()).save(any(Livreur.class));
    }

    @Test
    @DisplayName("Update - Should update without changing zone when zoneId is null")
    void testUpdate_Success_NullZoneId() {
        updateRequestDto.setZoneId(null);
        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
        doNothing().when(smartLogiMapper).updateEntityFromDto((LivreurUpdateRequestDto) any(), any());
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);

        LivreurSimpleResponseDto result = livreurService.update("livreur-123", updateRequestDto);

        assertThat(result).isNotNull();
        verify(zoneRepository, never()).findById(anyString());
        verify(livreurRepository, times(1)).save(livreur);
    }

    @Test
    @DisplayName("Update - Should handle partial update with only nom changed")
    void testUpdate_PartialUpdate_OnlyNom() {

        updateRequestDto.setPrenom(null);
        updateRequestDto.setTelephone(null);
        updateRequestDto.setVehicule(null);
        updateRequestDto.setZoneId(null);

        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
        doNothing().when(smartLogiMapper).updateEntityFromDto((LivreurUpdateRequestDto) any(), any());
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        LivreurSimpleResponseDto result = livreurService.update("livreur-123", updateRequestDto);

        assertThat(result).isNotNull();
        verify(zoneRepository, never()).findById(anyString());
        verify(livreurRepository, times(1)).save(livreur);
    }

    @Test
    @DisplayName("Update - Should update all fields including zone")
    void testUpdate_AllFieldsChanged() {

        Zone newZone = new Zone();
        newZone.setId("zone-456");

        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
        doNothing().when(smartLogiMapper).updateEntityFromDto((LivreurUpdateRequestDto) any(), any());
        when(zoneRepository.findById("zone-456")).thenReturn(Optional.of(newZone));
        when(livreurRepository.save(any(Livreur.class))).thenReturn(livreur);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        LivreurSimpleResponseDto result = livreurService.update("livreur-123", updateRequestDto);

        assertThat(result).isNotNull();
        verify(smartLogiMapper, times(1)).updateEntityFromDto(updateRequestDto, livreur);
        verify(zoneRepository, times(1)).findById("zone-456");
    }


    @Test
    @DisplayName("GetById - Should get livreur by id successfully")
    void testGetById_Success() {

        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        LivreurSimpleResponseDto result = livreurService.getById("livreur-123");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("livreur-123");
        verify(livreurRepository, times(1)).findById("livreur-123");
        verify(smartLogiMapper, times(1)).toSimpleResponseDto(livreur);
    }

    @Test
    @DisplayName("GetById - Should throw exception when livreur not found")
    void testGetById_NotFound() {

        when(livreurRepository.findById("livreur-999")).thenReturn(Optional.empty());


        assertThatThrownBy(() -> livreurService.getById("livreur-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livreur non trouvé");

        verify(livreurRepository, times(1)).findById("livreur-999");
    }


    @Test
    @DisplayName("GetByIdWithStats - Should get livreur with stats successfully")
    void testGetByIdWithStats_Success() {

        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
        when(smartLogiMapper.toAdvancedResponseDto(any(Livreur.class))).thenReturn(advancedResponseDto);


        LivreurAdvancedResponseDto result = livreurService.getByIdWithStats("livreur-123");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("livreur-123");
        verify(livreurRepository, times(1)).findById("livreur-123");
        verify(smartLogiMapper, times(1)).toAdvancedResponseDto(livreur);
    }

    @Test
    @DisplayName("GetByIdWithStats - Should throw exception when livreur not found")
    void testGetByIdWithStats_NotFound() {

        when(livreurRepository.findById("livreur-999")).thenReturn(Optional.empty());


        assertThatThrownBy(() -> livreurService.getByIdWithStats("livreur-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livreur non trouvé");

        verify(livreurRepository, times(1)).findById("livreur-999");
    }


    @Test
    @DisplayName("GetByIdWithColis - Should get livreur with colis successfully")
    void testGetByIdWithColis_Success() {

        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
        when(smartLogiMapper.toDetailedResponseDto(any(Livreur.class))).thenReturn(detailedResponseDto);


        LivreurDetailedResponseDto result = livreurService.getByIdWithColis("livreur-123");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("livreur-123");
        verify(livreurRepository, times(1)).findById("livreur-123");
        verify(smartLogiMapper, times(1)).toDetailedResponseDto(livreur);
    }

    @Test
    @DisplayName("GetByIdWithColis - Should throw exception when livreur not found")
    void testGetByIdWithColis_NotFound() {

        when(livreurRepository.findById("livreur-999")).thenReturn(Optional.empty());


        assertThatThrownBy(() -> livreurService.getByIdWithColis("livreur-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livreur non trouvé");

        verify(livreurRepository, times(1)).findById("livreur-999");
    }


    @Test
    @DisplayName("GetAll - Should get all livreurs with pagination")
    void testGetAllWithPagination_Success() {

        Pageable pageable = PageRequest.of(0, 10);
        List<Livreur> livreurs = Arrays.asList(livreur);
        Page<Livreur> livreurPage = new PageImpl<>(livreurs, pageable, livreurs.size());

        when(livreurRepository.findAll(any(Pageable.class))).thenReturn(livreurPage);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        Page<LivreurSimpleResponseDto> result = livreurService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(livreurRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("GetAll - Should return empty page when no livreurs exist")
    void testGetAllWithPagination_EmptyPage() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Livreur> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        when(livreurRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);


        Page<LivreurSimpleResponseDto> result = livreurService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        verify(livreurRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("GetAll - Should handle multiple pages")
    void testGetAllWithPagination_MultiplePages() {

        Pageable pageable = PageRequest.of(1, 5);
        List<Livreur> livreurs = Arrays.asList(livreur);
        Page<Livreur> livreurPage = new PageImpl<>(livreurs, pageable, 15);

        when(livreurRepository.findAll(any(Pageable.class))).thenReturn(livreurPage);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        Page<LivreurSimpleResponseDto> result = livreurService.getAll(pageable);

        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getNumber()).isEqualTo(1);
    }


    @Test
    @DisplayName("GetAll - Should get all livreurs without pagination")
    void testGetAllList_Success() {

        List<Livreur> livreurs = Arrays.asList(livreur);
        when(livreurRepository.findAll()).thenReturn(livreurs);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        List<LivreurSimpleResponseDto> result = livreurService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(livreurRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GetAll - Should return empty list when no livreurs exist")
    void testGetAllList_EmptyList() {

        when(livreurRepository.findAll()).thenReturn(new ArrayList<>());


        List<LivreurSimpleResponseDto> result = livreurService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(livreurRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("GetAll - Should handle multiple livreurs")
    void testGetAllList_MultipleLivreurs() {

        Livreur livreur2 = new Livreur();
        livreur2.setId("livreur-456");
        livreur2.setNom("Martin");

        LivreurSimpleResponseDto dto2 = new LivreurSimpleResponseDto();
        dto2.setId("livreur-456");
        dto2.setNom("Martin");

        List<Livreur> livreurs = Arrays.asList(livreur, livreur2);
        when(livreurRepository.findAll()).thenReturn(livreurs);
        when(smartLogiMapper.toSimpleResponseDto(livreur)).thenReturn(simpleResponseDto);
        when(smartLogiMapper.toSimpleResponseDto(livreur2)).thenReturn(dto2);


        List<LivreurSimpleResponseDto> result = livreurService.getAll();

        assertThat(result).hasSize(2);
        verify(smartLogiMapper, times(2)).toSimpleResponseDto(any(Livreur.class));
    }


    @Test
    @DisplayName("GetByZone - Should get livreurs by zone successfully")
    void testGetByZone_Success() {

        List<Livreur> livreurs = Arrays.asList(livreur);
        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
        when(livreurRepository.findByZone(zone)).thenReturn(livreurs);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        List<LivreurSimpleResponseDto> result = livreurService.getByZone("zone-123");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(zoneRepository, times(1)).findById("zone-123");
        verify(livreurRepository, times(1)).findByZone(zone);
    }

    @Test
    @DisplayName("GetByZone - Should throw exception when zone not found")
    void testGetByZone_ZoneNotFound() {

        when(zoneRepository.findById("zone-999")).thenReturn(Optional.empty());


        assertThatThrownBy(() -> livreurService.getByZone("zone-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Zone non trouvée");

        verify(zoneRepository, times(1)).findById("zone-999");
        verify(livreurRepository, never()).findByZone(any(Zone.class));
    }

    @Test
    @DisplayName("GetByZone - Should return empty list when zone has no livreurs")
    void testGetByZone_EmptyList() {

        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
        when(livreurRepository.findByZone(zone)).thenReturn(new ArrayList<>());


        List<LivreurSimpleResponseDto> result = livreurService.getByZone("zone-123");

        assertThat(result).isEmpty();
        verify(livreurRepository, times(1)).findByZone(zone);
    }


    @Test
    @DisplayName("SearchByNom - Should find livreurs by nom")
    void testSearchByNom_Found() {

        List<Livreur> livreurs = Arrays.asList(livreur);
        when(livreurRepository.findByNomContainingIgnoreCase("Dupont")).thenReturn(livreurs);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        List<LivreurSimpleResponseDto> result = livreurService.searchByNom("Dupont");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(livreurRepository, times(1)).findByNomContainingIgnoreCase("Dupont");
    }

    @Test
    @DisplayName("SearchByNom - Should return empty list when no match")
    void testSearchByNom_NotFound() {

        when(livreurRepository.findByNomContainingIgnoreCase("Unknown")).thenReturn(new ArrayList<>());


        List<LivreurSimpleResponseDto> result = livreurService.searchByNom("Unknown");

        assertThat(result).isEmpty();
        verify(livreurRepository, times(1)).findByNomContainingIgnoreCase("Unknown");
    }

    @Test
    @DisplayName("SearchByNom - Should be case insensitive")
    void testSearchByNom_CaseInsensitive() {

        List<Livreur> livreurs = Arrays.asList(livreur);
        when(livreurRepository.findByNomContainingIgnoreCase("dupont")).thenReturn(livreurs);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        List<LivreurSimpleResponseDto> result = livreurService.searchByNom("dupont");
        assertThat(result).hasSize(1);
        verify(livreurRepository, times(1)).findByNomContainingIgnoreCase("dupont");
    }


    @Test
    @DisplayName("SearchByKeyword - Should find livreurs by keyword")
    void testSearchByKeyword_Found() {

        List<Livreur> livreurs = Arrays.asList(livreur);
        when(livreurRepository.searchByKeyword("Dupont")).thenReturn(livreurs);
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        List<LivreurSimpleResponseDto> result = livreurService.searchByKeyword("Dupont");

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(livreurRepository, times(1)).searchByKeyword("Dupont");
    }

    @Test
    @DisplayName("SearchByKeyword - Should return empty list when no match")
    void testSearchByKeyword_NotFound() {

        when(livreurRepository.searchByKeyword("Unknown")).thenReturn(new ArrayList<>());


        List<LivreurSimpleResponseDto> result = livreurService.searchByKeyword("Unknown");

        assertThat(result).isEmpty();
        verify(livreurRepository, times(1)).searchByKeyword("Unknown");
    }


    @Test
    @DisplayName("GetByTelephone - Should find livreur by telephone")
    void testGetByTelephone_Found() {

        when(livreurRepository.findByTelephone("0612345678")).thenReturn(Optional.of(livreur));
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        Optional<LivreurSimpleResponseDto> result = livreurService.getByTelephone("0612345678");

        assertThat(result).isPresent();
        assertThat(result.get().getTelephone()).isEqualTo("0612345678");
        verify(livreurRepository, times(1)).findByTelephone("0612345678");
    }

    @Test
    @DisplayName("GetByTelephone - Should return empty when not found")
    void testGetByTelephone_NotFound() {

        when(livreurRepository.findByTelephone("0699999999")).thenReturn(Optional.empty());


        Optional<LivreurSimpleResponseDto> result = livreurService.getByTelephone("0699999999");

        assertThat(result).isEmpty();
        verify(livreurRepository, times(1)).findByTelephone("0699999999");
    }

    @Test
    @DisplayName("GetByTelephone - Should handle Moroccan international format")
    void testGetByTelephone_InternationalFormat() {

        when(livreurRepository.findByTelephone("+212612345678")).thenReturn(Optional.of(livreur));
        when(smartLogiMapper.toSimpleResponseDto(any(Livreur.class))).thenReturn(simpleResponseDto);


        Optional<LivreurSimpleResponseDto> result = livreurService.getByTelephone("+212612345678");

        assertThat(result).isPresent();
        verify(livreurRepository, times(1)).findByTelephone("+212612345678");
    }


    @Test
    @DisplayName("CountByZone - Should count livreurs in zone")
    void testCountByZone_Success() {

        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
        when(livreurRepository.countByZone(zone)).thenReturn(5L);


        long result = livreurService.countByZone("zone-123");

        assertThat(result).isEqualTo(5L);
        verify(zoneRepository, times(1)).findById("zone-123");
        verify(livreurRepository, times(1)).countByZone(zone);
    }

    @Test
    @DisplayName("CountByZone - Should throw exception when zone not found")
    void testCountByZone_ZoneNotFound() {

        when(zoneRepository.findById("zone-999")).thenReturn(Optional.empty());


        assertThatThrownBy(() -> livreurService.countByZone("zone-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Zone non trouvée");

        verify(zoneRepository, times(1)).findById("zone-999");
        verify(livreurRepository, never()).countByZone(any(Zone.class));
    }

    @Test
    @DisplayName("CountByZone - Should return zero when zone has no livreurs")
    void testCountByZone_ZeroCount() {

        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
        when(livreurRepository.countByZone(zone)).thenReturn(0L);


        long result = livreurService.countByZone("zone-123");

        assertThat(result).isEqualTo(0L);
        verify(livreurRepository, times(1)).countByZone(zone);
    }


    @Test
    @DisplayName("Delete - Should delete livreur successfully")
    void testDelete_Success() {

        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
        doNothing().when(livreurRepository).delete(any(Livreur.class));


        livreurService.delete("livreur-123");

        verify(livreurRepository, times(1)).findById("livreur-123");
        verify(livreurRepository, times(1)).delete(livreur);
    }

    @Test
    @DisplayName("Delete - Should throw exception when livreur not found")
    void testDelete_NotFound() {

        when(livreurRepository.findById("livreur-999")).thenReturn(Optional.empty());


        assertThatThrownBy(() -> livreurService.delete("livreur-999"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Livreur non trouvé");

        verify(livreurRepository, times(1)).findById("livreur-999");
        verify(livreurRepository, never()).delete(any(Livreur.class));
    }


    @Test
    @DisplayName("ExistsById - Should return true when livreur exists")
    void testExistsById_True() {

        when(livreurRepository.existsById("livreur-123")).thenReturn(true);


        boolean result = livreurService.existsById("livreur-123");

        assertThat(result).isTrue();
        verify(livreurRepository, times(1)).existsById("livreur-123");
    }

    @Test
    @DisplayName("ExistsById - Should return false when livreur does not exist")
    void testExistsById_False() {

        when(livreurRepository.existsById("livreur-999")).thenReturn(false);


        boolean result = livreurService.existsById("livreur-999");

        assertThat(result).isFalse();
        verify(livreurRepository, times(1)).existsById("livreur-999");
    }


    @Test
    @DisplayName("ExistsByTelephone - Should return true when telephone exists")
    void testExistsByTelephone_True() {

        when(livreurRepository.existsByTelephone("0612345678")).thenReturn(true);


        boolean result = livreurService.existsByTelephone("0612345678");

        assertThat(result).isTrue();
        verify(livreurRepository, times(1)).existsByTelephone("0612345678");
    }

    @Test
    @DisplayName("ExistsByTelephone - Should return false when telephone does not exist")
    void testExistsByTelephone_False() {

        when(livreurRepository.existsByTelephone("0699999999")).thenReturn(false);


        boolean result = livreurService.existsByTelephone("0699999999");

        assertThat(result).isFalse();
        verify(livreurRepository, times(1)).existsByTelephone("0699999999");
    }

    @Test
    @DisplayName("ExistsByTelephone - Should handle international format")
    void testExistsByTelephone_InternationalFormat() {

        when(livreurRepository.existsByTelephone("+212612345678")).thenReturn(true);


        boolean result = livreurService.existsByTelephone("+212612345678");

        assertThat(result).isTrue();
        verify(livreurRepository, times(1)).existsByTelephone("+212612345678");
    }

}
