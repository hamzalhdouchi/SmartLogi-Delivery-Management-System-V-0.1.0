package com.smartlogi.smartlogiv010.controllerTest;//package com.smartlogi.smartlogiv010.controllerTest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smartlogi.smartlogiv010.controller.DestinataireController;
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.DestinataireCreateDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.DestinataireUpdateDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Destinataire.DestinataireSimpleResponseDto;
//import com.smartlogi.smartlogiv010.exception.EmailAlreadyExistsException;
//import com.smartlogi.smartlogiv010.service.DestinataireServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.hamcrest.Matchers.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(DestinataireController.class)
//@DisplayName("Tests du contrôleur Destinataire")
//class DestinataireControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private DestinataireServiceImpl destinataireService;
//
//    private DestinataireCreateDto createDto;
//    private DestinataireUpdateDto updateDto;
//    private DestinataireSimpleResponseDto responseDto;
//
//    @BeforeEach
//    void setUp() {
//        // Initialisation des DTOs pour les tests
//        createDto = new DestinataireCreateDto(
//                "Alami",
//                "Mohammed",
//                "mohammed.alami@example.com",
//                "+212612345678",
//                "123 Rue Hassan II, Casablanca"
//        );
//
//        updateDto = new DestinataireUpdateDto();
//        updateDto.setId("123e4567-e89b-12d3-a456-426614174000");
//        updateDto.setNom("Alami");
//        updateDto.setPrenom("Ahmed");
//        updateDto.setEmail("ahmed.alami@example.com");
//        updateDto.setTelephone("+212698765432");
//        updateDto.setAdresse("456 Avenue Mohammed V, Rabat");
//
//        responseDto = new DestinataireSimpleResponseDto(
//                "123e4567-e89b-12d3-a456-426614174000",
//                "Alami",
//                "Mohammed",
//                "mohammed.alami@example.com",
//                "+212612345678",
//                "123 Rue Hassan II, Casablanca",
//                LocalDateTime.now()
//        );
//    }
//
//    @Test
//    @DisplayName("Créer un destinataire - Succès")
//    void testCreateDestinataire_Success() throws Exception {
//        when(destinataireService.create(any(DestinataireCreateDto.class)))
//                .thenReturn(responseDto);
//
//        mockMvc.perform(post("/api/destinataires")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Destinataire créé avec succès")))
//                .andExpect(jsonPath("$.data.id", is(responseDto.getId())))
//                .andExpect(jsonPath("$.data.nom", is(responseDto.getNom())))
//                .andExpect(jsonPath("$.data.prenom", is(responseDto.getPrenom())))
//                .andExpect(jsonPath("$.data.email", is(responseDto.getEmail())))
//                .andExpect(jsonPath("$.data.telephone", is(responseDto.getTelephone())))
//                .andExpect(jsonPath("$.data.adresse", is(responseDto.getAdresse())));
//
//        verify(destinataireService, times(1)).create(any(DestinataireCreateDto.class));
//    }
//
//    @Test
//    @DisplayName("Créer un destinataire - Email déjà existant")
//    void testCreateDestinataire_EmailAlreadyExists() throws Exception {
//        when(destinataireService.create(any(DestinataireCreateDto.class)))
//                .thenThrow(new EmailAlreadyExistsException("L'email existe déjà"));
//
//        mockMvc.perform(post("/api/destinataires")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andDo(print())
//                .andExpect(status().isConflict());
//
//        verify(destinataireService, times(1)).create(any(DestinataireCreateDto.class));
//    }
//
//    @Test
//    @DisplayName("Créer un destinataire - Validation échouée (nom vide)")
//    void testCreateDestinataire_ValidationFailed_EmptyName() throws Exception {
//        createDto.setNom("");
//
//        mockMvc.perform(post("/api/destinataires")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//        verify(destinataireService, never()).create(any(DestinataireCreateDto.class));
//    }
//
//    @Test
//    @DisplayName("Créer un destinataire - Email invalide")
//    void testCreateDestinataire_InvalidEmail() throws Exception {
//        createDto.setEmail("invalid-email");
//
//        mockMvc.perform(post("/api/destinataires")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//        verify(destinataireService, never()).create(any(DestinataireCreateDto.class));
//    }
//
//    @Test
//    @DisplayName("Créer un destinataire - Téléphone invalide")
//    void testCreateDestinataire_InvalidPhone() throws Exception {
//        createDto.setTelephone("123456");
//
//        mockMvc.perform(post("/api/destinataires")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createDto)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//
//        verify(destinataireService, never()).create(any(DestinataireCreateDto.class));
//    }
//
//    @Test
//    @DisplayName("Mettre à jour un destinataire - Succès")
//    void testUpdateDestinataire_Success() throws Exception {
//        String destinataireId = "123e4567-e89b-12d3-a456-426614174000";
//        DestinataireSimpleResponseDto updatedResponse = new DestinataireSimpleResponseDto(
//                destinataireId,
//                updateDto.getNom(),
//                updateDto.getPrenom(),
//                updateDto.getEmail(),
//                updateDto.getTelephone(),
//                updateDto.getAdresse(),
//                LocalDateTime.now()
//        );
//
//        when(destinataireService.update(eq(destinataireId), any(DestinataireUpdateDto.class)))
//                .thenReturn(updatedResponse);
//
//        mockMvc.perform(put("/api/destinataires/{id}", destinataireId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Destinataire mis à jour avec succès")))
//                .andExpect(jsonPath("$.data.id", is(destinataireId)))
//                .andExpect(jsonPath("$.data.nom", is(updateDto.getNom())))
//                .andExpect(jsonPath("$.data.prenom", is(updateDto.getPrenom())));
//
//        verify(destinataireService, times(1)).update(eq(destinataireId), any(DestinataireUpdateDto.class));
//    }
//
//    @Test
//    @DisplayName("Obtenir un destinataire par ID - Succès")
//    void testGetDestinataireById_Success() throws Exception {
//        String destinataireId = "123e4567-e89b-12d3-a456-426614174000";
//        when(destinataireService.getById(destinataireId)).thenReturn(responseDto);
//
//        mockMvc.perform(get("/api/destinataires/{id}", destinataireId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Destinataire récupéré avec succès")))
//                .andExpect(jsonPath("$.data.id", is(responseDto.getId())))
//                .andExpect(jsonPath("$.data.nom", is(responseDto.getNom())))
//                .andExpect(jsonPath("$.data.email", is(responseDto.getEmail())));
//
//        verify(destinataireService, times(1)).getById(destinataireId);
//    }
//
//    @Test
//    @DisplayName("Lister tous les destinataires avec pagination - Succès")
//    void testGetAllDestinataires_Success() throws Exception {
//        DestinataireSimpleResponseDto destinataire1 = new DestinataireSimpleResponseDto(
//                "id1", "Alami", "Mohammed", "m.alami@example.com",
//                "+212612345678", "Casablanca", LocalDateTime.now()
//        );
//        DestinataireSimpleResponseDto destinataire2 = new DestinataireSimpleResponseDto(
//                "id2", "Bennani", "Fatima", "f.bennani@example.com",
//                "+212698765432", "Rabat", LocalDateTime.now()
//        );
//
//        List<DestinataireSimpleResponseDto> destinatairesList = Arrays.asList(destinataire1, destinataire2);
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<DestinataireSimpleResponseDto> destinatairesPage = new PageImpl<>(destinatairesList, pageable, 2);
//
//        when(destinataireService.getAll(any(Pageable.class))).thenReturn(destinatairesPage);
//
//        mockMvc.perform(get("/api/destinataires")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Liste des destinataires récupérée avec succès")))
//                .andExpect(jsonPath("$.data.content", hasSize(2)))
//                .andExpect(jsonPath("$.data.totalElements", is(2)))
//                .andExpect(jsonPath("$.data.content[0].nom", is("Alami")))
//                .andExpect(jsonPath("$.data.content[1].nom", is("Bennani")));
//
//        verify(destinataireService, times(1)).getAll(any(Pageable.class));
//    }
//
//    @Test
//    @DisplayName("Rechercher des destinataires par nom - Succès")
//    void testSearchDestinatairesByNom_Success() throws Exception {
//        String searchNom = "Alami";
//        List<DestinataireSimpleResponseDto> searchResults = Arrays.asList(responseDto);
//
//        when(destinataireService.searchByNom(searchNom)).thenReturn(searchResults);
//
//        mockMvc.perform(get("/api/destinataires/search")
//                        .param("nom", searchNom)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Recherche de destinataires par nom effectuée avec succès")))
//                .andExpect(jsonPath("$.data", hasSize(1)))
//                .andExpect(jsonPath("$.data[0].nom", is(responseDto.getNom())));
//
//        verify(destinataireService, times(1)).searchByNom(searchNom);
//    }
//
//    @Test
//    @DisplayName("Rechercher un destinataire par mot-clé - Succès")
//    void testFindDestinataireByKeyword_Success() throws Exception {
//        String keyword = "mohammed.alami@example.com";
//        when(destinataireService.findByKeyWord(keyword)).thenReturn(responseDto);
//
//        mockMvc.perform(get("/api/destinataires/search-keyword")
//                        .param("keyword", keyword)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Destinataire trouvé avec succès")))
//                .andExpect(jsonPath("$.data.email", is(responseDto.getEmail())));
//
//        verify(destinataireService, times(1)).findByKeyWord(keyword);
//    }
//
//    @Test
//    @DisplayName("Supprimer un destinataire - Succès")
//    void testDeleteDestinataire_Success() throws Exception {
//        String destinataireId = "123e4567-e89b-12d3-a456-426614174000";
//        doNothing().when(destinataireService).delete(destinataireId);
//
//        mockMvc.perform(delete("/api/destinataires/{id}", destinataireId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Destinataire supprimé avec succès")))
//                .andExpect(jsonPath("$.data").doesNotExist());
//
//        verify(destinataireService, times(1)).delete(destinataireId);
//    }
//
//    @Test
//    @DisplayName("Vérifier l'existence d'un destinataire - Existe")
//    void testExistsById_DestinataireExists() throws Exception {
//        String destinataireId = "123e4567-e89b-12d3-a456-426614174000";
//        when(destinataireService.existsById(destinataireId)).thenReturn(true);
//
//        mockMvc.perform(get("/api/destinataires/{id}/exists", destinataireId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Le destinataire existe")))
//                .andExpect(jsonPath("$.data", is(true)));
//
//        verify(destinataireService, times(1)).existsById(destinataireId);
//    }
//
//    @Test
//    @DisplayName("Vérifier l'existence d'un destinataire - N'existe pas")
//    void testExistsById_DestinataireDoesNotExist() throws Exception {
//        String destinataireId = "non-existent-id";
//        when(destinataireService.existsById(destinataireId)).thenReturn(false);
//
//        mockMvc.perform(get("/api/destinataires/{id}/exists", destinataireId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.message", is("Le destinataire n'existe pas")))
//                .andExpect(jsonPath("$.data", is(false)));
//
//        verify(destinataireService, times(1)).existsById(destinataireId);
//    }
//
//    @Test
//    @DisplayName("Lister tous les destinataires avec tri - Succès")
//    void testGetAllDestinatairesSorted_Success() throws Exception {
//        DestinataireSimpleResponseDto destinataire1 = new DestinataireSimpleResponseDto(
//                "id1", "Alami", "Mohammed", "m.alami@example.com",
//                "+212612345678", "Casablanca", LocalDateTime.now()
//        );
//        DestinataireSimpleResponseDto destinataire2 = new DestinataireSimpleResponseDto(
//                "id2", "Bennani", "Fatima", "f.bennani@example.com",
//                "+212698765432", "Rabat", LocalDateTime.now()
//        );
//
//        List<DestinataireSimpleResponseDto> destinatairesList = Arrays.asList(destinataire1, destinataire2);
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<DestinataireSimpleResponseDto> destinatairesPage = new PageImpl<>(destinatairesList, pageable, 2);
//
//        when(destinataireService.getAll(any(Pageable.class))).thenReturn(destinatairesPage);
//
//        mockMvc.perform(get("/api/destinataires")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .param("sort", "nom,asc")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success", is(true)))
//                .andExpect(jsonPath("$.data.content", hasSize(2)));
//
//        verify(destinataireService, times(1)).getAll(any(Pageable.class));
//    }
//}
