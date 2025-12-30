package com.smartlogi.smartlogiv010.controllerTest;//package com.smartlogi.smartlogiv010.controllerTest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smartlogi.smartlogiv010.controller.ZoneController;
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ZoneCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ZoneUpdateRequestDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Zone.ZoneDetailedResponseDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Zone.ZoneSimpleResponseDto;
//import com.smartlogi.smartlogiv010.service.interfaces.ZoneService;
//import org.junit.jupiter.api.BeforeEach;
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
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ZoneController.class)
//class ZoneControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ZoneService zoneService;
//
//    private ZoneSimpleResponseDto zoneSimpleDto;
//    private ZoneDetailedResponseDto zoneDetailedDto;
//    private ZoneCreateRequestDto createRequestDto;
//    private ZoneUpdateRequestDto updateRequestDto;
//
//    @BeforeEach
//    void setUp() {
//        zoneSimpleDto = new ZoneSimpleResponseDto();
//        zoneSimpleDto.setId("123e4567-e89b-12d3-a456-426614174000");
//        zoneSimpleDto.setNom("Casablanca Centre");
//        zoneSimpleDto.setCodePostal("20000");
//
//        zoneDetailedDto = new ZoneDetailedResponseDto();
//        zoneDetailedDto.setId("123e4567-e89b-12d3-a456-426614174000");
//        zoneDetailedDto.setNom("Casablanca Centre");
//        zoneDetailedDto.setCodePostal("20000");
//
//        createRequestDto = new ZoneCreateRequestDto();
//        createRequestDto.setNom("Casablanca Centre");
//        createRequestDto.setCodePostal("20000");
//
//        updateRequestDto = new ZoneUpdateRequestDto();
//        updateRequestDto.setNom("Casablanca Centre Updated");
//        updateRequestDto.setCodePostal("20001");
//    }
//
//    @Test
//    void testCreate_Success() throws Exception {
//        when(zoneService.create(any(ZoneCreateRequestDto.class)))
//                .thenReturn(zoneSimpleDto);
//
//        mockMvc.perform(post("/api/zones")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createRequestDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Zone créée avec succès"))
//                .andExpect(jsonPath("$.data.id").value(zoneSimpleDto.getId()))
//                .andExpect(jsonPath("$.data.nom").value(zoneSimpleDto.getNom()))
//                .andExpect(jsonPath("$.data.codePostal").value(zoneSimpleDto.getCodePostal()));
//
//        verify(zoneService, times(1)).create(any(ZoneCreateRequestDto.class));
//    }
//
//    @Test
//    void testUpdate_Success() throws Exception {
//        String zoneId = "123e4567-e89b-12d3-a456-426614174000";
//        when(zoneService.update(eq(zoneId), any(ZoneUpdateRequestDto.class)))
//                .thenReturn(zoneSimpleDto);
//
//        mockMvc.perform(put("/api/zones/{id}", zoneId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Zone mise à jour avec succès"))
//                .andExpect(jsonPath("$.data.id").value(zoneSimpleDto.getId()));
//
//        verify(zoneService, times(1)).update(eq(zoneId), any(ZoneUpdateRequestDto.class));
//    }
//
//    @Test
//    void testGetById_Success() throws Exception {
//        String zoneId = "123e4567-e89b-12d3-a456-426614174000";
//        when(zoneService.getById(zoneId)).thenReturn(zoneSimpleDto);
//
//        mockMvc.perform(get("/api/zones/{id}", zoneId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Zone récupérée avec succès"))
//                .andExpect(jsonPath("$.data.id").value(zoneSimpleDto.getId()))
//                .andExpect(jsonPath("$.data.nom").value(zoneSimpleDto.getNom()));
//
//        verify(zoneService, times(1)).getById(zoneId);
//    }
//
//    @Test
//    void testGetByIdWithDetails_Success() throws Exception {
//        String zoneId = "123e4567-e89b-12d3-a456-426614174000";
//        when(zoneService.getByIdWithDetails(zoneId)).thenReturn(zoneDetailedDto);
//
//        mockMvc.perform(get("/api/zones/{id}/detailed", zoneId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Zone détaillée récupérée avec succès"))
//                .andExpect(jsonPath("$.data.id").value(zoneDetailedDto.getId()));
//
//        verify(zoneService, times(1)).getByIdWithDetails(zoneId);
//    }
//
//    @Test
//    void testGetAll_Success() throws Exception {
//        List<ZoneSimpleResponseDto> zones = Arrays.asList(zoneSimpleDto);
//        when(zoneService.getAll()).thenReturn(zones);
//
//        mockMvc.perform(get("/api/zones")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Liste des zones récupérée avec succès"))
//                .andExpect(jsonPath("$.data[0].id").value(zoneSimpleDto.getId()));
//
//        verify(zoneService, times(1)).getAll();
//    }
//
//    @Test
//    void testGetAllPaginated_Success() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//
//        Page<ZoneSimpleResponseDto> zonePage = new PageImpl<>(Arrays.asList(zoneSimpleDto), pageable, 1);
//
//        when(zoneService.getAll(any(Pageable.class))).thenReturn(zonePage);
//
//        mockMvc.perform(get("/api/zones/paginated")
//                        .param("page", "0")  // Changé de 1 à 0 (index commence à 0)
//                        .param("size", "10")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Zones paginées récupérées avec succès"))
//                .andExpect(jsonPath("$.data.content[0].id").value(zoneSimpleDto.getId()))
//                .andExpect(jsonPath("$.data.totalElements").value(1))
//                .andExpect(jsonPath("$.data.size").value(10));
//
//        verify(zoneService, times(1)).getAll(any(Pageable.class));
//    }
//
//
//    @Test
//    void testGetByCodePostal_Found() throws Exception {
//        String codePostal = "20000";
//        when(zoneService.getByCodePostal(codePostal)).thenReturn(Optional.of(zoneSimpleDto));
//
//        mockMvc.perform(get("/api/zones/code-postal/{codePostal}", codePostal)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Zone trouvée par code postal"))
//                .andExpect(jsonPath("$.data.codePostal").value(codePostal));
//
//        verify(zoneService, times(1)).getByCodePostal(codePostal);
//    }
//
//    @Test
//    void testGetByCodePostal_NotFound() throws Exception {
//        String codePostal = "99999";
//        when(zoneService.getByCodePostal(codePostal)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/api/zones/code-postal/{codePostal}", codePostal)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("Aucune zone trouvée avec ce code postal"));
//
//        verify(zoneService, times(1)).getByCodePostal(codePostal);
//    }
//
//    @Test
//    void testGetByNom_Found() throws Exception {
//        String nom = "Casablanca Centre";
//        when(zoneService.getByNom(nom)).thenReturn(Optional.of(zoneSimpleDto));
//
//        mockMvc.perform(get("/api/zones/nom/{nom}", nom)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Zone trouvée par nom"))
//                .andExpect(jsonPath("$.data.nom").value(nom));
//
//        verify(zoneService, times(1)).getByNom(nom);
//    }
//
//    @Test
//    void testGetByNom_NotFound() throws Exception {
//        String nom = "Zone Inexistante";
//        when(zoneService.getByNom(nom)).thenReturn(Optional.empty());
//
//        mockMvc.perform(get("/api/zones/nom/{nom}", nom)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.success").value(false))
//                .andExpect(jsonPath("$.message").value("Aucune zone trouvée avec ce nom"));
//
//        verify(zoneService, times(1)).getByNom(nom);
//    }
//
//    @Test
//    void testSearchByNom_Success() throws Exception {
//        String searchTerm = "casa";
//        List<ZoneSimpleResponseDto> zones = Arrays.asList(zoneSimpleDto);
//        when(zoneService.searchByNom(searchTerm)).thenReturn(zones);
//
//        mockMvc.perform(get("/api/zones/search/nom")
//                        .param("nom", searchTerm)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Recherche par nom effectuée avec succès"))
//                .andExpect(jsonPath("$.data[0].nom").value(zoneSimpleDto.getNom()));
//
//        verify(zoneService, times(1)).searchByNom(searchTerm);
//    }
//
//    @Test
//    void testSearchByKeyword_Success() throws Exception {
//        String keyword = "20000";
//        List<ZoneSimpleResponseDto> zones = Arrays.asList(zoneSimpleDto);
//        when(zoneService.searchByKeyword(keyword)).thenReturn(zones);
//
//        mockMvc.perform(get("/api/zones/search/keyword")
//                        .param("keyword", keyword)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Recherche par mot-clé effectuée avec succès"));
//
//        verify(zoneService, times(1)).searchByKeyword(keyword);
//    }
//
//    @Test
//    void testExistsByCodePostal_True() throws Exception {
//        String codePostal = "20000";
//        when(zoneService.existsByCodePostal(codePostal)).thenReturn(true);
//
//        mockMvc.perform(get("/api/zones/code-postal/{codePostal}/exists", codePostal)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Une zone existe avec ce code postal"))
//                .andExpect(jsonPath("$.data").value(true));
//
//        verify(zoneService, times(1)).existsByCodePostal(codePostal);
//    }
//
//    @Test
//    void testExistsByCodePostal_False() throws Exception {
//        String codePostal = "99999";
//        when(zoneService.existsByCodePostal(codePostal)).thenReturn(false);
//
//        mockMvc.perform(get("/api/zones/code-postal/{codePostal}/exists", codePostal)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Aucune zone avec ce code postal"))
//                .andExpect(jsonPath("$.data").value(false));
//
//        verify(zoneService, times(1)).existsByCodePostal(codePostal);
//    }
//
//    @Test
//    void testDelete_Success() throws Exception {
//        String zoneId = "123e4567-e89b-12d3-a456-426614174000";
//        doNothing().when(zoneService).delete(zoneId);
//
//        mockMvc.perform(delete("/api/zones/{id}", zoneId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Zone supprimée avec succès"));
//
//        verify(zoneService, times(1)).delete(zoneId);
//    }
//
//    @Test
//    void testExistsById_True() throws Exception {
//        String zoneId = "123e4567-e89b-12d3-a456-426614174000";
//        when(zoneService.existsById(zoneId)).thenReturn(true);
//
//        mockMvc.perform(get("/api/zones/{id}/exists", zoneId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("La zone existe"))
//                .andExpect(jsonPath("$.data").value(true));
//
//        verify(zoneService, times(1)).existsById(zoneId);
//    }
//
//    @Test
//    void testExistsById_False() throws Exception {
//        String zoneId = "non-existent-id";
//        when(zoneService.existsById(zoneId)).thenReturn(false);
//
//        mockMvc.perform(get("/api/zones/{id}/exists", zoneId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("La zone n'existe pas"))
//                .andExpect(jsonPath("$.data").value(false));
//
//        verify(zoneService, times(1)).existsById(zoneId);
//    }
//
//    @Test
//    void testExistsByNom_True() throws Exception {
//        String nom = "Casablanca Centre";
//        when(zoneService.existsByNom(nom)).thenReturn(true);
//
//        mockMvc.perform(get("/api/zones/nom/{nom}/exists", nom)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Une zone existe avec ce nom"))
//                .andExpect(jsonPath("$.data").value(true));
//
//        verify(zoneService, times(1)).existsByNom(nom);
//    }
//
//    @Test
//    void testExistsByNom_False() throws Exception {
//        String nom = "Zone Inexistante";
//        when(zoneService.existsByNom(nom)).thenReturn(false);
//
//        mockMvc.perform(get("/api/zones/nom/{nom}/exists", nom)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Aucune zone avec ce nom"))
//                .andExpect(jsonPath("$.data").value(false));
//
//        verify(zoneService, times(1)).existsByNom(nom);
//    }
//}
