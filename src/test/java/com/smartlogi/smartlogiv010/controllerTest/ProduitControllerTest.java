package com.smartlogi.smartlogiv010.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogi.smartlogiv010.controller.ProduitController;
import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ProduitCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ProduitUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Produit.ProduitAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Produit.ProduitDetailedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Produit.ProduitSimpleResponseDto;
import com.smartlogi.smartlogiv010.service.interfaces.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProduitController.class)
@DisplayName("Tests du contrôleur Produit")
class ProduitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProduitService produitService;

    private ProduitCreateRequestDto createDto;
    private ProduitUpdateRequestDto updateDto;
    private ProduitSimpleResponseDto simpleResponseDto;
    private ProduitAdvancedResponseDto advancedResponseDto;
    private ProduitDetailedResponseDto detailedResponseDto;

    @BeforeEach
    void setUp() {
        // Initialisation des DTOs pour les tests
        createDto = new ProduitCreateRequestDto(
                "Samsung Galaxy S23",
                "Électronique",
                new BigDecimal("0.168"),
                new BigDecimal("799.99")
        );

        updateDto = new ProduitUpdateRequestDto(
                "123e4567-e89b-12d3-a456-426614174000",
                "Samsung Galaxy S23 Ultra",
                "Électronique",
                new BigDecimal("0.234"),
                new BigDecimal("1199.99")
        );

        simpleResponseDto = new ProduitSimpleResponseDto(
                "123e4567-e89b-12d3-a456-426614174000",
                "Samsung Galaxy S23",
                "Électronique",
                new BigDecimal("0.168"),
                new BigDecimal("799.99"),
                LocalDateTime.now()
        );

        advancedResponseDto = new ProduitAdvancedResponseDto(
                "123e4567-e89b-12d3-a456-426614174000",
                "Samsung Galaxy S23",
                "Électronique",
                new BigDecimal("0.168"),
                new BigDecimal("799.99"),
                LocalDateTime.now(),
                5,
                15,
                new BigDecimal("11999.85")
        );

        detailedResponseDto = new ProduitDetailedResponseDto(
                "123e4567-e89b-12d3-a456-426614174000",
                "Samsung Galaxy S23",
                "Électronique",
                new BigDecimal("0.168"),
                new BigDecimal("799.99"),
                LocalDateTime.now(),
                5,
                15,
                new BigDecimal("11999.85"),
                Arrays.asList()
        );
    }

    @Test
    @DisplayName("Créer un produit - Succès")
    void testCreateProduit_Success() throws Exception {
        // Given
        when(produitService.create(any(ProduitCreateRequestDto.class)))
                .thenReturn(simpleResponseDto);

        // When & Then
        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Produit créé avec succès")))
                .andExpect(jsonPath("$.data.id", is(simpleResponseDto.getId())))
                .andExpect(jsonPath("$.data.nom", is(simpleResponseDto.getNom())))
                .andExpect(jsonPath("$.data.categorie", is(simpleResponseDto.getCategorie())))
                .andExpect(jsonPath("$.data.poids").value(simpleResponseDto.getPoids()))
                .andExpect(jsonPath("$.data.prix").value(simpleResponseDto.getPrix()));

        verify(produitService, times(1)).create(any(ProduitCreateRequestDto.class));
    }

    @Test
    @DisplayName("Créer un produit - Validation échouée (nom vide)")
    void testCreateProduit_ValidationFailed_EmptyName() throws Exception {
        // Given
        createDto.setNom("");

        // When & Then
        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(produitService, never()).create(any(ProduitCreateRequestDto.class));
    }

    @Test
    @DisplayName("Créer un produit - Prix invalide (négatif)")
    void testCreateProduit_InvalidPrice() throws Exception {
        // Given
        createDto.setPrix(new BigDecimal("-10.00"));

        // When & Then
        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(produitService, never()).create(any(ProduitCreateRequestDto.class));
    }

    @Test
    @DisplayName("Créer un produit - Poids invalide")
    void testCreateProduit_InvalidWeight() throws Exception {
        // Given
        createDto.setPoids(new BigDecimal("0.001"));

        // When & Then
        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(produitService, never()).create(any(ProduitCreateRequestDto.class));
    }

    @Test
    @DisplayName("Créer un produit - Catégorie invalide (caractères spéciaux)")
    void testCreateProduit_InvalidCategory() throws Exception {
        // Given
        createDto.setCategorie("Électronique@123");

        // When & Then
        mockMvc.perform(post("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(produitService, never()).create(any(ProduitCreateRequestDto.class));
    }

    @Test
    @DisplayName("Mettre à jour un produit - Succès")
    void testUpdateProduit_Success() throws Exception {
        // Given
        String produitId = "123e4567-e89b-12d3-a456-426614174000";
        ProduitSimpleResponseDto updatedResponse = new ProduitSimpleResponseDto(
                produitId,
                updateDto.getNom(),
                updateDto.getCategorie(),
                updateDto.getPoids(),
                updateDto.getPrix(),
                LocalDateTime.now()
        );

        when(produitService.update(eq(produitId), any(ProduitUpdateRequestDto.class)))
                .thenReturn(updatedResponse);

        // When & Then
        mockMvc.perform(put("/api/produits/{id}", produitId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Produit mis à jour avec succès")))
                .andExpect(jsonPath("$.data.id", is(produitId)))
                .andExpect(jsonPath("$.data.nom", is(updateDto.getNom())));

        verify(produitService, times(1)).update(eq(produitId), any(ProduitUpdateRequestDto.class));
    }

    @Test
    @DisplayName("Obtenir un produit par ID - Succès")
    void testGetProduitById_Success() throws Exception {
        // Given
        String produitId = "123e4567-e89b-12d3-a456-426614174000";
        when(produitService.getById(produitId)).thenReturn(simpleResponseDto);

        // When & Then
        mockMvc.perform(get("/api/produits/{id}", produitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Produit récupéré avec succès")))
                .andExpect(jsonPath("$.data.id", is(simpleResponseDto.getId())))
                .andExpect(jsonPath("$.data.nom", is(simpleResponseDto.getNom())));

        verify(produitService, times(1)).getById(produitId);
    }

    @Test
    @DisplayName("Obtenir un produit avec statistiques - Succès")
    void testGetProduitByIdWithStats_Success() throws Exception {
        // Given
        String produitId = "123e4567-e89b-12d3-a456-426614174000";
        when(produitService.getByIdWithStats(produitId)).thenReturn(advancedResponseDto);

        // When & Then
        mockMvc.perform(get("/api/produits/{id}/advanced", produitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Produit avec statistiques récupéré avec succès")))
                .andExpect(jsonPath("$.data.id", is(advancedResponseDto.getId())))
                .andExpect(jsonPath("$.data.nombreColisAssocies", is(5)))
                .andExpect(jsonPath("$.data.quantiteTotaleVendue", is(15)))
                .andExpect(jsonPath("$.data.chiffreAffaireTotal").value(advancedResponseDto.getChiffreAffaireTotal()));

        verify(produitService, times(1)).getByIdWithStats(produitId);
    }

    @Test
    @DisplayName("Obtenir un produit avec ses colis - Succès")
    void testGetProduitByIdWithColis_Success() throws Exception {
        // Given
        String produitId = "123e4567-e89b-12d3-a456-426614174000";
        when(produitService.getByIdWithColis(produitId)).thenReturn(detailedResponseDto);

        // When & Then
        mockMvc.perform(get("/api/produits/{id}/detailed", produitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Produit avec colis récupéré avec succès")))
                .andExpect(jsonPath("$.data.id", is(detailedResponseDto.getId())))
                .andExpect(jsonPath("$.data.colisProduits", is(notNullValue())));

        verify(produitService, times(1)).getByIdWithColis(produitId);
    }

    @Test
    @DisplayName("Lister tous les produits - Succès")
    void testGetAllProduits_Success() throws Exception {
        // Given
        ProduitSimpleResponseDto produit1 = new ProduitSimpleResponseDto(
                "id1", "Samsung Galaxy S23", "Électronique",
                new BigDecimal("0.168"), new BigDecimal("799.99"), LocalDateTime.now()
        );
        ProduitSimpleResponseDto produit2 = new ProduitSimpleResponseDto(
                "id2", "iPhone 14 Pro", "Électronique",
                new BigDecimal("0.206"), new BigDecimal("1099.99"), LocalDateTime.now()
        );

        List<ProduitSimpleResponseDto> produitsList = Arrays.asList(produit1, produit2);
        when(produitService.getAll()).thenReturn(produitsList);

        // When & Then
        mockMvc.perform(get("/api/produits")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Liste des produits récupérée avec succès")))
                .andExpect(jsonPath("$.data", hasSize(2)))
                .andExpect(jsonPath("$.data[0].nom", is("Samsung Galaxy S23")))
                .andExpect(jsonPath("$.data[1].nom", is("iPhone 14 Pro")));

        verify(produitService, times(1)).getAll();
    }

    @Test
    @DisplayName("Lister tous les produits paginés - Succès")
    void testGetAllProduitsPaginated_Success() throws Exception {
        // Given
        ProduitSimpleResponseDto produit1 = new ProduitSimpleResponseDto(
                "id1", "Samsung Galaxy S23", "Électronique",
                new BigDecimal("0.168"), new BigDecimal("799.99"), LocalDateTime.now()
        );
        ProduitSimpleResponseDto produit2 = new ProduitSimpleResponseDto(
                "id2", "iPhone 14 Pro", "Électronique",
                new BigDecimal("0.206"), new BigDecimal("1099.99"), LocalDateTime.now()
        );

        List<ProduitSimpleResponseDto> produitsList = Arrays.asList(produit1, produit2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProduitSimpleResponseDto> produitsPage = new PageImpl<>(produitsList, pageable, 2);

        when(produitService.getAll(any(Pageable.class))).thenReturn(produitsPage);

        // When & Then
        mockMvc.perform(get("/api/produits/paginated")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Produits paginés récupérés avec succès")))
                .andExpect(jsonPath("$.data.content", hasSize(2)))
                .andExpect(jsonPath("$.data.totalElements", is(2)));

        verify(produitService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Rechercher des produits par nom - Succès")
    void testSearchProduitsByNom_Success() throws Exception {
        // Given
        String searchNom = "Samsung";
        List<ProduitSimpleResponseDto> searchResults = Arrays.asList(simpleResponseDto);

        when(produitService.searchByNom(searchNom)).thenReturn(searchResults);

        // When & Then
        mockMvc.perform(get("/api/produits/search/nom")
                        .param("nom", searchNom)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Recherche par nom effectuée avec succès")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].nom", containsString("Samsung")));

        verify(produitService, times(1)).searchByNom(searchNom);
    }

    @Test
    @DisplayName("Rechercher des produits par catégorie - Succès")
    void testGetProduitsByCategorie_Success() throws Exception {
        // Given
        String categorie = "Électronique";
        List<ProduitSimpleResponseDto> searchResults = Arrays.asList(simpleResponseDto);

        when(produitService.getByCategorie(categorie)).thenReturn(searchResults);

        // When & Then
        mockMvc.perform(get("/api/produits/categorie/{categorie}", categorie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Produits par catégorie récupérés avec succès")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].categorie", is(categorie)));

        verify(produitService, times(1)).getByCategorie(categorie);
    }

    @Test
    @DisplayName("Rechercher des produits par mot-clé - Succès")
    void testSearchProduitsByKeyword_Success() throws Exception {
        // Given
        String keyword = "galaxy";
        List<ProduitSimpleResponseDto> searchResults = Arrays.asList(simpleResponseDto);

        when(produitService.searchByKeyword(keyword)).thenReturn(searchResults);

        // When & Then
        mockMvc.perform(get("/api/produits/search/keyword")
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Recherche par mot-clé effectuée avec succès")))
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(produitService, times(1)).searchByKeyword(keyword);
    }

    @Test
    @DisplayName("Rechercher des produits par plage de prix - Succès")
    void testGetProduitsByPrixBetween_Success() throws Exception {
        // Given
        BigDecimal prixMin = new BigDecimal("500.00");
        BigDecimal prixMax = new BigDecimal("1000.00");
        List<ProduitSimpleResponseDto> searchResults = Arrays.asList(simpleResponseDto);

        when(produitService.getByPrixBetween(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(searchResults);

        // When & Then
        mockMvc.perform(get("/api/produits/prix/range")
                        .param("prixMin", prixMin.toString())
                        .param("prixMax", prixMax.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Produits par plage de prix récupérés avec succès")))
                .andExpect(jsonPath("$.data", hasSize(1)));

        verify(produitService, times(1)).getByPrixBetween(any(BigDecimal.class), any(BigDecimal.class));
    }

    @Test
    @DisplayName("Obtenir toutes les catégories - Succès")
    void testGetAllCategories_Success() throws Exception {
        // Given
        List<String> categories = Arrays.asList("Électronique", "Vêtements", "Alimentation");
        when(produitService.getAllCategories()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/produits/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Liste des catégories récupérée avec succès")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0]", is("Électronique")))
                .andExpect(jsonPath("$.data[1]", is("Vêtements")))
                .andExpect(jsonPath("$.data[2]", is("Alimentation")));

        verify(produitService, times(1)).getAllCategories();
    }

    @Test
    @DisplayName("Supprimer un produit - Succès")
    void testDeleteProduit_Success() throws Exception {
        // Given
        String produitId = "123e4567-e89b-12d3-a456-426614174000";
        doNothing().when(produitService).delete(produitId);

        // When & Then
        mockMvc.perform(delete("/api/produits/{id}", produitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Produit supprimé avec succès")))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(produitService, times(1)).delete(produitId);
    }

    @Test
    @DisplayName("Vérifier l'existence d'un produit - Existe")
    void testExistsById_ProduitExists() throws Exception {
        // Given
        String produitId = "123e4567-e89b-12d3-a456-426614174000";
        when(produitService.existsById(produitId)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/produits/{id}/exists", produitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Le produit existe")))
                .andExpect(jsonPath("$.data", is(true)));

        verify(produitService, times(1)).existsById(produitId);
    }

    @Test
    @DisplayName("Vérifier l'existence d'un produit - N'existe pas")
    void testExistsById_ProduitDoesNotExist() throws Exception {
        // Given
        String produitId = "non-existent-id";
        when(produitService.existsById(produitId)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/produits/{id}/exists", produitId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", is("Le produit n'existe pas")))
                .andExpect(jsonPath("$.data", is(false)));

        verify(produitService, times(1)).existsById(produitId);
    }

    @Test
    @DisplayName("Lister les produits paginés avec tri - Succès")
    void testGetAllProduitsPaginatedSorted_Success() throws Exception {
        // Given
        ProduitSimpleResponseDto produit1 = new ProduitSimpleResponseDto(
                "id1", "iPhone 14 Pro", "Électronique",
                new BigDecimal("0.206"), new BigDecimal("1099.99"), LocalDateTime.now()
        );
        ProduitSimpleResponseDto produit2 = new ProduitSimpleResponseDto(
                "id2", "Samsung Galaxy S23", "Électronique",
                new BigDecimal("0.168"), new BigDecimal("799.99"), LocalDateTime.now()
        );

        List<ProduitSimpleResponseDto> produitsList = Arrays.asList(produit1, produit2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<ProduitSimpleResponseDto> produitsPage = new PageImpl<>(produitsList, pageable, 2);

        when(produitService.getAll(any(Pageable.class))).thenReturn(produitsPage);

        // When & Then
        mockMvc.perform(get("/api/produits/paginated")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "prix,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data.content", hasSize(2)));

        verify(produitService, times(1)).getAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Rechercher des produits par plage de prix - Prix minimum supérieur au prix maximum")
    void testGetProduitsByPrixBetween_InvalidRange() throws Exception {
        // Given
        BigDecimal prixMin = new BigDecimal("1000.00");
        BigDecimal prixMax = new BigDecimal("500.00");

        when(produitService.getByPrixBetween(any(BigDecimal.class), any(BigDecimal.class)))
                .thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/produits/prix/range")
                        .param("prixMin", prixMin.toString())
                        .param("prixMax", prixMax.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)));

        verify(produitService, times(1)).getByPrixBetween(any(BigDecimal.class), any(BigDecimal.class));
    }
}
