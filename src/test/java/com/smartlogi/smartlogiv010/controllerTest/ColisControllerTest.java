//package com.smartlogi.smartlogiv010.controllerTest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smartlogi.smartlogiv010.controller.ColisController;
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ColisCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ProduitCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ColisUpdateRequestDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisAdvancedResponseDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisSimpleResponseDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.PoidsParLivreur.PoidsParLivreurDTO;
//import com.smartlogi.smartlogiv010.dto.responseDTO.PoidsParLivreur.PoidsParLivreurDetailDTO;
//import com.smartlogi.smartlogiv010.entity.ColisProduit;
//import com.smartlogi.smartlogiv010.entity.ColisProduitId;
//import com.smartlogi.smartlogiv010.entity.Produit;
//import com.smartlogi.smartlogiv010.enums.Priorite;
//import com.smartlogi.smartlogiv010.enums.StatutColis;
//import com.smartlogi.smartlogiv010.service.interfaces.ColisService;
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
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(ColisController.class)
//class ColisControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private ColisService colisService;
//
//    private ColisSimpleResponseDto colisSimpleDto;
//    private ColisAdvancedResponseDto colisAdvancedDto;
//    private ColisCreateRequestDto createRequestDto;
//    private ColisUpdateRequestDto updateRequestDto;
//    private ColisProduit colisProduit;
//
//    @BeforeEach
//    void setUp() {
//        colisSimpleDto = new ColisSimpleResponseDto();
//        colisSimpleDto.setId("colis-123");
//        colisSimpleDto.setDescription("Test Colis");
//        colisSimpleDto.setStatut(StatutColis.CREE);
//        colisSimpleDto.setPriorite(Priorite.HAUTE);
//
//        colisAdvancedDto = new ColisAdvancedResponseDto();
//        colisAdvancedDto.setId("colis-123");
//        colisAdvancedDto.setDescription("Test Colis Advanced");
//        colisAdvancedDto.setStatut(StatutColis.COLLECTE);
//
//        createRequestDto = new ColisCreateRequestDto();
//        createRequestDto.setDescription("Nouveau Colis");
//        createRequestDto.setClientExpediteurId("client-123");
//        createRequestDto.setDestinataireId("dest-456");
//        createRequestDto.setPriorite(Priorite.HAUTE);
//        createRequestDto.setPoids(BigDecimal.valueOf(0.45));
//        createRequestDto.setVilleDestination("casablanca");
//        createRequestDto.setZoneId("zone-123");
//        createRequestDto.setStatut(StatutColis.CREE);
//
//        updateRequestDto = new ColisUpdateRequestDto();
//        updateRequestDto.setDescription("Colis Mis à Jour");
//
//        ColisProduitId compositeKey = new ColisProduitId("colis-123", "produit-123");
//
//        Produit produit = new Produit();
//        produit.setNom("product 124");
//        produit.setPoids(BigDecimal.valueOf(0.45));
//        produit.setCategorie("electro");
//
//        colisProduit = new ColisProduit();
//        colisProduit.setId(compositeKey);
//        colisProduit.setQuantite(5);
//        colisProduit.setPrix(BigDecimal.valueOf(500));
//        colisProduit.setProduit(produit);
//    }
//
//
//    @Test
//    void testCreate_Success() throws Exception {
//        when(colisService.create(any(ColisCreateRequestDto.class)))
//                .thenReturn(colisSimpleDto);
//
//        mockMvc.perform(post("/api/colis")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(createRequestDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis créé avec succès"))
//                .andExpect(jsonPath("$.data.id").value(colisSimpleDto.getId()))
//                .andExpect(jsonPath("$.data.description").value(colisSimpleDto.getDescription()));
//
//        verify(colisService, times(1)).create(any(ColisCreateRequestDto.class));
//    }
//
//    @Test
//    void testAjouterProduit_Success() throws Exception {
//        ProduitCreateRequestDto produit = new ProduitCreateRequestDto();
//        produit.setNom("product 124");
//        produit.setPoids(BigDecimal.valueOf(0.45));
//        produit.setCategorie("electro");
//        produit.setPrix(BigDecimal.valueOf(500));
//        String colisId = "colis-123";
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setProduitId("produit-456");
//        produitDto.setQuantite(3);
//        produitDto.setPrix(BigDecimal.valueOf(500));
//        produitDto.setNouveauProduit(produit);
//
//        doNothing().when(colisService).ajouterProduit(eq(colisId), any(ColisCreateRequestDto.ProduitColisDto.class));
//
//        mockMvc.perform(post("/api/colis/{colisId}/produits", colisId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(produitDto)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Produit existant ajouté au colis avec succès"));
//
//        verify(colisService, times(1)).ajouterProduit(eq(colisId), any(ColisCreateRequestDto.ProduitColisDto.class));
//    }
//
//
//    @Test
//    void testGetById_Success() throws Exception {
//        String colisId = "colis-123";
//        when(colisService.getById(colisId)).thenReturn(colisSimpleDto);
//
//        mockMvc.perform(get("/api/colis/{id}", colisId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis récupéré avec succès"))
//                .andExpect(jsonPath("$.data.id").value(colisSimpleDto.getId()));
//
//        verify(colisService, times(1)).getById(colisId);
//    }
//
//    @Test
//    void testGetByIdWithDetails_Success() throws Exception {
//        String colisId = "colis-123";
//        when(colisService.getByIdWithDetails(colisId)).thenReturn(colisAdvancedDto);
//
//        mockMvc.perform(get("/api/colis/{id}/detailed", colisId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis détaillé récupéré avec succès"))
//                .andExpect(jsonPath("$.data.id").value(colisAdvancedDto.getId()));
//
//        verify(colisService, times(1)).getByIdWithDetails(colisId);
//    }
//
//    @Test
//    void testGetAll_Success() throws Exception {
//        Page<ColisSimpleResponseDto> colisPage = new PageImpl<>(Arrays.asList(colisSimpleDto));
//        when(colisService.getAll(any(Pageable.class))).thenReturn(colisPage);
//
//        mockMvc.perform(get("/api/colis")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Liste des colis récupérée avec succès"))
//                .andExpect(jsonPath("$.data[0].id").value(colisSimpleDto.getId()));
//
//        verify(colisService, times(1)).getAll(any(Pageable.class));
//    }
//
//    @Test
//    void testGetAllPaginated_Success() throws Exception {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<ColisSimpleResponseDto> colisPage = new PageImpl<>(
//                Arrays.asList(colisSimpleDto),
//                pageable,
//                1
//        );
//        when(colisService.getAll(any(Pageable.class))).thenReturn(colisPage);
//
//        mockMvc.perform(get("/api/colis/paginated")
//                        .param("page", "0")
//                        .param("size", "10")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis paginés récupérés avec succès"))
//                .andExpect(jsonPath("$.data.content[0].id").value(colisSimpleDto.getId()));
//
//        verify(colisService, times(1)).getAll(any(Pageable.class));
//    }
//
//
//    @Test
//    void testGetByStatut_Success() throws Exception {
//        StatutColis statut = StatutColis.CREE;
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByStatut(statut)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/statut/{statut}", statut)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par statut récupérés avec succès"))
//                .andExpect(jsonPath("$.data[0].statut").value(statut.toString()));
//
//        verify(colisService, times(1)).getByStatut(statut);
//    }
//
//    @Test
//    void testGetByStatutPaginated_Success() throws Exception {
//        StatutColis statut = StatutColis.CREE;
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<ColisSimpleResponseDto> colisPage = new PageImpl<>(
//                Arrays.asList(colisSimpleDto),
//                pageable,
//                1
//        );
//        when(colisService.getByStatut(eq(statut), any(Pageable.class))).thenReturn(colisPage);
//
//        mockMvc.perform(get("/api/colis/statut/{statut}/paginated", statut)
//                        .param("page", "0")
//                        .param("size", "10")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.content[0].id").value(colisSimpleDto.getId()));
//
//        verify(colisService, times(1)).getByStatut(eq(statut), any(Pageable.class));
//    }
//
//
//    @Test
//    void testGetByClientExpediteur_Success() throws Exception {
//        String clientId = "client-123";
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByClientExpediteur(clientId)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/client-expediteur/{clientId}", clientId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par client expéditeur récupérés avec succès"));
//
//        verify(colisService, times(1)).getByClientExpediteur(clientId);
//    }
//
//    @Test
//    void testGetByDestinataire_Success() throws Exception {
//        String destinataireId = "dest-456";
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByDestinataire(destinataireId)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/destinataire/{destinataireId}", destinataireId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par destinataire récupérés avec succès"));
//
//        verify(colisService, times(1)).getByDestinataire(destinataireId);
//    }
//
//    @Test
//    void testGetByLivreur_Success() throws Exception {
//        String livreurId = "livreur-789";
//        List<ColisAdvancedResponseDto> colisList = Arrays.asList(colisAdvancedDto);
//        when(colisService.getByLivreur(livreurId)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/livreur/{livreurId}", livreurId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par livreur récupérés avec succès"));
//
//        verify(colisService, times(1)).getByLivreur(livreurId);
//    }
//
//    @Test
//    void testGetByZone_Success() throws Exception {
//        String zoneId = "zone-001";
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByZone(zoneId)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/zone/{zoneId}", zoneId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par zone récupérés avec succès"));
//
//        verify(colisService, times(1)).getByZone(zoneId);
//    }
//
//    @Test
//    void testGetByZoneId_Paginated_Success() throws Exception {
//        String zoneId = "zone-001";
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<ColisSimpleResponseDto> colisPage = new PageImpl<>(
//                Arrays.asList(colisSimpleDto),
//                pageable,
//                1
//        );
//        when(colisService.getByZoneId(eq(zoneId), any(Pageable.class))).thenReturn(colisPage);
//
//        mockMvc.perform(get("/api/colis/zone/{zoneId}/paginated", zoneId)
//                        .param("page", "0")
//                        .param("size", "10")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par zone paginés récupérés avec succès"));
//
//        verify(colisService, times(1)).getByZoneId(eq(zoneId), any(Pageable.class));
//    }
//
//    @Test
//    void testSearchByVilleDestination_Success() throws Exception {
//        String ville = "Casablanca";
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.searchByVilleDestination(ville)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/ville-destination/{ville}", ville)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par ville de destination récupérés avec succès"));
//
//        verify(colisService, times(1)).searchByVilleDestination(ville);
//    }
//
//
//    @Test
//    void testGetByPriorite_Success() throws Exception {
//        Priorite priorite = Priorite.HAUTE;
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByPriorite(priorite)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/priorite/{priorite}", priorite)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par priorité récupérés avec succès"))
//                .andExpect(jsonPath("$.data[0].priorite").value(priorite.toString()));
//
//        verify(colisService, times(1)).getByPriorite(priorite);
//    }
//
//    @Test
//    void testGetByPrioriteAndStatut_Success() throws Exception {
//        Priorite priorite = Priorite.HAUTE;
//        StatutColis statut = StatutColis.COLLECTE;
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByPrioriteAndStatut(priorite, statut)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/priorite/{priorite}/statut/{statut}", priorite, statut)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par priorité et statut récupérés avec succès"));
//
//        verify(colisService, times(1)).getByPrioriteAndStatut(priorite, statut);
//    }
//
//    @Test
//    void testGetByLivreurAndStatut_Success() throws Exception {
//        String livreurId = "livreur-789";
//        StatutColis statut = StatutColis.COLLECTE;
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByLivreurAndStatut(livreurId, statut)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/livreur/{livreurId}/statut/{statut}", livreurId, statut)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par livreur et statut récupérés avec succès"));
//
//        verify(colisService, times(1)).getByLivreurAndStatut(livreurId, statut);
//    }
//
//    @Test
//    void testGetByZoneAndStatut_Success() throws Exception {
//        String zoneId = "zone-001";
//        StatutColis statut = StatutColis.CREE;
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByZoneAndStatut(zoneId, statut)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/zone/{zoneId}/statut/{statut}", zoneId, statut)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par zone et statut récupérés avec succès"));
//
//        verify(colisService, times(1)).getByZoneAndStatut(zoneId, statut);
//    }
//
//    @Test
//    void testGetByVilleDestinationAndStatut_Success() throws Exception {
//        String ville = "Casablanca";
//        StatutColis statut = StatutColis.COLLECTE;
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByVilleDestinationAndStatut(ville, statut)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/ville-destination/{ville}/statut/{statut}", ville, statut)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par ville destination et statut récupérés avec succès"));
//
//        verify(colisService, times(1)).getByVilleDestinationAndStatut(ville, statut);
//    }
//
//    @Test
//    void testSearchByKeyword_Success() throws Exception {
//        String keyword = "electronique";
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.searchByKeyword(keyword)).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/search/keyword")
//                        .param("keyword", keyword)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Recherche par mot-clé effectuée avec succès"));
//
//        verify(colisService, times(1)).searchByKeyword(keyword);
//    }
//
//    @Test
//    void testGetColisEnRetard_Success() throws Exception {
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getColisEnRetard()).thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/en-retard")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis en retard récupérés avec succès"));
//
//        verify(colisService, times(1)).getColisEnRetard();
//    }
//
//    @Test
//    void testGetByDateCreationBetween_Success() throws Exception {
//        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 0, 0);
//        LocalDateTime endDate = LocalDateTime.of(2024, 12, 31, 23, 59);
//        List<ColisSimpleResponseDto> colisList = Arrays.asList(colisSimpleDto);
//        when(colisService.getByDateCreationBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
//                .thenReturn(colisList);
//
//        mockMvc.perform(get("/api/colis/date-creation")
//                        .param("startDate", startDate.toString())
//                        .param("endDate", endDate.toString())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis par période de création récupérés avec succès"));
//
//        verify(colisService, times(1)).getByDateCreationBetween(any(LocalDateTime.class), any(LocalDateTime.class));
//    }
//
//    // === TESTS MODIFICATION ===
//
//    @Test
//    void testUpdate_Success() throws Exception {
//        String colisId = "colis-123";
//        when(colisService.update(eq(colisId), any(ColisUpdateRequestDto.class)))
//                .thenReturn(colisSimpleDto);
//
//        mockMvc.perform(put("/api/colis/{id}", colisId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequestDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis mis à jour avec succès"))
//                .andExpect(jsonPath("$.data.id").value(colisSimpleDto.getId()));
//
//        verify(colisService, times(1)).update(eq(colisId), any(ColisUpdateRequestDto.class));
//    }
//
//    @Test
//    void testAssignerLivreur_Success() throws Exception {
//        String colisId = "colis-123";
//        String livreurId = "livreur-789";
//        doNothing().when(colisService).assignerLivreur(colisId, livreurId);
//
//        mockMvc.perform(put("/api/colis/{colisId}/assigner-livreur/{livreurId}", colisId, livreurId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Livreur assigné au colis avec succès"));
//
//        verify(colisService, times(1)).assignerLivreur(colisId, livreurId);
//    }
//
//    @Test
//    void testChangerStatut_Success() throws Exception {
//        String colisId = "colis-123";
//        StatutColis nouveauStatut = StatutColis.EN_TRANSIT;
//        String commentaire = "En cours de livraison";
//        doNothing().when(colisService).changerStatut(colisId, nouveauStatut, commentaire);
//
//        mockMvc.perform(put("/api/colis/{colisId}/changer-statut", colisId)
//                        .param("nouveauStatut", nouveauStatut.toString())
//                        .param("commentaire", commentaire))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Statut du colis modifié avec succès"));
//
//        verify(colisService, times(1)).changerStatut(colisId, nouveauStatut, commentaire);
//    }
//
//    // === TESTS GESTION PRODUITS ===
//
//    @Test
//    void testGetProduitsByColis_Success() throws Exception {
//        String colisId = "colis-123";
//        List<ColisProduit> produits = Arrays.asList(colisProduit);
//        when(colisService.getProduitsByColis(colisId)).thenReturn(produits);
//
//        mockMvc.perform(get("/api/colis/{colisId}/produits", colisId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Produits du colis récupérés avec succès"));
//
//        verify(colisService, times(1)).getProduitsByColis(colisId);
//    }
//
//    @Test
//    void testMettreAJourQuantiteProduit_Success() throws Exception {
//        String colisId = "colis-123";
//        String produitId = "produit-456";
//        Integer nouvelleQuantite = 10;
//        doNothing().when(colisService).mettreAJourQuantiteProduit(colisId, produitId, nouvelleQuantite);
//
//        mockMvc.perform(put("/api/colis/{colisId}/produits/{produitId}/quantite", colisId, produitId)
//                        .param("nouvelleQuantite", nouvelleQuantite.toString()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Quantité du produit mise à jour avec succès"));
//
//        verify(colisService, times(1)).mettreAJourQuantiteProduit(colisId, produitId, nouvelleQuantite);
//    }
//
//    @Test
//    void testSupprimerProduit_Success() throws Exception {
//        String colisId = "colis-123";
//        String produitId = "produit-456";
//        doNothing().when(colisService).supprimerProduit(colisId, produitId);
//
//        mockMvc.perform(delete("/api/colis/{colisId}/produits/{produitId}", colisId, produitId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Produit retiré du colis avec succès"));
//
//        verify(colisService, times(1)).supprimerProduit(colisId, produitId);
//    }
//
//    @Test
//    void testGetTotalPrixColis_Success() throws Exception {
//        String colisId = "colis-123";
//        BigDecimal totalPrix = new BigDecimal("250.50");
//        when(colisService.getPrixTotalColis(colisId)).thenReturn(totalPrix);
//
//        mockMvc.perform(get("/api/colis/{colisId}/total-prix", colisId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Prix total du colis calculé avec succès"))
//                .andExpect(jsonPath("$.data").value(250.50));
//
//        verify(colisService, times(1)).getPrixTotalColis(colisId);
//    }
//
//    @Test
//    void testProduitExisteDansColis_True() throws Exception {
//        String colisId = "colis-123";
//        String produitId = "produit-456";
//        when(colisService.produitExisteDansColis(colisId, produitId)).thenReturn(true);
//
//        mockMvc.perform(get("/api/colis/{colisId}/produits/{produitId}/existe", colisId, produitId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").value(true));
//
//        verify(colisService, times(1)).produitExisteDansColis(colisId, produitId);
//    }
//
//    // === TESTS SUPPRESSION ===
//
//    @Test
//    void testDelete_Success() throws Exception {
//        String colisId = "colis-123";
//        doNothing().when(colisService).delete(colisId);
//
//        mockMvc.perform(delete("/api/colis/{id}", colisId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis supprimé avec succès"));
//
//        verify(colisService, times(1)).delete(colisId);
//    }
//
//    // === TESTS STATISTIQUES ===
//
//    @Test
//    void testCountByStatut_Success() throws Exception {
//        StatutColis statut = StatutColis.CREE;
//        long count = 42L;
//        when(colisService.countByStatut(statut)).thenReturn(count);
//
//        mockMvc.perform(get("/api/colis/statistiques/statut/{statut}/count", statut)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Nombre de colis par statut récupéré avec succès"))
//                .andExpect(jsonPath("$.data").value(42));
//
//        verify(colisService, times(1)).countByStatut(statut);
//    }
//
//    @Test
//    void testCountByZoneAndStatut_Success() throws Exception {
//        String zoneId = "zone-001";
//        StatutColis statut = StatutColis.CREE;
//        long count = 15L;
//        when(colisService.countByZoneAndStatut(zoneId, statut)).thenReturn(count);
//
//        mockMvc.perform(get("/api/colis/statistiques/zone/{zoneId}/statut/{statut}/count", zoneId, statut)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").value(15));
//
//        verify(colisService, times(1)).countByZoneAndStatut(zoneId, statut);
//    }
//
//    @Test
//    void testCountByLivreurAndStatut_Success() throws Exception {
//        String livreurId = "livreur-789";
//        StatutColis statut = StatutColis.COLLECTE;
//        long count = 8L;
//        when(colisService.countByLivreurAndStatut(livreurId, statut)).thenReturn(count);
//
//        mockMvc.perform(get("/api/colis/statistiques/livreur/{livreurId}/statut/{statut}/count", livreurId, statut)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").value(8));
//
//        verify(colisService, times(1)).countByLivreurAndStatut(livreurId, statut);
//    }
//
//    @Test
//    void testPoidsTotal_Success() throws Exception {
//        String colisId = "colis-123";
//        Double poids = 45.5;
//        when(colisService.calculateTotal(colisId)).thenReturn(poids);
//
//        mockMvc.perform(get("/api/colis/poidstotal/colis/{colisId}", colisId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("le Poids recupere successfully"))
//                .andExpect(jsonPath("$.data").value(45.5));
//
//        verify(colisService, times(1)).calculateTotal(colisId);
//    }
//
//    @Test
//    void testPrixTotal_Success() throws Exception {
//        String colisId = "colis-123";
//        Double prix = 350.75;
//        when(colisService.calculateTotalPrix(colisId)).thenReturn(prix);
//
//        mockMvc.perform(get("/api/colis/prixtotal/colis/{colisId}", colisId)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("le prix recupere successfully"))
//                .andExpect(jsonPath("$.data").value(350.75));
//
//        verify(colisService, times(1)).calculateTotalPrix(colisId);
//    }
//
//    @Test
//    void testGetPoidsTotalParLivreur_Success() throws Exception {
//        PoidsParLivreurDTO dto = new PoidsParLivreurDTO();
//        List<PoidsParLivreurDTO> result = Arrays.asList(dto);
//        when(colisService.getPoidsTotalParLivreur()).thenReturn(result);
//
//        mockMvc.perform(get("/api/colis/poids-par-livreur")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Poids total par livreur récupéré avec succès"));
//
//        verify(colisService, times(1)).getPoidsTotalParLivreur();
//    }
//
//    @Test
//    void testGetPoidsDetailParLivreur_Success() throws Exception {
//        PoidsParLivreurDetailDTO detailDto = new PoidsParLivreurDetailDTO();
//        detailDto.setLivreurId("livreur-789");
//        List<PoidsParLivreurDetailDTO> result = Arrays.asList(detailDto);
//        when(colisService.getPoidsDetailParLivreur()).thenReturn(result);
//
//        mockMvc.perform(get("/api/colis/poids-par-livreur/detail")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Détail poids par livreur récupéré avec succès"));
//
//        verify(colisService, times(1)).getPoidsDetailParLivreur();
//    }
//}
