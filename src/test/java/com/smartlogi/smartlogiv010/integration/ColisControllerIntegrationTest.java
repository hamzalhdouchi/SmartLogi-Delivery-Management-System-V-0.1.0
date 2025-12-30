package com.smartlogi.smartlogiv010.integration;//package com.smartlogi.smartlogiv010.integration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ColisCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ColisUpdateRequestDto;
//import com.smartlogi.smartlogiv010.entity.*;
//import com.smartlogi.smartlogiv010.enums.Priorite;
//import com.smartlogi.smartlogiv010.enums.StatutColis;
//import com.smartlogi.smartlogiv010.repository.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//@DisplayName("ColisController Integration Tests")
//class ColisControllerIntegrationTest {
//
//    @Autowired private MockMvc mockMvc;
//    @Autowired private ObjectMapper objectMapper;
//    @Autowired private ColisRepository colisRepository;
//    @Autowired private ClientExpediteurRepository clientRepository;
//    @Autowired private DestinataireRepository destinataireRepository;
//    @Autowired private LivreurRepository livreurRepository;
//    @Autowired private ZoneRepository zoneRepository;
//    @Autowired private ProduitRepository produitRepository;
//    @Autowired private ColisProduitRepository colisProduitRepository;
//
//    private ClientExpediteur client;
//    private Destinataire destinataire;
//    private Livreur livreur;
//    private Zone zone;
//    private Produit produit;
//
//    @BeforeEach
//    void setUp() {
//        colisRepository.deleteAll();
//
//        client = new ClientExpediteur();
//        client.setId("CLI-001");
//        client.setNom("Client  Test");
//        client.setEmail("client@e2e.test");
//        client.setTelephone("+212612345678");
//        client = clientRepository.save(client);
//
//        destinataire = new Destinataire();
//        destinataire.setId("DEST-001");
//        destinataire.setNom("Destinataire Test");
//        destinataire.setPrenom("");
//        destinataire.setEmail("dest@e2e.test");
//        destinataire.setTelephone("+212654321098");
//        destinataire.setAdresse("123 Test Street");
//        destinataire = destinataireRepository.save(destinataire);
//
//        zone = new Zone();
//        zone.setId("ZONE-001");
//        zone.setNom("Zone  Test");
//        zone = zoneRepository.save(zone);
//
//        livreur = new Livreur();
//        livreur.setId("LIVRE-001");
//        livreur.setNom("Livreur ");
//        livreur.setPrenom("Test");
//        livreur.setTelephone("+212698765432");
//        livreur.setZone(zone);  // Assigner la zone au livreur
//        livreur = livreurRepository.save(livreur);
//
//        produit = new Produit();
//        produit.setId("PROD-001");
//        produit.setNom("Produit  Test");
//        produit.setCategorie("Test Category");
//        produit.setPrix(new BigDecimal("100.00"));
//        produit.setPoids(new BigDecimal("1.0"));
//        produit = produitRepository.save(produit);
//    }
//
//
//    @Test
//    @DisplayName("001: POST /api/colis - Créer un colis complètement")
//    void test_CreateColisEndToEnd() throws Exception {
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setProduitId(produit.getId());
//        produitDto.setQuantite(2);
//
//        ColisCreateRequestDto requestDto = new ColisCreateRequestDto();
//        requestDto.setDescription("Test  creation");
//        requestDto.setPoids(new BigDecimal("2.0"));
//        requestDto.setStatut(StatutColis.CREE);
//        requestDto.setPriorite(Priorite.HAUTE);
//        requestDto.setVilleDestination("Casablanca");
//        requestDto.setClientExpediteurId(client.getId());
//        requestDto.setDestinataireId(destinataire.getId());
//        requestDto.setLivreurId(livreur.getId());
//        requestDto.setZoneId(zone.getId());
//        requestDto.setProduits(Arrays.asList(produitDto));
//
//        mockMvc.perform(post("/api/colis")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis créé avec succès avec 1 produit(s)"))
//                .andExpect(jsonPath("$.data.id").isNotEmpty())
//                .andExpect(jsonPath("$.data.description").value("Test  creation"))
//                .andExpect(jsonPath("$.data.statut").value("CREE"));
//
//        // Vérifier que le colis a été créé en base de données
//        var allColis = colisRepository.findAll();
//        assert allColis.size() > 0;
//    }
//
//    @Test
//    @DisplayName("002: GET /api/colis/{id} - Récupérer un colis par ID")
//    void test_GetColisById() throws Exception {
//        Colis colis = new Colis();
//        colis.setId("COLIS-GET-001");
//        colis.setDescription("Colis to get");
//        colis.setStatut(StatutColis.CREE);
//        colis.setPriorite(Priorite.NORMALE);
//        colis.setVilleDestination("Casablanca");
//        colis.setClientExpediteur(client);
//        colis.setDestinataire(destinataire);
//        colis.setLivreur(livreur);
//        colis.setZone(zone);
//        colis.setDateCreation(LocalDateTime.now());
//        colis.setHistorique(new ArrayList<>());
//        colisRepository.save(colis);
//
//        mockMvc.perform(get("/api/colis/{id}", colis.getId())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value(colis.getId()))
//                .andExpect(jsonPath("$.data.description").value("Colis to get"))
//                .andExpect(jsonPath("$.data.statut").value("CREE"));
//    }
//
//    @Test
//    @DisplayName("003: GET /api/colis/{id}/detailed - Récupérer les détails complets")
//    void test_GetColisWithDetails() throws Exception {
//
//        Colis colis = new Colis();
//        colis.setId("COLIS-DETAIL-001");
//        colis.setDescription("Colis with details");
//        colis.setStatut(StatutColis.EN_TRANSIT);
//        colis.setPriorite(Priorite.HAUTE);
//        colis.setVilleDestination("Rabat");
//        colis.setClientExpediteur(client);
//        colis.setDestinataire(destinataire);
//        colis.setLivreur(livreur);
//        colis.setZone(zone);
//        colis.setDateCreation(LocalDateTime.now());
//        colis.setHistorique(new ArrayList<>());
//        colisRepository.save(colis);
//
//        mockMvc.perform(get("/api/colis/{id}/detailed", colis.getId())
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data.id").value(colis.getId()))
//                .andExpect(jsonPath("$.data.description").value("Colis with details"));
//    }
//
//    @Test
//    @DisplayName("004: PUT /api/colis/{id} - Mettre à jour un colis")
//    void test_UpdateColis() throws Exception {
//        Colis colis = new Colis();
//        colis.setId("COLIS-UPDATE-001");
//        colis.setDescription("Original description");
//        colis.setStatut(StatutColis.CREE);
//        colis.setPriorite(Priorite.NORMALE);
//        colis.setVilleDestination("Original City");
//        colis.setClientExpediteur(client);
//        colis.setDestinataire(destinataire);
//        colis.setLivreur(livreur);
//        colis.setZone(zone);
//        colis.setDateCreation(LocalDateTime.now());
//        colis.setHistorique(new ArrayList<>());
//        colisRepository.save(colis);
//
//        ColisUpdateRequestDto updateDto = new ColisUpdateRequestDto();
//        updateDto.setId(colis.getId());
//        updateDto.setDescription("Updated description");
//        updateDto.setPoids(new BigDecimal("10.0"));
//
//        mockMvc.perform(put("/api/colis/{id}", colis.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateDto)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis mis à jour avec succès"));
//
//        var updatedColis = colisRepository.findById(colis.getId());
//        assert updatedColis.isPresent();
//        assert updatedColis.get().getDescription().equals("Updated description");
//    }
//
//    @Test
//    @DisplayName("005: DELETE /api/colis/{id} - Supprimer un colis")
//    void test_DeleteColis() throws Exception {
//
//        Colis colis = new Colis();
//        colis.setId("COLIS-DELETE-001");
//        colis.setDescription("Colis to delete");
//        colis.setStatut(StatutColis.CREE);
//        colis.setPriorite(Priorite.NORMALE);
//        colis.setVilleDestination("Casablanca");
//        colis.setClientExpediteur(client);
//        colis.setDestinataire(destinataire);
//        colis.setLivreur(livreur);
//        colis.setZone(zone);
//        colis.setDateCreation(LocalDateTime.now());
//        colis.setHistorique(new ArrayList<>());
//        colisRepository.save(colis);
//
//        mockMvc.perform(delete("/api/colis/{id}", colis.getId()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Colis supprimé avec succès"));
//
//        var deletedColis = colisRepository.findById(colis.getId());
//        assert deletedColis.isEmpty();
//    }
//
//    @Test
//    @DisplayName("006: POST /api/colis/{colisId}/produits - Ajouter un produit")
//    void test_AddProductToColis() throws Exception {
//
//        Colis colis = new Colis();
//        colis.setId("COLIS-PROD-001");
//        colis.setDescription("Colis for product");
//        colis.setStatut(StatutColis.CREE);
//        colis.setPriorite(Priorite.NORMALE);
//        colis.setVilleDestination("Casablanca");
//        colis.setClientExpediteur(client);
//        colis.setDestinataire(destinataire);
//        colis.setLivreur(livreur);
//        colis.setZone(zone);
//        colis.setDateCreation(LocalDateTime.now());
//        colis.setHistorique(new ArrayList<>());
//        colis.setProduits(new ArrayList<>());
//        colisRepository.save(colis);
//
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setProduitId(produit.getId());
//        produitDto.setQuantite(3);
//
//        mockMvc.perform(post("/api/colis/{colisId}/produits", colis.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(produitDto)))
//                .andDo(print())
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value("Produit existant ajouté au colis avec succès"))
//                .andExpect(jsonPath("$.success").value(true));
//
//        var products = colisProduitRepository.findByColisId(colis.getId());
//        assert products.size() > 0;
//    }
//
//    @Test
//    @DisplayName("007: DELETE /api/colis/{colisId}/produits/{produitId} - Supprimer un produit")
//    void test_RemoveProductFromColis() throws Exception {
//
//        Colis colis = new Colis();
//        colis.setId("COLIS-REM-001");
//        colis.setDescription("Colis for remove product");
//        colis.setStatut(StatutColis.CREE);
//        colis.setPriorite(Priorite.NORMALE);
//        colis.setVilleDestination("Casablanca");
//        colis.setClientExpediteur(client);
//        colis.setDestinataire(destinataire);
//        colis.setLivreur(livreur);
//        colis.setZone(zone);
//        colis.setDateCreation(LocalDateTime.now());
//        colis.setHistorique(new ArrayList<>());
//        colis.setProduits(new ArrayList<>());
//        colisRepository.save(colis);
//
//        ColisProduit colisProduit = new ColisProduit();
//        colisProduit.setId(new ColisProduitId(colis.getId(), produit.getId()));
//        colisProduit.setColis(colis);
//        colisProduit.setProduit(produit);
//        colisProduit.setQuantite(2);
//        colisProduit.setPrix(new BigDecimal("200.00"));
//        colisProduitRepository.save(colisProduit);
//
//        mockMvc.perform(delete("/api/colis/{colisId}/produits/{produitId}", colis.getId(), produit.getId()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Produit retiré du colis avec succès"));
//
//        var products = colisProduitRepository.findByColisId(colis.getId());
//        assert products.isEmpty();
//    }
//
//    @Test
//    @DisplayName("008: PUT /api/colis/{colisId}/assigner-livreur/{livreurId}")
//    void test_AssignLivreur() throws Exception {
//
//        Colis colis = new Colis();
//        colis.setId("COLIS-ASSIGN-001");
//        colis.setDescription("Colis for assign");
//        colis.setStatut(StatutColis.CREE);
//        colis.setPriorite(Priorite.NORMALE);
//        colis.setVilleDestination("Casablanca");
//        colis.setClientExpediteur(client);
//        colis.setDestinataire(destinataire);
//        colis.setLivreur(null); // Pas encore assigné
//        colis.setZone(zone);
//        colis.setDateCreation(LocalDateTime.now());
//        colis.setHistorique(new ArrayList<>());
//        colisRepository.save(colis);
//
//        mockMvc.perform(put("/api/colis/{colisId}/assigner-livreur/{livreurId}", colis.getId(), livreur.getId()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Livreur assigné au colis avec succès"));
//
//        var assignedColis = colisRepository.findById(colis.getId());
//        assert assignedColis.isPresent();
//        assert assignedColis.get().getLivreur().getId().equals(livreur.getId());
//    }
//
//    @Test
//    @DisplayName("009: PUT /api/colis/{colisId}/changer-statut - Changer le statut")
//    void test_ChangeStatus() throws Exception {
//
//        Colis colis = new Colis();
//        colis.setId("COLIS-STATUS-001");
//        colis.setDescription("Colis for status change");
//        colis.setStatut(StatutColis.CREE);
//        colis.setPriorite(Priorite.NORMALE);
//        colis.setVilleDestination("Casablanca");
//        colis.setClientExpediteur(client);
//        colis.setDestinataire(destinataire);
//        colis.setLivreur(livreur);
//        colis.setZone(zone);
//        colis.setDateCreation(LocalDateTime.now());
//        colis.setHistorique(new ArrayList<>());
//        colisRepository.save(colis);
//
//        mockMvc.perform(put("/api/colis/{colisId}/changer-statut", colis.getId())
//                        .param("nouveauStatut", "EN_TRANSIT")
//                        .param("commentaire", "En route vers le destinataire"))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.message").value("Statut du colis modifié avec succès"));
//
//        var updatedColis = colisRepository.findById(colis.getId());
//        assert updatedColis.isPresent();
//        assert updatedColis.get().getStatut() == StatutColis.EN_TRANSIT;
//    }
//
//    @Test
//    @DisplayName("010: GET /api/colis/statut/{statut} - Rechercher par statut")
//    void test_SearchByStatut() throws Exception {
//        for (int i = 0; i < 3; i++) {
//            Colis colis = new Colis();
//            colis.setId("COLIS-SEARCH-00" + i);
//            colis.setDescription("Search test " + i);
//            colis.setStatut(StatutColis.CREE);
//            colis.setPriorite(Priorite.NORMALE);
//            colis.setVilleDestination("Casablanca");
//            colis.setClientExpediteur(client);
//            colis.setDestinataire(destinataire);
//            colis.setLivreur(livreur);
//            colis.setZone(zone);
//            colis.setDateCreation(LocalDateTime.now());
//            colis.setHistorique(new ArrayList<>());
//            colisRepository.save(colis);
//        }
//
//        mockMvc.perform(get("/api/colis/statut/{statut}", "CREE")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isArray());
//    }
//
//    @Test
//    @DisplayName("011: GET /api/colis - Lister tous les colis")
//    void test_GetAllColis() throws Exception {
//
//        for (int i = 0; i < 2; i++) {
//            Colis colis = new Colis();
//            colis.setId("COLIS-ALL-00" + i);
//            colis.setDescription("All list test " + i);
//            colis.setStatut(StatutColis.CREE);
//            colis.setPriorite(Priorite.NORMALE);
//            colis.setVilleDestination("Casablanca");
//            colis.setClientExpediteur(client);
//            colis.setDestinataire(destinataire);
//            colis.setLivreur(livreur);
//            colis.setZone(zone);
//            colis.setDateCreation(LocalDateTime.now());
//            colis.setHistorique(new ArrayList<>());
//            colisRepository.save(colis);
//        }
//
//        mockMvc.perform(get("/api/colis")
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isArray());
//    }
//}