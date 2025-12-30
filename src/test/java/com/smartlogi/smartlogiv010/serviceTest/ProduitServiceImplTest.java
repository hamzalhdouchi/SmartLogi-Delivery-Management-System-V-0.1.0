package com.smartlogi.smartlogiv010.serviceTest;//package com.smartlogi.smartlogiv010.serviceTest;
//
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ProduitCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ProduitUpdateRequestDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Produit.ProduitAdvancedResponseDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Produit.ProduitDetailedResponseDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Produit.ProduitSimpleResponseDto;
//import com.smartlogi.smartlogiv010.entity.ColisProduit;
//import com.smartlogi.smartlogiv010.entity.Produit;
//import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
//import com.smartlogi.smartlogiv010.repository.ProduitRepository;
//import com.smartlogi.smartlogiv010.service.ProduitServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.provider.Arguments;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Stream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("Produit Service Implementation - Complete Test Suite")
//class ProduitServiceImplTest {
//
//    @Mock
//    private ProduitRepository produitRepository;
//
//    @Mock
//    private SmartLogiMapper smartLogiMapper;
//
//    @InjectMocks
//    private ProduitServiceImpl produitService;
//
//    private Produit produit;
//    private ProduitCreateRequestDto createRequestDto;
//    private ProduitUpdateRequestDto updateRequestDto;
//    private ProduitSimpleResponseDto simpleResponseDto;
//    private ProduitAdvancedResponseDto advancedResponseDto;
//    private ProduitDetailedResponseDto detailedResponseDto;
//
//    @BeforeEach
//    void setUp() {
//        produit = new Produit();
//        produit.setId("produit-123");
//        produit.setNom("Laptop HP");
//        produit.setCategorie("Electronique");
//        produit.setPoids(new BigDecimal("2.5"));
//        produit.setPrix(new BigDecimal("5999.99"));
//        produit.setDateCreation(LocalDateTime.now());
//
//        createRequestDto = new ProduitCreateRequestDto();
//        createRequestDto.setNom("Laptop HP");
//        createRequestDto.setCategorie("Electronique");
//        createRequestDto.setPoids(new BigDecimal("2.5"));
//        createRequestDto.setPrix(new BigDecimal("5999.99"));
//
//        updateRequestDto = new ProduitUpdateRequestDto();
//        updateRequestDto.setId("produit-123");
//        updateRequestDto.setNom("Laptop Dell");
//        updateRequestDto.setCategorie("Informatique");
//        updateRequestDto.setPoids(new BigDecimal("3.0"));
//        updateRequestDto.setPrix(new BigDecimal("6999.99"));
//
//        simpleResponseDto = new ProduitSimpleResponseDto();
//        simpleResponseDto.setId("produit-123");
//        simpleResponseDto.setNom("Laptop HP");
//        simpleResponseDto.setCategorie("Electronique");
//        simpleResponseDto.setPoids(new BigDecimal("2.5"));
//        simpleResponseDto.setPrix(new BigDecimal("5999.99"));
//        simpleResponseDto.setDateCreation(LocalDateTime.now());
//
//        advancedResponseDto = new ProduitAdvancedResponseDto();
//        advancedResponseDto.setId("produit-123");
//        advancedResponseDto.setNom("Laptop HP");
//
//        detailedResponseDto = new ProduitDetailedResponseDto();
//        detailedResponseDto.setId("produit-123");
//        detailedResponseDto.setNom("Laptop HP");
//    }
//
//
//    @Test
//    @DisplayName("Create - Should create produit successfully")
//    void testCreate_Success() {
//
//        when(produitRepository.findByNomContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
//        when(smartLogiMapper.toEntity(any(ProduitCreateRequestDto.class))).thenReturn(produit);
//        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        ProduitSimpleResponseDto result = produitService.create(createRequestDto);
//
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("produit-123");
//        assertThat(result.getNom()).isEqualTo("Laptop HP");
//        assertThat(result.getPrix()).isEqualTo(new BigDecimal("5999.99"));
//
//        verify(produitRepository, times(1)).findByNomContainingIgnoreCase(createRequestDto.getNom());
//        verify(smartLogiMapper, times(1)).toEntity(createRequestDto);
//        verify(produitRepository, times(1)).save(produit);
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(produit);
//    }
//
//    @Test
//    @DisplayName("Create - Should throw exception when nom already exists")
//    void testCreate_NomAlreadyExists() {
//
//        Produit existingProduit = new Produit();
//        existingProduit.setNom("Laptop HP");
//        when(produitRepository.findByNomContainingIgnoreCase(anyString()))
//                .thenReturn(Arrays.asList(existingProduit));
//
//
//        assertThatThrownBy(() -> produitService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Un produit avec ce nom existe déjà");
//
//        verify(produitRepository, times(1)).findByNomContainingIgnoreCase(createRequestDto.getNom());
//        verify(produitRepository, never()).save(any(Produit.class));
//    }
//
//    @Test
//    @DisplayName("Create - Should throw exception when nom exists case insensitive")
//    void testCreate_NomExistsCaseInsensitive() {
//
//        Produit existingProduit = new Produit();
//        existingProduit.setNom("LAPTOP HP");
//        when(produitRepository.findByNomContainingIgnoreCase(anyString()))
//                .thenReturn(Arrays.asList(existingProduit));
//
//
//        assertThatThrownBy(() -> produitService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Un produit avec ce nom existe déjà");
//
//        verify(produitRepository, never()).save(any(Produit.class));
//    }
//
//    @Test
//    @DisplayName("Create - Should handle special characters in nom")
//    void testCreate_SpecialCharacters() {
//
//        createRequestDto.setNom("Laptop HP-2024, Model & Series");
//        when(produitRepository.findByNomContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
//        when(smartLogiMapper.toEntity(any(ProduitCreateRequestDto.class))).thenReturn(produit);
//        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        ProduitSimpleResponseDto result = produitService.create(createRequestDto);
//
//
//        assertThat(result).isNotNull();
//        verify(produitRepository, times(1)).save(produit);
//    }
//
//    @Test
//    @DisplayName("Create - Should handle minimal price and weight")
//    void testCreate_MinimalValues() {
//
//        createRequestDto.setPrix(new BigDecimal("0.01"));
//        createRequestDto.setPoids(new BigDecimal("0.01"));
//        when(produitRepository.findByNomContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
//        when(smartLogiMapper.toEntity(any(ProduitCreateRequestDto.class))).thenReturn(produit);
//        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        ProduitSimpleResponseDto result = produitService.create(createRequestDto);
//
//
//        assertThat(result).isNotNull();
//        verify(produitRepository, times(1)).save(produit);
//    }
//
//    @Test
//    @DisplayName("Create - Should handle large price values")
//    void testCreate_LargePriceValues() {
//
//        createRequestDto.setPrix(new BigDecimal("99999999.99"));
//        when(produitRepository.findByNomContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
//        when(smartLogiMapper.toEntity(any(ProduitCreateRequestDto.class))).thenReturn(produit);
//        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        ProduitSimpleResponseDto result = produitService.create(createRequestDto);
//
//
//        assertThat(result).isNotNull();
//        verify(produitRepository, times(1)).save(produit);
//    }
//
//
//    @Test
//    @DisplayName("Update - Should update produit successfully")
//    void testUpdate_Success() {
//
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        when(produitRepository.findByNomContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ProduitUpdateRequestDto) any(), any());
//        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        ProduitSimpleResponseDto result = produitService.update("produit-123", updateRequestDto);
//
//
//        assertThat(result).isNotNull();
//        verify(produitRepository, times(1)).findById("produit-123");
//        verify(smartLogiMapper, times(1)).updateEntityFromDto(updateRequestDto, produit);
//        verify(produitRepository, times(1)).save(produit);
//    }
//
//    @Test
//    @DisplayName("Update - Should throw exception when produit not found")
//    void testUpdate_ProduitNotFound() {
//
//        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());
//
//
//        assertThatThrownBy(() -> produitService.update("produit-999", updateRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Produit non trouvé");
//
//        verify(produitRepository, times(1)).findById("produit-999");
//        verify(produitRepository, never()).save(any(Produit.class));
//    }
//
//    @Test
//    @DisplayName("Update - Should throw exception when nom already exists")
//    void testUpdate_NomAlreadyExists() {
//
//        Produit anotherProduit = new Produit();
//        anotherProduit.setId("produit-456");
//        anotherProduit.setNom("Laptop Dell");
//
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        when(produitRepository.findByNomContainingIgnoreCase(anyString()))
//                .thenReturn(Arrays.asList(anotherProduit));
//
//
//        assertThatThrownBy(() -> produitService.update("produit-123", updateRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Un produit avec ce nom existe déjà");
//
//        verify(produitRepository, never()).save(any(Produit.class));
//    }
//
//    @Test
//    @DisplayName("Update - Should allow update with same nom")
//    void testUpdate_SameNom() {
//
//        updateRequestDto.setNom("Laptop HP");
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ProduitUpdateRequestDto) any(), any());
//        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        ProduitSimpleResponseDto result = produitService.update("produit-123", updateRequestDto);
//
//
//        assertThat(result).isNotNull();
//        verify(produitRepository, never()).findByNomContainingIgnoreCase(anyString());
//        verify(produitRepository, times(1)).save(produit);
//    }
//
//    @Test
//    @DisplayName("Update - Should handle null nom")
//    void testUpdate_NullNom() {
//
//        updateRequestDto.setNom(null);
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ProduitUpdateRequestDto) any(), any());
//        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        ProduitSimpleResponseDto result = produitService.update("produit-123", updateRequestDto);
//
//
//        assertThat(result).isNotNull();
//        verify(produitRepository, never()).findByNomContainingIgnoreCase(anyString());
//        verify(produitRepository, times(1)).save(produit);
//    }
//
//    @Test
//    @DisplayName("Update - Should handle partial update")
//    void testUpdate_PartialUpdate() {
//
//        updateRequestDto.setCategorie(null);
//        updateRequestDto.setPoids(null);
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        when(produitRepository.findByNomContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ProduitUpdateRequestDto) any(), any());
//        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        ProduitSimpleResponseDto result = produitService.update("produit-123", updateRequestDto);
//
//
//        assertThat(result).isNotNull();
//        verify(produitRepository, times(1)).save(produit);
//    }
//
//
//    @Test
//    @DisplayName("GetById - Should get produit by id successfully")
//    void testGetById_Success() {
//
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        ProduitSimpleResponseDto result = produitService.getById("produit-123");
//
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("produit-123");
//        verify(produitRepository, times(1)).findById("produit-123");
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(produit);
//    }
//
//    @Test
//    @DisplayName("GetById - Should throw exception when produit not found")
//    void testGetById_NotFound() {
//
//        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());
//
//
//        assertThatThrownBy(() -> produitService.getById("produit-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Produit non trouvé");
//
//        verify(produitRepository, times(1)).findById("produit-999");
//    }
//
//
//    @Test
//    @DisplayName("GetByIdWithStats - Should get produit with stats successfully")
//    void testGetByIdWithStats_Success() {
//
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        when(smartLogiMapper.toAdvancedResponseDto(any(Produit.class))).thenReturn(advancedResponseDto);
//
//
//        ProduitAdvancedResponseDto result = produitService.getByIdWithStats("produit-123");
//
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("produit-123");
//        verify(produitRepository, times(1)).findById("produit-123");
//        verify(smartLogiMapper, times(1)).toAdvancedResponseDto(produit);
//    }
//
//    @Test
//    @DisplayName("GetByIdWithStats - Should throw exception when produit not found")
//    void testGetByIdWithStats_NotFound() {
//
//        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());
//
//
//        assertThatThrownBy(() -> produitService.getByIdWithStats("produit-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Produit non trouvé");
//
//        verify(produitRepository, times(1)).findById("produit-999");
//    }
//
//
//    @Test
//    @DisplayName("GetByIdWithColis - Should get produit with colis successfully")
//    void testGetByIdWithColis_Success() {
//
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        when(smartLogiMapper.toDetailedResponseDto(any(Produit.class))).thenReturn(detailedResponseDto);
//
//
//        ProduitDetailedResponseDto result = produitService.getByIdWithColis("produit-123");
//
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("produit-123");
//        verify(produitRepository, times(1)).findById("produit-123");
//        verify(smartLogiMapper, times(1)).toDetailedResponseDto(produit);
//    }
//
//    @Test
//    @DisplayName("GetByIdWithColis - Should throw exception when produit not found")
//    void testGetByIdWithColis_NotFound() {
//
//        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());
//
//
//        assertThatThrownBy(() -> produitService.getByIdWithColis("produit-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Produit non trouvé");
//
//        verify(produitRepository, times(1)).findById("produit-999");
//    }
//
//
//    @Test
//    @DisplayName("GetAll - Should get all produits with pagination")
//    void testGetAllWithPagination_Success() {
//
//        Pageable pageable = PageRequest.of(0, 10);
//        List<Produit> produits = Arrays.asList(produit);
//        Page<Produit> produitPage = new PageImpl<>(produits, pageable, produits.size());
//
//        when(produitRepository.findAll(any(Pageable.class))).thenReturn(produitPage);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        Page<ProduitSimpleResponseDto> result = produitService.getAll(pageable);
//
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).hasSize(1);
//        assertThat(result.getTotalElements()).isEqualTo(1);
//        verify(produitRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should return empty page when no produits exist")
//    void testGetAllWithPagination_EmptyPage() {
//
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Produit> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
//
//        when(produitRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);
//
//
//        Page<ProduitSimpleResponseDto> result = produitService.getAll(pageable);
//
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).isEmpty();
//        verify(produitRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should handle multiple pages")
//    void testGetAllWithPagination_MultiplePages() {
//
//        Pageable pageable = PageRequest.of(1, 5);
//        List<Produit> produits = Arrays.asList(produit);
//        Page<Produit> produitPage = new PageImpl<>(produits, pageable, 20);
//
//        when(produitRepository.findAll(any(Pageable.class))).thenReturn(produitPage);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        Page<ProduitSimpleResponseDto> result = produitService.getAll(pageable);
//
//
//        assertThat(result.getTotalPages()).isEqualTo(4);
//        assertThat(result.getNumber()).isEqualTo(1);
//    }
//
//
//    @Test
//    @DisplayName("GetAll - Should get all produits without pagination")
//    void testGetAllList_Success() {
//
//        List<Produit> produits = Arrays.asList(produit);
//        when(produitRepository.findAll()).thenReturn(produits);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        List<ProduitSimpleResponseDto> result = produitService.getAll();
//
//
//        assertThat(result).isNotNull()
//                .hasSize(1);
//        verify(produitRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("GetAll - Should return empty list when no produits exist")
//    void testGetAllList_EmptyList() {
//
//        when(produitRepository.findAll()).thenReturn(new ArrayList<>());
//
//
//        List<ProduitSimpleResponseDto> result = produitService.getAll();
//
//
//        assertThat(result).isNotNull()
//                .isEmpty();
//        verify(produitRepository, times(1)).findAll();
//    }
//
//    @Test
//    @DisplayName("GetAll - Should handle multiple produits")
//    void testGetAllList_MultipleProduits() {
//
//        Produit produit2 = new Produit();
//        produit2.setId("produit-456");
//        produit2.setNom("Mouse");
//
//        ProduitSimpleResponseDto dto2 = new ProduitSimpleResponseDto();
//        dto2.setId("produit-456");
//        dto2.setNom("Mouse");
//
//        List<Produit> produits = Arrays.asList(produit, produit2);
//        when(produitRepository.findAll()).thenReturn(produits);
//        when(smartLogiMapper.toSimpleResponseDto(produit)).thenReturn(simpleResponseDto);
//        when(smartLogiMapper.toSimpleResponseDto(produit2)).thenReturn(dto2);
//
//
//        List<ProduitSimpleResponseDto> result = produitService.getAll();
//
//
//        assertThat(result).hasSize(2);
//        verify(smartLogiMapper, times(2)).toSimpleResponseDto(any(Produit.class));
//    }
//
//
//    @Test
//    @DisplayName("SearchByNom - Should find produits by nom")
//    void testSearchByNom_Found() {
//
//        List<Produit> produits = Arrays.asList(produit);
//        when(produitRepository.findByNomContainingIgnoreCase("Laptop")).thenReturn(produits);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        List<ProduitSimpleResponseDto> result = produitService.searchByNom("Laptop");
//
//
//        assertThat(result).isNotNull()
//                .hasSize(1);
//        verify(produitRepository, times(1)).findByNomContainingIgnoreCase("Laptop");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should return empty list when no match")
//    void testSearchByNom_NotFound() {
//
//        when(produitRepository.findByNomContainingIgnoreCase("Unknown")).thenReturn(new ArrayList<>());
//
//
//        List<ProduitSimpleResponseDto> result = produitService.searchByNom("Unknown");
//
//
//        assertThat(result).isEmpty();
//        verify(produitRepository, times(1)).findByNomContainingIgnoreCase("Unknown");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should be case insensitive")
//    void testSearchByNom_CaseInsensitive() {
//
//        List<Produit> produits = Arrays.asList(produit);
//        when(produitRepository.findByNomContainingIgnoreCase("laptop")).thenReturn(produits);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        List<ProduitSimpleResponseDto> result = produitService.searchByNom("laptop");
//
//
//        assertThat(result).hasSize(1);
//        verify(produitRepository, times(1)).findByNomContainingIgnoreCase("laptop");
//    }
//
//
//    @Test
//    @DisplayName("GetByCategorie - Should find produits by categorie")
//    void testGetByCategorie_Found() {
//
//        List<Produit> produits = Arrays.asList(produit);
//        when(produitRepository.findByCategorie("Electronique")).thenReturn(produits);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        List<ProduitSimpleResponseDto> result = produitService.getByCategorie("Electronique");
//
//
//        assertThat(result).isNotNull()
//                .hasSize(1);
//        verify(produitRepository, times(1)).findByCategorie("Electronique");
//    }
//
//    @Test
//    @DisplayName("GetByCategorie - Should return empty list when categorie not found")
//    void testGetByCategorie_NotFound() {
//
//        when(produitRepository.findByCategorie("Unknown")).thenReturn(new ArrayList<>());
//
//
//        List<ProduitSimpleResponseDto> result = produitService.getByCategorie("Unknown");
//
//
//        assertThat(result).isEmpty();
//        verify(produitRepository, times(1)).findByCategorie("Unknown");
//    }
//
//
//    @Test
//    @DisplayName("SearchByKeyword - Should find produits by keyword")
//    void testSearchByKeyword_Found() {
//
//        List<Produit> produits = Arrays.asList(produit);
//        when(produitRepository.searchByKeyword("HP")).thenReturn(produits);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        List<ProduitSimpleResponseDto> result = produitService.searchByKeyword("HP");
//
//
//        assertThat(result).isNotNull()
//                .hasSize(1);
//        verify(produitRepository, times(1)).searchByKeyword("HP");
//    }
//
//    @Test
//    @DisplayName("SearchByKeyword - Should return empty list when no match")
//    void testSearchByKeyword_NotFound() {
//
//        when(produitRepository.searchByKeyword("Unknown")).thenReturn(new ArrayList<>());
//
//
//        List<ProduitSimpleResponseDto> result = produitService.searchByKeyword("Unknown");
//
//
//        assertThat(result).isEmpty();
//        verify(produitRepository, times(1)).searchByKeyword("Unknown");
//    }
//
//    // ==================== GET BY PRIX BETWEEN TESTS ====================
//
//    @Test
//    @DisplayName("GetByPrixBetween - Should find produits in price range")
//    void testGetByPrixBetween_Success() {
//
//        BigDecimal prixMin = new BigDecimal("1000.00");
//        BigDecimal prixMax = new BigDecimal("10000.00");
//        List<Produit> produits = Arrays.asList(produit);
//
//        when(produitRepository.findByPrixBetween(prixMin, prixMax)).thenReturn(produits);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        List<ProduitSimpleResponseDto> result = produitService.getByPrixBetween(prixMin, prixMax);
//
//
//        assertThat(result).isNotNull()
//                .hasSize(1);
//        verify(produitRepository, times(1)).findByPrixBetween(prixMin, prixMax);
//    }
//
//    @Test
//    @DisplayName("GetByPrixBetween - Should throw exception when prixMin is null")
//    void testGetByPrixBetween_NullPrixMin() {
//
//        assertThatThrownBy(() -> produitService.getByPrixBetween(null, new BigDecimal("1000")))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Les prix minimum et maximum sont obligatoires");
//
//        verify(produitRepository, never()).findByPrixBetween(any(), any());
//    }
//
//    @Test
//    @DisplayName("GetByPrixBetween - Should throw exception when prixMax is null")
//    void testGetByPrixBetween_NullPrixMax() {
//
//        assertThatThrownBy(() -> produitService.getByPrixBetween(null, new BigDecimal("1000")))                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Les prix minimum et maximum sont obligatoires");
//
//        verify(produitRepository, never()).findByPrixBetween(any(), any());
//    }
//
//    void testGetByPrixBetween_InvalidPriceRanges(BigDecimal prixMin, BigDecimal prixMax, String expectedMessage) {
//        assertThatThrownBy(() -> produitService.getByPrixBetween(prixMin, prixMax))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage(expectedMessage);
//        verify(produitRepository, never()).findByPrixBetween(any(), any());
//    }
//
//    private static Stream<Arguments> invalidPriceRanges() {
//        return Stream.of(
//                Arguments.of(new BigDecimal("100.0"), new BigDecimal("50.0"), "prixMin must be less than or equal to prixMax"),
//                Arguments.of(new BigDecimal("200.0"), new BigDecimal("100.0"), "prixMin must be less than or equal to prixMax"),
//                Arguments.of(new BigDecimal("999.99"), new BigDecimal("10.50"), "prixMin must be less than or equal to prixMax")
//        );
//    }
//
//    @Test
//    @DisplayName("GetByPrixBetween - Should throw exception when prixMin is negative")
//    void testGetByPrixBetween_NegativePrixMin() {
//
//        BigDecimal prixMin = new BigDecimal("-100.00");
//        BigDecimal prixMax = new BigDecimal("1000.00");
//
//
//        assertThatThrownBy(() -> produitService.getByPrixBetween(prixMin, prixMax))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Les prix ne peuvent pas être négatifs");
//
//        verify(produitRepository, never()).findByPrixBetween(any(), any());
//    }
//
//    @Test
//    @DisplayName("GetByPrixBetween - Should throw exception when prixMax is negative")
//    void testGetByPrixBetween_NegativePrixMax() {
//
//        BigDecimal prixMin = new BigDecimal("100.00");
//        BigDecimal prixMax = new BigDecimal("-1000.00");
//
//
//        assertThatThrownBy(() -> produitService.getByPrixBetween(prixMin, prixMax))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Le prix minimum ne peut pas être supérieur au prix maximum");
//
//        verify(produitRepository, never()).findByPrixBetween(any(), any());
//    }
//
//    @Test
//    @DisplayName("GetByPrixBetween - Should handle equal min and max prices")
//    void testGetByPrixBetween_EqualPrices() {
//
//        BigDecimal prix = new BigDecimal("5999.99");
//        List<Produit> produits = Arrays.asList(produit);
//
//        when(produitRepository.findByPrixBetween(prix, prix)).thenReturn(produits);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        List<ProduitSimpleResponseDto> result = produitService.getByPrixBetween(prix, prix);
//
//
//        assertThat(result).hasSize(1);
//        verify(produitRepository, times(1)).findByPrixBetween(prix, prix);
//    }
//
//    @Test
//    @DisplayName("GetByPrixBetween - Should handle zero prices")
//    void testGetByPrixBetween_ZeroPrices() {
//
//        BigDecimal prixMin = BigDecimal.ZERO;
//        BigDecimal prixMax = new BigDecimal("100.00");
//        List<Produit> produits = Arrays.asList(produit);
//
//        when(produitRepository.findByPrixBetween(prixMin, prixMax)).thenReturn(produits);
//        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class))).thenReturn(simpleResponseDto);
//
//
//        List<ProduitSimpleResponseDto> result = produitService.getByPrixBetween(prixMin, prixMax);
//
//
//        assertThat(result).hasSize(1);
//        verify(produitRepository, times(1)).findByPrixBetween(prixMin, prixMax);
//    }
//
//    @Test
//    @DisplayName("GetByPrixBetween - Should return empty list when no produits in range")
//    void testGetByPrixBetween_EmptyResult() {
//
//        BigDecimal prixMin = new BigDecimal("1.00");
//        BigDecimal prixMax = new BigDecimal("10.00");
//
//        when(produitRepository.findByPrixBetween(prixMin, prixMax)).thenReturn(new ArrayList<>());
//
//
//        List<ProduitSimpleResponseDto> result = produitService.getByPrixBetween(prixMin, prixMax);
//
//
//        assertThat(result).isEmpty();
//        verify(produitRepository, times(1)).findByPrixBetween(prixMin, prixMax);
//    }
//
//    // ==================== GET ALL CATEGORIES TESTS ====================
//
//    @Test
//    @DisplayName("GetAllCategories - Should return all categories")
//    void testGetAllCategories_Success() {
//
//        List<String> categories = Arrays.asList("Electronique", "Informatique", "Mobilier");
//        when(produitRepository.findAllCategories()).thenReturn(categories);
//
//
//        List<String> result = produitService.getAllCategories();
//
//
//        assertThat(result).isNotNull()
//                .hasSize(3);
//        assertThat(result).contains("Electronique", "Informatique", "Mobilier");
//        verify(produitRepository, times(1)).findAllCategories();
//    }
//
//    @Test
//    @DisplayName("GetAllCategories - Should return empty list when no categories")
//    void testGetAllCategories_EmptyList() {
//
//        when(produitRepository.findAllCategories()).thenReturn(new ArrayList<>());
//
//
//        List<String> result = produitService.getAllCategories();
//
//
//        assertThat(result).isEmpty();
//        verify(produitRepository, times(1)).findAllCategories();
//    }
//
//    // ==================== DELETE TESTS ====================
//
//    @Test
//    @DisplayName("Delete - Should delete produit successfully")
//    void testDelete_Success() {
//
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        doNothing().when(produitRepository).delete(any(Produit.class));
//
//
//        produitService.delete("produit-123");
//
//
//        verify(produitRepository, times(1)).findById("produit-123");
//        verify(produitRepository, times(1)).delete(produit);
//    }
//
//    @Test
//    @DisplayName("Delete - Should throw exception when produit not found")
//    void testDelete_NotFound() {
//
//        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());
//
//
//        assertThatThrownBy(() -> produitService.delete("produit-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Produit non trouvé");
//
//        verify(produitRepository, times(1)).findById("produit-999");
//        verify(produitRepository, never()).delete(any(Produit.class));
//    }
//
//    @Test
//    @DisplayName("Delete - Should throw exception when produit has associated colis")
//    void testDelete_HasColisProduits() {
//
//        ColisProduit mockproductColis = mock(ColisProduit.class);
//        produit.getColisProduits().add(mockproductColis);
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//
//
//        assertThatThrownBy(() -> produitService.delete("produit-123"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Impossible de supprimer le produit : il est associé à des colis");
//
//        verify(produitRepository, times(1)).findById("produit-123");
//        verify(produitRepository, never()).delete(any(Produit.class));
//    }
//
//    // ==================== EXISTS BY ID TESTS ====================
//
//    @Test
//    @DisplayName("ExistsById - Should return true when produit exists")
//    void testExistsById_True() {
//
//        when(produitRepository.existsById("produit-123")).thenReturn(true);
//
//
//        boolean result = produitService.existsById("produit-123");
//
//
//        assertThat(result).isTrue();
//        verify(produitRepository, times(1)).existsById("produit-123");
//    }
//
//    @Test
//    @DisplayName("ExistsById - Should return false when produit does not exist")
//    void testExistsById_False() {
//
//        when(produitRepository.existsById("produit-999")).thenReturn(false);
//
//
//        boolean result = produitService.existsById("produit-999");
//
//
//        assertThat(result).isFalse();
//        verify(produitRepository, times(1)).existsById("produit-999");
//    }
//}
