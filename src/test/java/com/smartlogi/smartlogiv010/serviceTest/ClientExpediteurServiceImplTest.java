//package com.smartlogi.smartlogiv010.serviceTest;
//
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ClientExpediteurCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ClientExpediteurUpdateRequestDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
//import com.smartlogi.smartlogiv010.entity.ClientExpediteur;
//import com.smartlogi.smartlogiv010.exception.ResourceNotFoundException;
//import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
//import com.smartlogi.smartlogiv010.repository.ClientExpediteurRepository;
//import com.smartlogi.smartlogiv010.service.clientExpsditeurInterfaceImpl;
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
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("ClientExpediteur Service Implementation - Complete Test Suite")
//class ClientExpediteurServiceImplTest {
//
//    @Mock
//    private ClientExpediteurRepository clientExpediteurRepository;
//
//    @Mock
//    private SmartLogiMapper smartLogiMapper;
//
//    @InjectMocks
//    private clientExpsditeurInterfaceImpl clientExpediteurService;
//
//    private ClientExpediteur clientExpediteur;
//    private ClientExpediteurCreateRequestDto createRequestDto;
//    private ClientExpediteurUpdateRequestDto updateRequestDto;
//    private ClientExpediteurSimpleResponseDto simpleResponseDto;
//
//    @BeforeEach
//    void setUp() {
//        clientExpediteur = new ClientExpediteur();
//        clientExpediteur.setId("client-123");
//        clientExpediteur.setNom("Dupont");
//        clientExpediteur.setPrenom("Jean");
//        clientExpediteur.setEmail("jean.dupont@example.com");
//        clientExpediteur.setTelephone("0612345678");
//        clientExpediteur.setAdresse("123 Rue Test, Casablanca");
//        clientExpediteur.setColis(new ArrayList<>());
//
//        createRequestDto = new ClientExpediteurCreateRequestDto();
//        createRequestDto.setNom("Dupont");
//        createRequestDto.setPrenom("Jean");
//        createRequestDto.setEmail("jean.dupont@example.com");
//        createRequestDto.setTelephone("0612345678");
//        createRequestDto.setAdresse("123 Rue Test, Casablanca");
//
//        updateRequestDto = new ClientExpediteurUpdateRequestDto();
//        updateRequestDto.setId("client-123");
//        updateRequestDto.setNom("Martin");
//        updateRequestDto.setPrenom("Pierre");
//        updateRequestDto.setEmail("pierre.martin@example.com");
//        updateRequestDto.setTelephone("0698765432");
//        updateRequestDto.setAdresse("456 Avenue Test, Rabat");
//
//        simpleResponseDto = new ClientExpediteurSimpleResponseDto();
//        simpleResponseDto.setId("client-123");
//        simpleResponseDto.setNom("Dupont");
//        simpleResponseDto.setPrenom("Jean");
//        simpleResponseDto.setEmail("jean.dupont@example.com");
//        simpleResponseDto.setTelephone("0612345678");
//        simpleResponseDto.setAdresse("123 Rue Test, Casablanca");
//        simpleResponseDto.setDateCreation(LocalDateTime.now());
//    }
//
//    @Test
//    @DisplayName("Create - Should create client expediteur successfully")
//    void testCreate_Success() {
//        when(smartLogiMapper.toEntity(any(ClientExpediteurCreateRequestDto.class))).thenReturn(clientExpediteur);
//        when(clientExpediteurRepository.save(any(ClientExpediteur.class))).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.create(createRequestDto);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("client-123");
//        assertThat(result.getNom()).isEqualTo("Dupont");
//        assertThat(result.getEmail()).isEqualTo("jean.dupont@example.com");
//
//        verify(smartLogiMapper, times(1)).toEntity(createRequestDto);
//        verify(clientExpediteurRepository, times(1)).save(clientExpediteur);
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(clientExpediteur);
//    }
//
//    @Test
//    @DisplayName("Create - Should handle special characters in nom")
//    void testCreate_SpecialCharactersInNom() {
//        createRequestDto.setNom("Jean-Pierre O'Connor");
//        when(smartLogiMapper.toEntity(any(ClientExpediteurCreateRequestDto.class))).thenReturn(clientExpediteur);
//        when(clientExpediteurRepository.save(any(ClientExpediteur.class))).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.create(createRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(clientExpediteurRepository, times(1)).save(clientExpediteur);
//    }
//
//    @Test
//    @DisplayName("Create - Should handle Moroccan phone number with country code")
//    void testCreate_MoroccanPhoneWithCountryCode() {
//        createRequestDto.setTelephone("+212612345678");
//        when(smartLogiMapper.toEntity(any(ClientExpediteurCreateRequestDto.class))).thenReturn(clientExpediteur);
//        when(clientExpediteurRepository.save(any(ClientExpediteur.class))).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.create(createRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(clientExpediteurRepository, times(1)).save(clientExpediteur);
//    }
//
//    @Test
//    @DisplayName("Create - Should handle special characters in adresse")
//    void testCreate_SpecialCharactersInAdresse() {
//        createRequestDto.setAdresse("123 Rue de l'École, Apt. #5 & Bât. C");
//        when(smartLogiMapper.toEntity(any(ClientExpediteurCreateRequestDto.class))).thenReturn(clientExpediteur);
//        when(clientExpediteurRepository.save(any(ClientExpediteur.class))).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.create(createRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(clientExpediteurRepository, times(1)).save(clientExpediteur);
//    }
//
//    @Test
//    @DisplayName("Update - Should update client expediteur successfully")
//    void testUpdate_Success() {
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(clientExpediteur));
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ClientExpediteurUpdateRequestDto) any(), any());
//        when(clientExpediteurRepository.save(any(ClientExpediteur.class))).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.update("client-123", updateRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(clientExpediteurRepository, times(1)).findById("client-123");
//        verify(smartLogiMapper, times(1)).updateEntityFromDto(updateRequestDto, clientExpediteur);
//        verify(clientExpediteurRepository, times(1)).save(clientExpediteur);
//    }
//
//    @Test
//    @DisplayName("Update - Should throw exception when client not found")
//    void testUpdate_ClientNotFound() {
//        when(clientExpediteurRepository.findById("client-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> clientExpediteurService.update("client-999", updateRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Client expéditeur non trouvé");
//
//        verify(clientExpediteurRepository, times(1)).findById("client-999");
//        verify(clientExpediteurRepository, never()).save(any(ClientExpediteur.class));
//    }
//
//    @Test
//    @DisplayName("Update - Should handle partial update with null values")
//    void testUpdate_PartialUpdate() {
//        updateRequestDto.setNom(null);
//        updateRequestDto.setPrenom(null);
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(clientExpediteur));
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ClientExpediteurUpdateRequestDto) any(), any());
//        when(clientExpediteurRepository.save(any(ClientExpediteur.class))).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.update("client-123", updateRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(clientExpediteurRepository, times(1)).save(clientExpediteur);
//    }
//
//    @Test
//    @DisplayName("Update - Should handle email update")
//    void testUpdate_EmailUpdate() {
//        updateRequestDto.setEmail("newemail@example.com");
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(clientExpediteur));
//        doNothing().when(smartLogiMapper).updateEntityFromDto((ClientExpediteurUpdateRequestDto) any(), any());
//        when(clientExpediteurRepository.save(any(ClientExpediteur.class))).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.update("client-123", updateRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(clientExpediteurRepository, times(1)).save(clientExpediteur);
//    }
//
//    @Test
//    @DisplayName("GetById - Should get client expediteur by id successfully")
//    void testGetById_Success() {
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(clientExpediteur));
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.getById("client-123");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("client-123");
//        verify(clientExpediteurRepository, times(1)).findById("client-123");
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(clientExpediteur);
//    }
//
//    @Test
//    @DisplayName("GetById - Should throw exception when client not found")
//    void testGetById_NotFound() {
//        when(clientExpediteurRepository.findById("client-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> clientExpediteurService.getById("client-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Client expéditeur non trouvé");
//
//        verify(clientExpediteurRepository, times(1)).findById("client-999");
//    }
//
//    @Test
//    @DisplayName("GetAll - Should get all clients with pagination")
//    void testGetAll_WithPagination_Success() {
//        Pageable pageable = PageRequest.of(0, 10);
//        List<ClientExpediteur> clients = Arrays.asList(clientExpediteur);
//        Page<ClientExpediteur> clientPage = new PageImpl<>(clients, pageable, clients.size());
//
//        when(clientExpediteurRepository.findAll(any(Pageable.class))).thenReturn(clientPage);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        Page<ClientExpediteurSimpleResponseDto> result = clientExpediteurService.getAll(pageable);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).hasSize(1);
//        assertThat(result.getTotalElements()).isEqualTo(1);
//        verify(clientExpediteurRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should return empty page when no clients exist")
//    void testGetAll_EmptyPage() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<ClientExpediteur> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
//
//        when(clientExpediteurRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);
//
//        Page<ClientExpediteurSimpleResponseDto> result = clientExpediteurService.getAll(pageable);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).isEmpty();
//        verify(clientExpediteurRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should handle multiple pages")
//    void testGetAll_MultiplePages() {
//        Pageable pageable = PageRequest.of(1, 5);
//        List<ClientExpediteur> clients = Arrays.asList(clientExpediteur);
//        Page<ClientExpediteur> clientPage = new PageImpl<>(clients, pageable, 20);
//
//        when(clientExpediteurRepository.findAll(any(Pageable.class))).thenReturn(clientPage);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        Page<ClientExpediteurSimpleResponseDto> result = clientExpediteurService.getAll(pageable);
//
//        assertThat(result.getTotalPages()).isEqualTo(4);
//        assertThat(result.getNumber()).isEqualTo(1);
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should find clients by nom")
//    void testSearchByNom_Found() {
//        List<ClientExpediteur> clients = Arrays.asList(clientExpediteur);
//        when(clientExpediteurRepository.findByNomContainingIgnoreCase("Dupont")).thenReturn(clients);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        List<ClientExpediteurSimpleResponseDto> result = clientExpediteurService.searchByNom("Dupont");
//
//        assertThat(result)
//                .isNotNull()
//                .hasSize(1);
//        verify(clientExpediteurRepository, times(1)).findByNomContainingIgnoreCase("Dupont");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should return empty list when no match")
//    void testSearchByNom_NotFound() {
//        when(clientExpediteurRepository.findByNomContainingIgnoreCase("Unknown")).thenReturn(new ArrayList<>());
//
//        List<ClientExpediteurSimpleResponseDto> result = clientExpediteurService.searchByNom("Unknown");
//
//        assertThat(result).isEmpty();
//        verify(clientExpediteurRepository, times(1)).findByNomContainingIgnoreCase("Unknown");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should be case insensitive")
//    void testSearchByNom_CaseInsensitive() {
//        List<ClientExpediteur> clients = Arrays.asList(clientExpediteur);
//        when(clientExpediteurRepository.findByNomContainingIgnoreCase("dupont")).thenReturn(clients);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        List<ClientExpediteurSimpleResponseDto> result = clientExpediteurService.searchByNom("dupont");
//
//        assertThat(result).hasSize(1);
//        verify(clientExpediteurRepository, times(1)).findByNomContainingIgnoreCase("dupont");
//    }
//
//    @Test
//    @DisplayName("SearchByNom - Should handle partial match")
//    void testSearchByNom_PartialMatch() {
//        List<ClientExpediteur> clients = Arrays.asList(clientExpediteur);
//        when(clientExpediteurRepository.findByNomContainingIgnoreCase("Du")).thenReturn(clients);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        List<ClientExpediteurSimpleResponseDto> result = clientExpediteurService.searchByNom("Du");
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("FindByKeyWord - Should find client by keyword")
//    void testFindByKeyWord_Found() {
//        when(clientExpediteurRepository.searchByKeyword("Dupont")).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.findByKeyWord("Dupont");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("client-123");
//        verify(clientExpediteurRepository, times(1)).searchByKeyword("Dupont");
//    }
//
//    @Test
//    @DisplayName("FindByKeyWord - Should throw exception when not found")
//    void testFindByKeyWord_NotFound() {
//        when(clientExpediteurRepository.searchByKeyword("Unknown")).thenReturn(null);
//
//        assertThatThrownBy(() -> clientExpediteurService.findByKeyWord("Unknown"))
//                .isInstanceOf(ResourceNotFoundException.class)
//                .hasMessage("le Destinataire pas trouve");
//
//        verify(clientExpediteurRepository, times(1)).searchByKeyword("Unknown");
//    }
//
//    @Test
//    @DisplayName("FindByKeyWord - Should handle email as keyword")
//    void testFindByKeyWord_EmailKeyword() {
//        when(clientExpediteurRepository.searchByKeyword("jean.dupont@example.com")).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.findByKeyWord("jean.dupont@example.com");
//
//        assertThat(result).isNotNull();
//        verify(clientExpediteurRepository, times(1)).searchByKeyword("jean.dupont@example.com");
//    }
//
//    @Test
//    @DisplayName("FindByKeyWord - Should handle phone as keyword")
//    void testFindByKeyWord_PhoneKeyword() {
//        when(clientExpediteurRepository.searchByKeyword("0612345678")).thenReturn(clientExpediteur);
//        when(smartLogiMapper.toSimpleResponseDto(any(ClientExpediteur.class))).thenReturn(simpleResponseDto);
//
//        ClientExpediteurSimpleResponseDto result = clientExpediteurService.findByKeyWord("0612345678");
//
//        assertThat(result).isNotNull();
//        verify(clientExpediteurRepository, times(1)).searchByKeyword("0612345678");
//    }
//
//    @Test
//    @DisplayName("Delete - Should delete client expediteur successfully")
//    void testDelete_Success() {
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(clientExpediteur));
//        doNothing().when(clientExpediteurRepository).delete(any(ClientExpediteur.class));
//
//        clientExpediteurService.delete("client-123");
//
//        verify(clientExpediteurRepository, times(1)).findById("client-123");
//        verify(clientExpediteurRepository, times(1)).delete(clientExpediteur);
//    }
//
//    @Test
//    @DisplayName("Delete - Should throw exception when client not found")
//    void testDelete_ClientNotFound() {
//        when(clientExpediteurRepository.findById("client-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> clientExpediteurService.delete("client-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Client expéditeur non trouvé");
//
//        verify(clientExpediteurRepository, times(1)).findById("client-999");
//        verify(clientExpediteurRepository, never()).delete(any(ClientExpediteur.class));
//    }
//
//    @Test
//    @DisplayName("ExistsById - Should return true when client exists")
//    void testExistsById_True() {
//        when(clientExpediteurRepository.existsById("client-123")).thenReturn(true);
//
//        boolean result = clientExpediteurService.existsById("client-123");
//
//        assertThat(result).isTrue();
//        verify(clientExpediteurRepository, times(1)).existsById("client-123");
//    }
//
//    @Test
//    @DisplayName("ExistsById - Should return false when client does not exist")
//    void testExistsById_False() {
//        when(clientExpediteurRepository.existsById("client-999")).thenReturn(false);
//
//        boolean result = clientExpediteurService.existsById("client-999");
//
//        assertThat(result).isFalse();
//        verify(clientExpediteurRepository, times(1)).existsById("client-999");
//    }
//}
