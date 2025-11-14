package com.smartlogi.smartlogiv010.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogi.smartlogiv010.controller.LivreurController;
import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.LivreurCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.LivreurUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurDetailedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Livreur.LivreurSimpleResponseDto;
import com.smartlogi.smartlogiv010.service.interfaces.LivreurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LivreurController.class)
@DisplayName("Tests du contrôleur Livreur")
class LivreurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LivreurService livreurService;

    private LivreurSimpleResponseDto livreurSimple;
    private LivreurAdvancedResponseDto livreurAdvanced;
    private LivreurDetailedResponseDto livreurDetailed;
    private LivreurCreateRequestDto createRequest;
    private LivreurUpdateRequestDto updateRequest;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        // Préparation des données de test
        livreurSimple = new LivreurSimpleResponseDto(
                "123e4567-e89b-12d3-a456-426614174000",
                "Alami",
                "Mohammed",
                "+212612345678",
                "Moto",
                now,
                "zone-001",
                "Casablanca Centre"
        );

        livreurAdvanced = new LivreurAdvancedResponseDto(
                "123e4567-e89b-12d3-a456-426614174000",
                "Alami",
                "Mohammed",
                "+212612345678",
                "Moto",
                now,
                "zone-001",
                "Casablanca Centre",
                50,
                45,
                5
        );

        livreurDetailed = new LivreurDetailedResponseDto(
                "123e4567-e89b-12d3-a456-426614174000",
                "Alami",
                "Mohammed",
                "+212612345678",
                "Moto",
                now,
                "zone-001",
                "Casablanca Centre",
                50,
                45,
                5,
                Arrays.asList()
        );

        createRequest = new LivreurCreateRequestDto(
                "Alami",
                "Mohammed",
                "+212612345678",
                "Moto",
                "zone-001"
        );

        updateRequest = new LivreurUpdateRequestDto(
                "123e4567-e89b-12d3-a456-426614174000",
                "Alami",
                "Ahmed",
                "+212698765432",
                "Voiture",
                "zone-002"
        );
    }

    @Nested
    @DisplayName("Tests de création")
    class CreateTests {

        @Test
        @DisplayName("Devrait créer un livreur avec succès")
        void shouldCreateLivreurSuccessfully() throws Exception {
            when(livreurService.create(any(LivreurCreateRequestDto.class)))
                    .thenReturn(livreurSimple);

            mockMvc.perform(post("/api/livreurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Livreur créé avec succès"))
                    .andExpect(jsonPath("$.data.id").value(livreurSimple.getId()))
                    .andExpect(jsonPath("$.data.nom").value("Alami"))
                    .andExpect(jsonPath("$.data.prenom").value("Mohammed"))
                    .andExpect(jsonPath("$.data.telephone").value("+212612345678"))
                    .andExpect(jsonPath("$.data.vehicule").value("Moto"));

            verify(livreurService, times(1)).create(any(LivreurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une création avec nom vide")
        void shouldRejectCreateWithBlankNom() throws Exception {
            LivreurCreateRequestDto invalidRequest = new LivreurCreateRequestDto(
                    "",
                    "Mohammed",
                    "+212612345678",
                    "Moto",
                    "zone-001"
            );

            mockMvc.perform(post("/api/livreurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(livreurService, never()).create(any(LivreurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une création avec téléphone invalide")
        void shouldRejectCreateWithInvalidTelephone() throws Exception {
            LivreurCreateRequestDto invalidRequest = new LivreurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "123456",
                    "Moto",
                    "zone-001"
            );

            mockMvc.perform(post("/api/livreurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(livreurService, never()).create(any(LivreurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une création avec nom contenant des caractères invalides")
        void shouldRejectCreateWithInvalidCharactersInNom() throws Exception {
            LivreurCreateRequestDto invalidRequest = new LivreurCreateRequestDto(
                    "Alami123",
                    "Mohammed",
                    "+212612345678",
                    "Moto",
                    "zone-001"
            );

            mockMvc.perform(post("/api/livreurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(livreurService, never()).create(any(LivreurCreateRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Tests de mise à jour")
    class UpdateTests {

        @Test
        @DisplayName("Devrait mettre à jour un livreur avec succès")
        void shouldUpdateLivreurSuccessfully() throws Exception {
            when(livreurService.update(eq("123e4567-e89b-12d3-a456-426614174000"), any(LivreurUpdateRequestDto.class)))
                    .thenReturn(livreurSimple);

            mockMvc.perform(put("/api/livreurs/123e4567-e89b-12d3-a456-426614174000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Livreur mis à jour avec succès"))
                    .andExpect(jsonPath("$.data").exists());

            verify(livreurService, times(1)).update(eq("123e4567-e89b-12d3-a456-426614174000"), any(LivreurUpdateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une mise à jour avec téléphone trop long")
        void shouldRejectUpdateWithTooLongTelephone() throws Exception {
            LivreurUpdateRequestDto invalidRequest = new LivreurUpdateRequestDto();
            invalidRequest.setTelephone("+2126123456789012345678901234567890");

            mockMvc.perform(put("/api/livreurs/123e4567-e89b-12d3-a456-426614174000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(livreurService, never()).update(anyString(), any(LivreurUpdateRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Tests de récupération")
    class GetTests {

        @Test
        @DisplayName("Devrait récupérer un livreur par ID")
        void shouldGetLivreurById() throws Exception {
            when(livreurService.getById("123e4567-e89b-12d3-a456-426614174000"))
                    .thenReturn(livreurSimple);

            mockMvc.perform(get("/api/livreurs/123e4567-e89b-12d3-a456-426614174000"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Livreur récupéré avec succès"))
                    .andExpect(jsonPath("$.data.id").value("123e4567-e89b-12d3-a456-426614174000"))
                    .andExpect(jsonPath("$.data.nom").value("Alami"));

            verify(livreurService, times(1)).getById("123e4567-e89b-12d3-a456-426614174000");
        }

        @Test
        @DisplayName("Devrait récupérer un livreur avec statistiques")
        void shouldGetLivreurWithStats() throws Exception {
            when(livreurService.getByIdWithStats("123e4567-e89b-12d3-a456-426614174000"))
                    .thenReturn(livreurAdvanced);

            mockMvc.perform(get("/api/livreurs/123e4567-e89b-12d3-a456-426614174000/advanced"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Livreur avec statistiques récupéré avec succès"))
                    .andExpect(jsonPath("$.data.nombreColisAssignes").value(50))
                    .andExpect(jsonPath("$.data.nombreColisLives").value(45))
                    .andExpect(jsonPath("$.data.nombreColisEnCours").value(5));

            verify(livreurService, times(1)).getByIdWithStats("123e4567-e89b-12d3-a456-426614174000");
        }

        @Test
        @DisplayName("Devrait récupérer un livreur avec ses colis")
        void shouldGetLivreurWithColis() throws Exception {
            when(livreurService.getByIdWithColis("123e4567-e89b-12d3-a456-426614174000"))
                    .thenReturn(livreurDetailed);

            mockMvc.perform(get("/api/livreurs/123e4567-e89b-12d3-a456-426614174000/detailed"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Livreur avec colis récupéré avec succès"))
                    .andExpect(jsonPath("$.data.colisAssignes").isArray());

            verify(livreurService, times(1)).getByIdWithColis("123e4567-e89b-12d3-a456-426614174000");
        }

        @Test
        @DisplayName("Devrait récupérer tous les livreurs")
        void shouldGetAllLivreurs() throws Exception {
            List<LivreurSimpleResponseDto> livreurs = Arrays.asList(livreurSimple);
            when(livreurService.getAll()).thenReturn(livreurs);

            mockMvc.perform(get("/api/livreurs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Liste des livreurs récupérée avec succès"))
                    .andExpect(jsonPath("$.data", hasSize(1)));

            verify(livreurService, times(1)).getAll();
        }

        @Test
        @DisplayName("Devrait récupérer les livreurs paginés")
        void shouldGetAllLivreursPaginated() throws Exception {
            Pageable pageable = PageRequest.of(0, 10);
            Page<LivreurSimpleResponseDto> page = new PageImpl<>(Arrays.asList(livreurSimple), pageable, 1);
            when(livreurService.getAll(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/api/livreurs/paginated")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Livreurs paginés récupérés avec succès"))
                    .andExpect(jsonPath("$.data.content", hasSize(1)))
                    .andExpect(jsonPath("$.data.totalElements").value(1));

            verify(livreurService, times(1)).getAll(any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("Tests de recherche")
    class SearchTests {

        @Test
        @DisplayName("Devrait récupérer les livreurs par zone")
        void shouldGetLivreursByZone() throws Exception {
            List<LivreurSimpleResponseDto> livreurs = Arrays.asList(livreurSimple);
            when(livreurService.getByZone("zone-001")).thenReturn(livreurs);

            mockMvc.perform(get("/api/livreurs/zone/zone-001"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Livreurs de la zone récupérés avec succès"))
                    .andExpect(jsonPath("$.data", hasSize(1)));

            verify(livreurService, times(1)).getByZone("zone-001");
        }

        @Test
        @DisplayName("Devrait rechercher par nom")
        void shouldSearchByNom() throws Exception {
            List<LivreurSimpleResponseDto> livreurs = Arrays.asList(livreurSimple);
            when(livreurService.searchByNom("Alami")).thenReturn(livreurs);

            mockMvc.perform(get("/api/livreurs/search/nom")
                            .param("nom", "Alami"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Recherche par nom effectuée avec succès"))
                    .andExpect(jsonPath("$.data", hasSize(1)));

            verify(livreurService, times(1)).searchByNom("Alami");
        }

        @Test
        @DisplayName("Devrait rechercher par mot-clé")
        void shouldSearchByKeyword() throws Exception {
            List<LivreurSimpleResponseDto> livreurs = Arrays.asList(livreurSimple);
            when(livreurService.searchByKeyword("Mohammed")).thenReturn(livreurs);

            mockMvc.perform(get("/api/livreurs/search/keyword")
                            .param("keyword", "Mohammed"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Recherche par mot-clé effectuée avec succès"))
                    .andExpect(jsonPath("$.data", hasSize(1)));

            verify(livreurService, times(1)).searchByKeyword("Mohammed");
        }

        @Test
        @DisplayName("Devrait récupérer un livreur par téléphone - trouvé")
        void shouldGetLivreurByTelephone_Found() throws Exception {
            when(livreurService.getByTelephone("+212612345678"))
                    .thenReturn(Optional.of(livreurSimple));

            mockMvc.perform(get("/api/livreurs/telephone/+212612345678"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Livreur trouvé par téléphone"))
                    .andExpect(jsonPath("$.data.telephone").value("+212612345678"));

            verify(livreurService, times(1)).getByTelephone("+212612345678");
        }

        @Test
        @DisplayName("Devrait récupérer un livreur par téléphone - non trouvé")
        void shouldGetLivreurByTelephone_NotFound() throws Exception {
            when(livreurService.getByTelephone("+212600000000"))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get("/api/livreurs/telephone/+212600000000"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Aucun livreur trouvé avec ce téléphone"));

            verify(livreurService, times(1)).getByTelephone("+212600000000");
        }
    }

    @Nested
    @DisplayName("Tests de comptage")
    class CountTests {

        @Test
        @DisplayName("Devrait compter les livreurs par zone")
        void shouldCountLivreursByZone() throws Exception {
            when(livreurService.countByZone("zone-001")).thenReturn(5L);

            mockMvc.perform(get("/api/livreurs/zone/zone-001/count"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Nombre de livreurs dans la zone récupéré avec succès"))
                    .andExpect(jsonPath("$.data").value(5));

            verify(livreurService, times(1)).countByZone("zone-001");
        }
    }

    @Nested
    @DisplayName("Tests de suppression")
    class DeleteTests {

        @Test
        @DisplayName("Devrait supprimer un livreur avec succès")
        void shouldDeleteLivreurSuccessfully() throws Exception {
            doNothing().when(livreurService).delete("123e4567-e89b-12d3-a456-426614174000");

            mockMvc.perform(delete("/api/livreurs/123e4567-e89b-12d3-a456-426614174000"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Livreur supprimé avec succès"));

            verify(livreurService, times(1)).delete("123e4567-e89b-12d3-a456-426614174000");
        }
    }

    @Nested
    @DisplayName("Tests d'existence")
    class ExistsTests {

        @Test
        @DisplayName("Devrait vérifier l'existence d'un livreur - existe")
        void shouldCheckExistenceById_Exists() throws Exception {
            when(livreurService.existsById("123e4567-e89b-12d3-a456-426614174000")).thenReturn(true);

            mockMvc.perform(get("/api/livreurs/123e4567-e89b-12d3-a456-426614174000/exists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Le livreur existe"))
                    .andExpect(jsonPath("$.data").value(true));

            verify(livreurService, times(1)).existsById("123e4567-e89b-12d3-a456-426614174000");
        }

        @Test
        @DisplayName("Devrait vérifier l'existence d'un livreur - n'existe pas")
        void shouldCheckExistenceById_NotExists() throws Exception {
            when(livreurService.existsById("unknown-id")).thenReturn(false);

            mockMvc.perform(get("/api/livreurs/unknown-id/exists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Le livreur n'existe pas"))
                    .andExpect(jsonPath("$.data").value(false));

            verify(livreurService, times(1)).existsById("unknown-id");
        }

        @Test
        @DisplayName("Devrait vérifier l'existence d'un téléphone - existe")
        void shouldCheckExistenceByTelephone_Exists() throws Exception {
            when(livreurService.existsByTelephone("+212612345678")).thenReturn(true);

            mockMvc.perform(get("/api/livreurs/telephone/+212612345678/exists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Un livreur existe avec ce téléphone"))
                    .andExpect(jsonPath("$.data").value(true));

            verify(livreurService, times(1)).existsByTelephone("+212612345678");
        }

        @Test
        @DisplayName("Devrait vérifier l'existence d'un téléphone - n'existe pas")
        void shouldCheckExistenceByTelephone_NotExists() throws Exception {
            when(livreurService.existsByTelephone("+212600000000")).thenReturn(false);

            mockMvc.perform(get("/api/livreurs/telephone/+212600000000/exists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Aucun livreur avec ce téléphone"))
                    .andExpect(jsonPath("$.data").value(false));

            verify(livreurService, times(1)).existsByTelephone("+212600000000");
        }
    }

    @Nested
    @DisplayName("Tests de validation des données")
    class ValidationTests {

        @Test
        @DisplayName("Devrait rejeter un nom dépassant 100 caractères")
        void shouldRejectNomExceedingMaxLength() throws Exception {
            LivreurCreateRequestDto invalidRequest = new LivreurCreateRequestDto(
                    "A".repeat(101),
                    "Mohammed",
                    "+212612345678",
                    "Moto",
                    "zone-001"
            );

            mockMvc.perform(post("/api/livreurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(livreurService, never()).create(any(LivreurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait accepter un téléphone marocain valide avec +212")
        void shouldAcceptValidMoroccanPhoneWithCountryCode() throws Exception {
            LivreurCreateRequestDto validRequest = new LivreurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "+212612345678",
                    "Moto",
                    "zone-001"
            );

            when(livreurService.create(any(LivreurCreateRequestDto.class)))
                    .thenReturn(livreurSimple);

            mockMvc.perform(post("/api/livreurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated());

            verify(livreurService, times(1)).create(any(LivreurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait accepter un téléphone marocain valide avec 0")
        void shouldAcceptValidMoroccanPhoneWithZero() throws Exception {
            LivreurCreateRequestDto validRequest = new LivreurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "0612345678",
                    "Moto",
                    "zone-001"
            );

            when(livreurService.create(any(LivreurCreateRequestDto.class)))
                    .thenReturn(livreurSimple);

            mockMvc.perform(post("/api/livreurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated());

            verify(livreurService, times(1)).create(any(LivreurCreateRequestDto.class));
        }
    }
}
