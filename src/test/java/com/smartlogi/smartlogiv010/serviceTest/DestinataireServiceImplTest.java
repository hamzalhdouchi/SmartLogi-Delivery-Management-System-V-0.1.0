package com.smartlogi.smartlogiv010.serviceTest;//package com.smartlogi.smartlogiv010.serviceTest;
//
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.DestinataireCreateDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.DestinataireUpdateDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Destinataire.DestinataireSimpleResponseDto;
//import com.smartlogi.smartlogiv010.entity.Destinataire;
//import com.smartlogi.smartlogiv010.exception.ArgementNotFoundExption;
//import com.smartlogi.smartlogiv010.exception.EmailAlreadyExistsException;
//import com.smartlogi.smartlogiv010.exception.ResourceNotFoundException;
//import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
//import com.smartlogi.smartlogiv010.repository.DestinataireRepository;
//import com.smartlogi.smartlogiv010.service.DestinataireServiceImpl;
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
//import java.time.LocalDateTime;
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
//@DisplayName("Destinataire Service Implementation - Complete Test Suite")
//class DestinataireServiceImplTest {
//
//    @Mock
//    private DestinataireRepository destinataireRepository;
//
//    @Mock
//    private SmartLogiMapper smartLogiMapper;
//
//    @InjectMocks
//    private DestinataireServiceImpl destinataireService;
//
//    private Destinataire destinataire;
//    private DestinataireCreateDto createDto;
//    private DestinataireUpdateDto updateDto;
//    private DestinataireSimpleResponseDto simpleResponseDto;
//
//    @BeforeEach
//    void setUp() {
//        destinataire = new Destinataire();
//        destinataire.setId("dest-123");
//        destinataire.setNom("Martin");
//        destinataire.setPrenom("Pierre");
//        destinataire.setEmail("pierre.martin@example.com");
//        destinataire.setTelephone("0698765432");
//        destinataire.setAdresse("456 Avenue Test, Rabat");
//        destinataire.setDateCreation(LocalDateTime.now());
//
//        createDto = new DestinataireCreateDto();
//        createDto.setNom("Martin");
//        createDto.setPrenom("Pierre");
//        createDto.setEmail("pierre.martin@example.com");
//        createDto.setTelephone("0698765432");
//        createDto.setAdresse("456 Avenue Test, Rabat");
//
//        updateDto = new DestinataireUpdateDto();
//        updateDto.setId("dest-123");
//        updateDto.setNom("Durand");
//        updateDto.setPrenom("Marie");
//        updateDto.setEmail("marie.durand@example.com");
//        updateDto.setTelephone("0612345678");
//        updateDto.setAdresse("789 Rue Updated, Marrakech");
//
//        simpleResponseDto = new DestinataireSimpleResponseDto();
//        simpleResponseDto.setId("dest-123");
//        simpleResponseDto.setNom("Martin");
//        simpleResponseDto.setPrenom("Pierre");
//        simpleResponseDto.setEmail("pierre.martin@example.com");
//        simpleResponseDto.setTelephone("0698765432");
//        simpleResponseDto.setAdresse("456 Avenue Test, Rabat");
//        simpleResponseDto.setDateCreation(LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("Create - Should create destinataire successfully")
//    void testCreate_Success() throws EmailAlreadyExistsException {
//        when(destinataireRepository.existsDestinataireByEmail(anyString())).thenReturn(false);
//        when(smartLogiMapper.toEntity(any(DestinataireCreateDto.class))).thenReturn(destinataire);
//        when(destinataireRepository.save(any(Destinataire.class))).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.create(createDto);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("dest-123");
//        assertThat(result.getEmail()).isEqualTo("pierre.martin@example.com");
//
//        verify(destinataireRepository, times(1)).existsDestinataireByEmail(createDto.getEmail());
//        verify(smartLogiMapper, times(1)).toEntity(createDto);
//        verify(destinataireRepository, times(1)).save(destinataire);
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(destinataire);
//    }
//
//    @Test
//    @DisplayName("Create - Should throw exception when email already exists")
//    void testCreate_EmailAlreadyExists() {
//        when(destinataireRepository.existsDestinataireByEmail(anyString())).thenReturn(true);
//
//        assertThatThrownBy(() -> destinataireService.create(createDto))
//                .isInstanceOf(EmailAlreadyExistsException.class);
//
//        verify(destinataireRepository, times(1)).existsDestinataireByEmail(createDto.getEmail());
//        verify(destinataireRepository, never()).save(any(Destinataire.class));
//    }
//
//    @Test
//    @DisplayName("Create - Should handle special characters in nom")
//    void testCreate_SpecialCharactersInNom() throws EmailAlreadyExistsException {
//        createDto.setNom("Jean-Pierre O'Connor");
//        when(destinataireRepository.existsDestinataireByEmail(anyString())).thenReturn(false);
//        when(smartLogiMapper.toEntity(any(DestinataireCreateDto.class))).thenReturn(destinataire);
//        when(destinataireRepository.save(any(Destinataire.class))).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.create(createDto);
//
//        assertThat(result).isNotNull();
//        verify(destinataireRepository, times(1)).save(destinataire);
//    }
//
//    @Test
//    @DisplayName("Create - Should handle Moroccan phone number with country code")
//    void testCreate_MoroccanPhoneWithCountryCode() throws EmailAlreadyExistsException {
//        createDto.setTelephone("+212698765432");
//        when(destinataireRepository.existsDestinataireByEmail(anyString())).thenReturn(false);
//        when(smartLogiMapper.toEntity(any(DestinataireCreateDto.class))).thenReturn(destinataire);
//        when(destinataireRepository.save(any(Destinataire.class))).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.create(createDto);
//
//        assertThat(result).isNotNull();
//        verify(destinataireRepository, times(1)).save(destinataire);
//    }
//
//    @Test
//    @DisplayName("Create - Should handle special characters in adresse")
//    void testCreate_SpecialCharactersInAdresse() throws EmailAlreadyExistsException {
//        createDto.setAdresse("123 Rue de l'École, Apt. #5 & Bât. C");
//        when(destinataireRepository.existsDestinataireByEmail(anyString())).thenReturn(false);
//        when(smartLogiMapper.toEntity(any(DestinataireCreateDto.class))).thenReturn(destinataire);
//        when(destinataireRepository.save(any(Destinataire.class))).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.create(createDto);
//
//        assertThat(result).isNotNull();
//        verify(destinataireRepository, times(1)).save(destinataire);
//    }
//
//    @Test
//    @DisplayName("Update - Should update destinataire successfully")
//    void testUpdate_Success() {
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        doNothing().when(smartLogiMapper).updateEntityFromDto((DestinataireUpdateDto) any(), any());
//        when(destinataireRepository.save(any(Destinataire.class))).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.update("dest-123", updateDto);
//
//        assertThat(result).isNotNull();
//        verify(destinataireRepository, times(1)).findById("dest-123");
//        verify(smartLogiMapper, times(1)).updateEntityFromDto(updateDto, destinataire);
//        verify(destinataireRepository, times(1)).save(destinataire);
//    }
//
//    @Test
//    @DisplayName("Update - Should throw ArgementNotFoundExption when destinataire not found")
//    void testUpdate_DestinataireNotFound() {
//        when(destinataireRepository.findById("dest-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> destinataireService.update("dest-999", updateDto))
//                .isInstanceOf(ArgementNotFoundExption.class)
//                .hasMessageContaining("the destinataire id not found");
//
//        verify(destinataireRepository, times(1)).findById("dest-999");
//        verify(destinataireRepository, never()).save(any(Destinataire.class));
//    }
//
//    @Test
//    @DisplayName("Update - Should handle partial update with null values")
//    void testUpdate_PartialUpdate() {
//        updateDto.setNom(null);
//        updateDto.setPrenom(null);
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        doNothing().when(smartLogiMapper).updateEntityFromDto((DestinataireUpdateDto) any(), any());
//        when(destinataireRepository.save(any(Destinataire.class))).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.update("dest-123", updateDto);
//
//        assertThat(result).isNotNull();
//        verify(destinataireRepository, times(1)).save(destinataire);
//    }
//
//    @Test
//    @DisplayName("Update - Should handle email update")
//    void testUpdate_EmailUpdate() {
//        updateDto.setEmail("newemail@example.com");
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        doNothing().when(smartLogiMapper).updateEntityFromDto((DestinataireUpdateDto) any(), any());
//        when(destinataireRepository.save(any(Destinataire.class))).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.update("dest-123", updateDto);
//
//        assertThat(result).isNotNull();
//        verify(destinataireRepository, times(1)).save(destinataire);
//    }
//
//    @Test
//    @DisplayName("GetById - Should get destinataire by id successfully")
//    void testGetById_Success() {
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.getById("dest-123");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("dest-123");
//        verify(destinataireRepository, times(1)).findById("dest-123");
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(destinataire);
//    }
//
//    @Test
//    @DisplayName("GetById - Should throw exception when destinataire not found")
//    void testGetById_NotFound() {
//        when(destinataireRepository.findById("dest-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> destinataireService.getById("dest-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Destinataire non trouvé");
//
//        verify(destinataireRepository, times(1)).findById("dest-999");
//    }
//
//    @Test
//    @DisplayName("GetAll - Should get all destinataires with pagination")
//    void testGetAll_WithPagination_Success() {
//        Pageable pageable = PageRequest.of(0, 10);
//        List<Destinataire> destinataires = Arrays.asList(destinataire);
//        Page<Destinataire> destinatairePage = new PageImpl<>(destinataires, pageable, destinataires.size());
//
//        when(destinataireRepository.findAll(any(Pageable.class))).thenReturn(destinatairePage);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        Page<DestinataireSimpleResponseDto> result = destinataireService.getAll(pageable);
//
//        assertThat(result).isNotNull()
//        .hasSize(1);
//        assertThat(result.getTotalElements()).isEqualTo(1);
//        verify(destinataireRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should return empty page when no destinataires exist")
//    void testGetAll_EmptyPage() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Destinataire> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
//
//        when(destinataireRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);
//
//        Page<DestinataireSimpleResponseDto> result = destinataireService.getAll(pageable);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).isEmpty();
//        verify(destinataireRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should handle multiple pages")
//    void testGetAll_MultiplePages() {
//        Pageable pageable = PageRequest.of(1, 5);
//        List<Destinataire> destinataires = Arrays.asList(destinataire);
//        Page<Destinataire> destinatairePage = new PageImpl<>(destinataires, pageable, 20);
//
//        when(destinataireRepository.findAll(any(Pageable.class))).thenReturn(destinatairePage);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        Page<DestinataireSimpleResponseDto> result = destinataireService.getAll(pageable);
//
//        assertThat(result.getTotalPages()).isEqualTo(4);
//        assertThat(result.getNumber()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should find destinataires by nom")
//    void testSearchByNom_Found() {
//        List<Destinataire> destinataires = Arrays.asList(destinataire);
//        when(destinataireRepository.findByNomContainingIgnoreCase("Martin")).thenReturn(destinataires);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        List<DestinataireSimpleResponseDto> result = destinataireService.searchByNom("Martin");
//
//        assertThat(result).isNotNull();
//        assertThat(result).hasSize(1);
//        verify(destinataireRepository, times(1)).findByNomContainingIgnoreCase("Martin");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should return empty list when no match")
//    void testSearchByNom_NotFound() {
//        when(destinataireRepository.findByNomContainingIgnoreCase("Unknown")).thenReturn(new ArrayList<>());
//
//        List<DestinataireSimpleResponseDto> result = destinataireService.searchByNom("Unknown");
//
//        assertThat(result).isEmpty();
//        verify(destinataireRepository, times(1)).findByNomContainingIgnoreCase("Unknown");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should be case insensitive")
//    void testSearchByNom_CaseInsensitive() {
//        List<Destinataire> destinataires = Arrays.asList(destinataire);
//        when(destinataireRepository.findByNomContainingIgnoreCase("martin")).thenReturn(destinataires);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        List<DestinataireSimpleResponseDto> result = destinataireService.searchByNom("martin");
//
//        assertThat(result).hasSize(1);
//        verify(destinataireRepository, times(1)).findByNomContainingIgnoreCase("martin");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should handle partial match")
//    void testSearchByNom_PartialMatch() {
//        List<Destinataire> destinataires = Arrays.asList(destinataire);
//        when(destinataireRepository.findByNomContainingIgnoreCase("Mar")).thenReturn(destinataires);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        List<DestinataireSimpleResponseDto> result = destinataireService.searchByNom("Mar");
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("FindByKeyWord - Should find destinataire by keyword")
//    void testFindByKeyWord_Found() {
//        when(destinataireRepository.searchByKeyword("Martin")).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.findByKeyWord("Martin");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("dest-123");
//        verify(destinataireRepository, times(1)).searchByKeyword("Martin");
//    }
//
//    @Test
//    @DisplayName("FindByKeyWord - Should throw exception when not found")
//    void testFindByKeyWord_NotFound() {
//        when(destinataireRepository.searchByKeyword("Unknown")).thenReturn(null);
//
//        assertThatThrownBy(() -> destinataireService.findByKeyWord("Unknown"))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessage("le Destinataire pas trouve");
//
//        verify(destinataireRepository, times(1)).searchByKeyword("Unknown");
//    }
//
//    @Test
//    @DisplayName("FindByKeyWord - Should handle email as keyword")
//    void testFindByKeyWord_EmailKeyword() {
//        when(destinataireRepository.searchByKeyword("pierre.martin@example.com")).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.findByKeyWord("pierre.martin@example.com");
//
//        assertThat(result).isNotNull();
//        verify(destinataireRepository, times(1)).searchByKeyword("pierre.martin@example.com");
//    }
//
//    @Test
//    @DisplayName("FindByKeyWord - Should handle phone as keyword")
//    void testFindByKeyWord_PhoneKeyword() {
//        when(destinataireRepository.searchByKeyword("0698765432")).thenReturn(destinataire);
//        when(smartLogiMapper.toSimpleResponseDto(any(Destinataire.class))).thenReturn(simpleResponseDto);
//
//        DestinataireSimpleResponseDto result = destinataireService.findByKeyWord("0698765432");
//
//        assertThat(result).isNotNull();
//        verify(destinataireRepository, times(1)).searchByKeyword("0698765432");
//    }
//
//    @Test
//    @DisplayName("Delete - Should delete destinataire successfully")
//    void testDelete_Success() {
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        doNothing().when(destinataireRepository).delete(any(Destinataire.class));
//
//        destinataireService.delete("dest-123");
//
//        verify(destinataireRepository, times(1)).findById("dest-123");
//        verify(destinataireRepository, times(1)).delete(destinataire);
//    }
//
//    @Test
//    @DisplayName("Delete - Should throw exception when destinataire not found")
//    void testDelete_DestinataireNotFound() {
//        when(destinataireRepository.findById("dest-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> destinataireService.delete("dest-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Destinataire non trouvé");
//
//        verify(destinataireRepository, times(1)).findById("dest-999");
//        verify(destinataireRepository, never()).delete(any(Destinataire.class));
//    }
//
//    @Test
//    @DisplayName("ExistsById - Should return true when destinataire exists")
//    void testExistsById_True() {
//        when(destinataireRepository.existsById("dest-123")).thenReturn(true);
//
//        boolean result = destinataireService.existsById("dest-123");
//
//        assertThat(result).isTrue();
//        verify(destinataireRepository, times(1)).existsById("dest-123");
//    }
//
//    @Test
//    @DisplayName("ExistsById - Should return false when destinataire does not exist")
//    void testExistsById_False() {
//        when(destinataireRepository.existsById("dest-999")).thenReturn(false);
//
//        boolean result = destinataireService.existsById("dest-999");
//
//        assertThat(result).isFalse();
//        verify(destinataireRepository, times(1)).existsById("dest-999");
//    }
//}
