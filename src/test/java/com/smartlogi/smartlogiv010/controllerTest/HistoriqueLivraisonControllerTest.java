//package com.smartlogi.smartlogiv010.controllerTest;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smartlogi.smartlogiv010.controller.HistoriqueLivraisonController;
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.HistoriqueLivraisonCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.HistoriqueLivraisonUpdateRequestDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.HistoriqueLivraison.HistoriqueLivraisonResponseDto;
//import com.smartlogi.smartlogiv010.enums.StatutColis;
//import com.smartlogi.smartlogiv010.service.interfaces.HistoriqueLivraisonService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.test.web.servlet.MockMvc;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import static org.hamcrest.Matchers.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(HistoriqueLivraisonController.class)
//@DisplayName("Tests du contrôleur Historique de Livraison")
//class HistoriqueLivraisonControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private HistoriqueLivraisonService historiqueLivraisonService;
//
//    private HistoriqueLivraisonResponseDto historiqueResponse;
//    private LocalDateTime dateChangement;
//    private LocalDateTime dateCreation;
//
//    @BeforeEach
//    void setUp() {
//        dateChangement = LocalDateTime.of(2024, 11, 13, 10, 30, 0);
//        dateCreation = LocalDateTime.of(2024, 11, 13, 9, 0, 0);
//
//        historiqueResponse = new HistoriqueLivraisonResponseDto(
//                "hist-123e4567-e89b-12d3-a456-426614174000",
//                "colis-123e4567-e89b-12d3-a456-426614174000",
//                StatutColis.COLLECTE,
//                dateChangement,
//                "Colis en cours de livraison",
//                dateCreation
//        );
//
//
//    }
//
//    @Nested
//    @DisplayName("Tests de récupération")
//    class GetTests {
//
//        @Test
//        @DisplayName("Devrait récupérer un historique par ID")
//        void shouldGetHistoriqueById() throws Exception {
//            when(historiqueLivraisonService.getById("hist-123e4567-e89b-12d3-a456-426614174000"))
//                    .thenReturn(historiqueResponse);
//
//            mockMvc.perform(get("/api/historique-livraison/hist-123e4567-e89b-12d3-a456-426614174000"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Historique de livraison récupéré avec succès"))
//                    .andExpect(jsonPath("$.data.id").value("hist-123e4567-e89b-12d3-a456-426614174000"))
//                    .andExpect(jsonPath("$.data.colisId").value("colis-123e4567-e89b-12d3-a456-426614174000"))
//                    .andExpect(jsonPath("$.data.statut").value("COLLECTE"))
//                    .andExpect(jsonPath("$.data.commentaire").value("Colis en cours de livraison"));
//
//            verify(historiqueLivraisonService, times(1)).getById("hist-123e4567-e89b-12d3-a456-426614174000");
//        }
//
//        @Test
//        @DisplayName("Devrait récupérer tous les historiques")
//        void shouldGetAllHistoriques() throws Exception {
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historiqueResponse);
//            when(historiqueLivraisonService.getAll()).thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Liste des historiques de livraison récupérée avec succès"))
//                    .andExpect(jsonPath("$.data", hasSize(1)))
//                    .andExpect(jsonPath("$.data[0].id").value("hist-123e4567-e89b-12d3-a456-426614174000"));
//
//            verify(historiqueLivraisonService, times(1)).getAll();
//        }
//
//        @Test
//        @DisplayName("Devrait récupérer les historiques paginés")
//        void shouldGetAllHistoriquesPaginated() throws Exception {
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<HistoriqueLivraisonResponseDto> page = new PageImpl<>(
//                    Arrays.asList(historiqueResponse),
//                    pageable,
//                    1
//            );
//            when(historiqueLivraisonService.getAll(any(Pageable.class))).thenReturn(page);
//
//            mockMvc.perform(get("/api/historique-livraison/paginated")
//                            .param("page", "0")
//                            .param("size", "10"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Historiques de livraison paginés récupérés avec succès"))
//                    .andExpect(jsonPath("$.data.content", hasSize(1)))
//                    .andExpect(jsonPath("$.data.totalElements").value(1))
//                    .andExpect(jsonPath("$.data.content[0].statut").value("COLLECTE"));
//
//            verify(historiqueLivraisonService, times(1)).getAll(any(Pageable.class));
//        }
//
//        @Test
//        @DisplayName("Devrait récupérer l'historique d'un colis")
//        void shouldGetHistoriqueByColis() throws Exception {
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historiqueResponse);
//            when(historiqueLivraisonService.getByColis("colis-123e4567-e89b-12d3-a456-426614174000"))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/colis/colis-123e4567-e89b-12d3-a456-426614174000"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Historique du colis récupéré avec succès"))
//                    .andExpect(jsonPath("$.data", hasSize(1)))
//                    .andExpect(jsonPath("$.data[0].colisId").value("colis-123e4567-e89b-12d3-a456-426614174000"));
//
//            verify(historiqueLivraisonService, times(1)).getByColis("colis-123e4567-e89b-12d3-a456-426614174000");
//        }
//
//        @Test
//        @DisplayName("Devrait récupérer l'historique d'un colis en ordre chronologique")
//        void shouldGetHistoriqueByColisOrderByDateAsc() throws Exception {
//            HistoriqueLivraisonResponseDto historique1 = new HistoriqueLivraisonResponseDto(
//                    "hist-001", "colis-001", StatutColis.EN_TRANSIT,
//                    LocalDateTime.of(2024, 11, 10, 8, 0, 0),
//                    "Premier statut", dateCreation
//            );
//            HistoriqueLivraisonResponseDto historique2 = new HistoriqueLivraisonResponseDto(
//                    "hist-002", "colis-001", StatutColis.COLLECTE,
//                    LocalDateTime.of(2024, 11, 12, 10, 0, 0),
//                    "Deuxième statut", dateCreation
//            );
//
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historique1, historique2);
//            when(historiqueLivraisonService.getByColisOrderByDateAsc("colis-001"))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/colis/colis-001/asc"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Historique du colis (ordre chronologique) récupéré avec succès"))
//                    .andExpect(jsonPath("$.data", hasSize(2)))
//                    .andExpect(jsonPath("$.data[0].statut").value("EN_TRANSIT"))
//                    .andExpect(jsonPath("$.data[1].statut").value("COLLECTE"));
//
//            verify(historiqueLivraisonService, times(1)).getByColisOrderByDateAsc("colis-001");
//        }
//
//        @Test
//        @DisplayName("Devrait récupérer l'historique d'un colis en ordre anti-chronologique")
//        void shouldGetHistoriqueByColisOrderByDateDesc() throws Exception {
//            HistoriqueLivraisonResponseDto historique1 = new HistoriqueLivraisonResponseDto(
//                    "hist-002", "colis-001", StatutColis.COLLECTE,
//                    LocalDateTime.of(2024, 11, 12, 10, 0, 0),
//                    "Deuxième statut", dateCreation
//            );
//            HistoriqueLivraisonResponseDto historique2 = new HistoriqueLivraisonResponseDto(
//                    "hist-001", "colis-001", StatutColis.EN_TRANSIT,
//                    LocalDateTime.of(2024, 11, 10, 8, 0, 0),
//                    "Premier statut", dateCreation
//            );
//
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historique1, historique2);
//            when(historiqueLivraisonService.getByColisOrderByDateDesc("colis-001"))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/colis/colis-001/desc"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Historique du colis (ordre anti-chronologique) récupéré avec succès"))
//                    .andExpect(jsonPath("$.data", hasSize(2)))
//                    .andExpect(jsonPath("$.data[0].statut").value("COLLECTE"))
//                    .andExpect(jsonPath("$.data[1].statut").value("EN_TRANSIT"));
//
//            verify(historiqueLivraisonService, times(1)).getByColisOrderByDateDesc("colis-001");
//        }
//    }
//
//    @Nested
//    @DisplayName("Tests de recherche par statut")
//    class SearchByStatutTests {
//
//        @Test
//        @DisplayName("Devrait rechercher les historiques par statut COLLECTE")
//        void shouldGetHistoriqueByStatut_EnCours() throws Exception {
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historiqueResponse);
//            when(historiqueLivraisonService.getByStatut(StatutColis.COLLECTE))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/statut/COLLECTE"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Historiques par statut récupérés avec succès"))
//                    .andExpect(jsonPath("$.data", hasSize(1)))
//                    .andExpect(jsonPath("$.data[0].statut").value("COLLECTE"));
//
//            verify(historiqueLivraisonService, times(1)).getByStatut(StatutColis.COLLECTE);
//        }
//
//        @Test
//        @DisplayName("Devrait rechercher les historiques par statut LIVRE")
//        void shouldGetHistoriqueByStatut_Livre() throws Exception {
//            HistoriqueLivraisonResponseDto historiquelivre = new HistoriqueLivraisonResponseDto(
//                    "hist-002", "colis-002", StatutColis.LIVRE,
//                    dateChangement, "Colis livré", dateCreation
//            );
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historiquelivre);
//            when(historiqueLivraisonService.getByStatut(StatutColis.LIVRE))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/statut/LIVRE"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Historiques par statut récupérés avec succès"))
//                    .andExpect(jsonPath("$.data", hasSize(1)))
//                    .andExpect(jsonPath("$.data[0].statut").value("LIVRE"));
//
//            verify(historiqueLivraisonService, times(1)).getByStatut(StatutColis.LIVRE);
//        }
//
//        @Test
//        @DisplayName("Devrait rechercher les historiques par statut ANNULE")
//        void shouldGetHistoriqueByStatut_Annule() throws Exception {
//            HistoriqueLivraisonResponseDto historiqueAnnule = new HistoriqueLivraisonResponseDto(
//                    "hist-003", "colis-003", StatutColis.EN_STOCK,
//                    dateChangement, "Colis annulé", dateCreation
//            );
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historiqueAnnule);
//            when(historiqueLivraisonService.getByStatut(StatutColis.EN_STOCK))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/statut/EN_STOCK"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.data[0].statut").value("EN_STOCK"));
//
//            verify(historiqueLivraisonService, times(1)).getByStatut(StatutColis.EN_STOCK);
//        }
//
//        @Test
//        @DisplayName("Devrait rechercher les historiques d'un colis par statut")
//        void shouldGetHistoriqueByColisIdAndStatut() throws Exception {
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historiqueResponse);
//            when(historiqueLivraisonService.getByColisIdAndStatut("colis-123e4567-e89b-12d3-a456-426614174000", StatutColis.COLLECTE))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/colis/colis-123e4567-e89b-12d3-a456-426614174000/statut/COLLECTE"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Historiques du colis par statut récupérés avec succès"))
//                    .andExpect(jsonPath("$.data", hasSize(1)))
//                    .andExpect(jsonPath("$.data[0].colisId").value("colis-123e4567-e89b-12d3-a456-426614174000"))
//                    .andExpect(jsonPath("$.data[0].statut").value("COLLECTE"));
//
//            verify(historiqueLivraisonService, times(1))
//                    .getByColisIdAndStatut("colis-123e4567-e89b-12d3-a456-426614174000", StatutColis.COLLECTE);
//        }
//
//        @Test
//        @DisplayName("Devrait retourner une liste vide pour un statut sans historique")
//        void shouldReturnEmptyListForStatutWithoutHistory() throws Exception {
//            when(historiqueLivraisonService.getByStatut(StatutColis.EN_TRANSIT))
//                    .thenReturn(Arrays.asList());
//
//            mockMvc.perform(get("/api/historique-livraison/statut/EN_TRANSIT"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.data", hasSize(0)));
//
//            verify(historiqueLivraisonService, times(1)).getByStatut(StatutColis.EN_TRANSIT);
//        }
//    }
//
//    @Nested
//    @DisplayName("Tests de recherche par période")
//    class SearchByPeriodTests {
//
//        @Test
//        @DisplayName("Devrait rechercher les historiques par période")
//        void shouldGetHistoriqueByDateChangementBetween() throws Exception {
//            LocalDateTime startDate = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
//            LocalDateTime endDate = LocalDateTime.of(2024, 11, 30, 23, 59, 59);
//
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historiqueResponse);
//            when(historiqueLivraisonService.getByDateChangementBetween(startDate, endDate))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/date-changement")
//                            .param("startDate", "2024-11-01T00:00:00")
//                            .param("endDate", "2024-11-30T23:59:59"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.message").value("Historiques par période de changement récupérés avec succès"))
//                    .andExpect(jsonPath("$.data", hasSize(1)));
//
//            verify(historiqueLivraisonService, times(1)).getByDateChangementBetween(startDate, endDate);
//        }
//
//        @Test
//        @DisplayName("Devrait rechercher les historiques pour une journée spécifique")
//        void shouldGetHistoriqueForSpecificDay() throws Exception {
//            LocalDateTime startDate = LocalDateTime.of(2024, 11, 13, 0, 0, 0);
//            LocalDateTime endDate = LocalDateTime.of(2024, 11, 13, 23, 59, 59);
//
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historiqueResponse);
//            when(historiqueLivraisonService.getByDateChangementBetween(startDate, endDate))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/date-changement")
//                            .param("startDate", "2024-11-13T00:00:00")
//                            .param("endDate", "2024-11-13T23:59:59"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.data", hasSize(1)));
//
//            verify(historiqueLivraisonService, times(1)).getByDateChangementBetween(startDate, endDate);
//        }
//
//        @Test
//        @DisplayName("Devrait retourner une liste vide pour une période sans historique")
//        void shouldReturnEmptyListForPeriodWithoutHistory() throws Exception {
//            LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
//            LocalDateTime endDate = LocalDateTime.of(2023, 1, 31, 23, 59, 59);
//
//            when(historiqueLivraisonService.getByDateChangementBetween(startDate, endDate))
//                    .thenReturn(Arrays.asList());
//
//            mockMvc.perform(get("/api/historique-livraison/date-changement")
//                            .param("startDate", "2023-01-01T00:00:00")
//                            .param("endDate", "2023-01-31T23:59:59"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.data", hasSize(0)));
//
//            verify(historiqueLivraisonService, times(1)).getByDateChangementBetween(startDate, endDate);
//        }
//
//        @Test
//        @DisplayName("Devrait rechercher avec des timestamps précis")
//        void shouldGetHistoriqueWithPreciseTimestamps() throws Exception {
//            LocalDateTime startDate = LocalDateTime.of(2024, 11, 13, 9, 30, 15);
//            LocalDateTime endDate = LocalDateTime.of(2024, 11, 13, 15, 45, 30);
//
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historiqueResponse);
//            when(historiqueLivraisonService.getByDateChangementBetween(startDate, endDate))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/date-changement")
//                            .param("startDate", "2024-11-13T09:30:15")
//                            .param("endDate", "2024-11-13T15:45:30"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.data", hasSize(1)));
//
//            verify(historiqueLivraisonService, times(1)).getByDateChangementBetween(startDate, endDate);
//        }
//    }
//
//    @Nested
//    @DisplayName("Tests de validation des enum")
//    class EnumValidationTests {
//
//        @Test
//        @DisplayName("Devrait accepter tous les statuts valides")
//        void shouldAcceptAllValidStatuts() throws Exception {
//            StatutColis[] allStatuts = {
//                    StatutColis.EN_TRANSIT,
//                    StatutColis.COLLECTE,
//                    StatutColis.LIVRE,
//            };
//
//            for (StatutColis statut : allStatuts) {
//                when(historiqueLivraisonService.getByStatut(statut))
//                        .thenReturn(Arrays.asList());
//
//                mockMvc.perform(get("/api/historique-livraison/statut/" + statut.name()))
//                        .andExpect(status().isOk())
//                        .andExpect(jsonPath("$.success").value(true));
//            }
//        }
//
//        @Test
//        @DisplayName("Devrait rejeter un statut invalide")
//        void shouldRejectInvalidStatut() throws Exception {
//            mockMvc.perform(get("/api/historique-livraison/statut/INVALID_STATUS"))
//                    .andExpect(status().isBadRequest());
//
//            verify(historiqueLivraisonService, never()).getByStatut(any(StatutColis.class));
//        }
//    }
//
//    @Nested
//    @DisplayName("Tests de validation des dates")
//    class DateValidationTests {
//
//        @Test
//        @DisplayName("Devrait valider le format de date avec LocalDateTime")
//        void shouldValidateDateTimeFormat() throws Exception {
//            LocalDateTime startDate = LocalDateTime.of(2024, 11, 1, 0, 0, 0);
//            LocalDateTime endDate = LocalDateTime.of(2024, 11, 30, 23, 59, 59);
//
//            when(historiqueLivraisonService.getByDateChangementBetween(startDate, endDate))
//                    .thenReturn(Arrays.asList(historiqueResponse));
//
//            mockMvc.perform(get("/api/historique-livraison/date-changement")
//                            .param("startDate", "2024-11-01T00:00:00")
//                            .param("endDate", "2024-11-30T23:59:59"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true));
//
//            verify(historiqueLivraisonService, times(1))
//                    .getByDateChangementBetween(startDate, endDate);
//        }
//
//        @Test
//        @DisplayName("Devrait valider les dates avec secondes et millisecondes")
//        void shouldValidateDateTimeWithSecondsAndMillis() throws Exception {
//            LocalDateTime startDate = LocalDateTime.of(2024, 11, 13, 10, 30, 45);
//            LocalDateTime endDate = LocalDateTime.of(2024, 11, 13, 12, 45, 30);
//
//            when(historiqueLivraisonService.getByDateChangementBetween(startDate, endDate))
//                    .thenReturn(Arrays.asList(historiqueResponse));
//
//            mockMvc.perform(get("/api/historique-livraison/date-changement")
//                            .param("startDate", "2024-11-13T10:30:45")
//                            .param("endDate", "2024-11-13T12:45:30"))
//                    .andExpect(status().isOk());
//
//            verify(historiqueLivraisonService, times(1))
//                    .getByDateChangementBetween(startDate, endDate);
//        }
//    }
//
//    @Nested
//    @DisplayName("Tests de cas limites")
//    class EdgeCaseTests {
//
//        @Test
//        @DisplayName("Devrait gérer les listes vides correctement")
//        void shouldHandleEmptyListsCorrectly() throws Exception {
//            when(historiqueLivraisonService.getAll()).thenReturn(Arrays.asList());
//
//            mockMvc.perform(get("/api/historique-livraison"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.data", hasSize(0)));
//
//            verify(historiqueLivraisonService, times(1)).getAll();
//        }
//
//        @Test
//        @DisplayName("Devrait gérer les historiques multiples pour un même colis")
//        void shouldHandleMultipleHistoriquesForSameColis() throws Exception {
//            HistoriqueLivraisonResponseDto historique1 = new HistoriqueLivraisonResponseDto(
//                    "hist-001", "colis-001", StatutColis.EN_TRANSIT,
//                    LocalDateTime.of(2024, 11, 10, 8, 0, 0),
//                    "En attente", dateCreation
//            );
//            HistoriqueLivraisonResponseDto historique2 = new HistoriqueLivraisonResponseDto(
//                    "hist-002", "colis-001", StatutColis.COLLECTE,
//                    LocalDateTime.of(2024, 11, 11, 10, 0, 0),
//                    "En cours", dateCreation
//            );
//            HistoriqueLivraisonResponseDto historique3 = new HistoriqueLivraisonResponseDto(
//                    "hist-003", "colis-001", StatutColis.LIVRE,
//                    LocalDateTime.of(2024, 11, 12, 14, 0, 0),
//                    "Livré", dateCreation
//            );
//
//            List<HistoriqueLivraisonResponseDto> historiques = Arrays.asList(historique1, historique2, historique3);
//            when(historiqueLivraisonService.getByColis("colis-001"))
//                    .thenReturn(historiques);
//
//            mockMvc.perform(get("/api/historique-livraison/colis/colis-001"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.data", hasSize(3)))
//                    .andExpect(jsonPath("$.data[0].statut").value("EN_TRANSIT"))
//                    .andExpect(jsonPath("$.data[1].statut").value("COLLECTE"))
//                    .andExpect(jsonPath("$.data[2].statut").value("LIVRE"));
//
//            verify(historiqueLivraisonService, times(1)).getByColis("colis-001");
//        }
//
//        @Test
//        @DisplayName("Devrait gérer les commentaires null")
//        void shouldHandleNullCommentaires() throws Exception {
//            HistoriqueLivraisonResponseDto historiqueWithoutComment = new HistoriqueLivraisonResponseDto(
//                    "hist-004", "colis-004", StatutColis.COLLECTE,
//                    dateChangement, null, dateCreation
//            );
//
//            when(historiqueLivraisonService.getById("hist-004"))
//                    .thenReturn(historiqueWithoutComment);
//
//            mockMvc.perform(get("/api/historique-livraison/hist-004"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.data.commentaire").isEmpty());
//
//            verify(historiqueLivraisonService, times(1)).getById("hist-004");
//        }
//    }
//
//    @Nested
//    @DisplayName("Tests de pagination")
//    class PaginationTests {
//
//        @Test
//        @DisplayName("Devrait paginer avec différentes tailles de page")
//        void shouldPaginateWithDifferentPageSizes() throws Exception {
//            Pageable pageable = PageRequest.of(0, 5);
//            Page<HistoriqueLivraisonResponseDto> page = new PageImpl<>(
//                    Arrays.asList(historiqueResponse),
//                    pageable,
//                    10
//            );
//            when(historiqueLivraisonService.getAll(any(Pageable.class))).thenReturn(page);
//
//            mockMvc.perform(get("/api/historique-livraison/paginated")
//                            .param("page", "0")
//                            .param("size", "5"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true))
//                    .andExpect(jsonPath("$.data.size").value(5))
//                    .andExpect(jsonPath("$.data.totalElements").value(10));
//
//            verify(historiqueLivraisonService, times(1)).getAll(any(Pageable.class));
//        }
//
//        @Test
//        @DisplayName("Devrait paginer avec tri")
//        void shouldPaginateWithSorting() throws Exception {
//            Pageable pageable = PageRequest.of(0, 10);
//            Page<HistoriqueLivraisonResponseDto> page = new PageImpl<>(
//                    Arrays.asList(historiqueResponse),
//                    pageable,
//                    1
//            );
//            when(historiqueLivraisonService.getAll(any(Pageable.class))).thenReturn(page);
//
//            mockMvc.perform(get("/api/historique-livraison/paginated")
//                            .param("page", "0")
//                            .param("size", "10")
//                            .param("sort", "dateChangement,desc"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.success").value(true));
//
//            verify(historiqueLivraisonService, times(1)).getAll(any(Pageable.class));
//        }
//    }
//}
//
