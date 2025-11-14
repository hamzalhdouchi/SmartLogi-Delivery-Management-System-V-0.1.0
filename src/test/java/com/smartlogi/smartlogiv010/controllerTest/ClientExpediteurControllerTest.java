package com.smartlogi.smartlogiv010.controllerTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartlogi.smartlogiv010.controller.ClientExpediteurController;
import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ClientExpediteurCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ClientExpediteurUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.ClientExpediteur.ClientExpediteurSimpleResponseDto;
import com.smartlogi.smartlogiv010.service.clientExpsditeurInterfaceImpl;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientExpediteurController.class)
@DisplayName("Tests du contrôleur Client Expéditeur")
class ClientExpediteurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private clientExpsditeurInterfaceImpl clientExpediteurService;

    private ClientExpediteurSimpleResponseDto clientResponse;
    private ClientExpediteurCreateRequestDto createRequest;
    private ClientExpediteurUpdateRequestDto updateRequest;
    private LocalDateTime dateCreation;

    @BeforeEach
    void setUp() {
        dateCreation = LocalDateTime.now();

        clientResponse = new ClientExpediteurSimpleResponseDto(
                "client-123e4567-e89b-12d3-a456-426614174000",
                "Alami",
                "Mohammed",
                "mohammed.alami@gmail.com",
                "+212612345678",
                "123 Avenue Mohammed V, Casablanca",
                dateCreation
        );

        createRequest = new ClientExpediteurCreateRequestDto(
                "Alami",
                "Mohammed",
                "mohammed.alami@gmail.com",
                "+212612345678",
                "123 Avenue Mohammed V, Casablanca"
        );

        updateRequest = new ClientExpediteurUpdateRequestDto(
                "client-123e4567-e89b-12d3-a456-426614174000",
                "Alami",
                "Ahmed",
                "ahmed.alami@gmail.com",
                "+212698765432",
                "456 Rue Hassan II, Rabat",
                dateCreation
        );
    }

    @Nested
    @DisplayName("Tests de création")
    class CreateTests {

        @Test
        @DisplayName("Devrait créer un client expéditeur avec succès")
        void shouldCreateClientExpediteurSuccessfully() throws Exception {
            when(clientExpediteurService.create(any(ClientExpediteurCreateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Client expéditeur créé avec succès"))
                    .andExpect(jsonPath("$.data.id").value(clientResponse.getId()))
                    .andExpect(jsonPath("$.data.nom").value("Alami"))
                    .andExpect(jsonPath("$.data.prenom").value("Mohammed"))
                    .andExpect(jsonPath("$.data.email").value("mohammed.alami@gmail.com"))
                    .andExpect(jsonPath("$.data.telephone").value("+212612345678"))
                    .andExpect(jsonPath("$.data.adresse").value("123 Avenue Mohammed V, Casablanca"));

            verify(clientExpediteurService, times(1)).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une création avec nom vide")
        void shouldRejectCreateWithBlankNom() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "",
                    "Mohammed",
                    "mohammed.alami@gmail.com",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une création avec email invalide")
        void shouldRejectCreateWithInvalidEmail() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "invalid-email",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une création avec téléphone invalide")
        void shouldRejectCreateWithInvalidTelephone() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed.alami@gmail.com",
                    "123456",
                    "123 Avenue Mohammed V, Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une création avec nom contenant des chiffres")
        void shouldRejectCreateWithNumericNom() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "Alami123",
                    "Mohammed",
                    "mohammed.alami@gmail.com",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une création avec adresse contenant des caractères interdits")
        void shouldRejectCreateWithInvalidAddressCharacters() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed.alami@gmail.com",
                    "+212612345678",
                    "123 Avenue @ Mohammed V $ Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait accepter une adresse valide avec caractères spéciaux autorisés")
        void shouldAcceptValidAddressWithAllowedSpecialCharacters() throws Exception {
            ClientExpediteurCreateRequestDto validRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed.alami@gmail.com",
                    "+212612345678",
                    "123 Avenue Mohammed V, Apt #5, Casablanca & Région"
            );

            when(clientExpediteurService.create(any(ClientExpediteurCreateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true));

            verify(clientExpediteurService, times(1)).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une création avec nom dépassant 100 caractères")
        void shouldRejectCreateWithTooLongNom() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "A".repeat(101),
                    "Mohammed",
                    "mohammed.alami@gmail.com",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait accepter un nom avec accents et apostrophes")
        void shouldAcceptNomWithAccentsAndApostrophes() throws Exception {
            ClientExpediteurCreateRequestDto validRequest = new ClientExpediteurCreateRequestDto(
                    "O'Malley-François",
                    "Jean-André",
                    "jean.andre@gmail.com",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            when(clientExpediteurService.create(any(ClientExpediteurCreateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true));

            verify(clientExpediteurService, times(1)).create(any(ClientExpediteurCreateRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Tests de mise à jour")
    class UpdateTests {

        @Test
        @DisplayName("Devrait mettre à jour un client expéditeur avec succès")
        void shouldUpdateClientExpediteurSuccessfully() throws Exception {
            when(clientExpediteurService.update(eq("client-123e4567-e89b-12d3-a456-426614174000"), any(ClientExpediteurUpdateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(put("/api/clients-expediteurs/client-123e4567-e89b-12d3-a456-426614174000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Client expéditeur mis à jour avec succès"))
                    .andExpect(jsonPath("$.data").exists());

            verify(clientExpediteurService, times(1)).update(eq("client-123e4567-e89b-12d3-a456-426614174000"), any(ClientExpediteurUpdateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter une mise à jour avec email invalide")
        void shouldRejectUpdateWithInvalidEmail() throws Exception {
            ClientExpediteurUpdateRequestDto invalidRequest = new ClientExpediteurUpdateRequestDto();
            invalidRequest.setId("client-123e4567-e89b-12d3-a456-426614174000");
            invalidRequest.setEmail("invalid-email-format");

            mockMvc.perform(put("/api/clients-expediteurs/client-123e4567-e89b-12d3-a456-426614174000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).update(anyString(), any(ClientExpediteurUpdateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait accepter une mise à jour partielle")
        void shouldAcceptPartialUpdate() throws Exception {
            ClientExpediteurUpdateRequestDto partialRequest = new ClientExpediteurUpdateRequestDto();
            partialRequest.setId("client-123e4567-e89b-12d3-a456-426614174000");
            partialRequest.setTelephone("+212698765432");

            when(clientExpediteurService.update(eq("client-123e4567-e89b-12d3-a456-426614174000"), any(ClientExpediteurUpdateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(put("/api/clients-expediteurs/client-123e4567-e89b-12d3-a456-426614174000")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(partialRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));

            verify(clientExpediteurService, times(1)).update(eq("client-123e4567-e89b-12d3-a456-426614174000"), any(ClientExpediteurUpdateRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Tests de récupération")
    class GetTests {

        @Test
        @DisplayName("Devrait récupérer un client expéditeur par ID")
        void shouldGetClientExpediteurById() throws Exception {
            when(clientExpediteurService.getById("client-123e4567-e89b-12d3-a456-426614174000"))
                    .thenReturn(clientResponse);

            mockMvc.perform(get("/api/clients-expediteurs/client-123e4567-e89b-12d3-a456-426614174000"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Client expéditeur récupéré avec succès"))
                    .andExpect(jsonPath("$.data.id").value("client-123e4567-e89b-12d3-a456-426614174000"))
                    .andExpect(jsonPath("$.data.nom").value("Alami"))
                    .andExpect(jsonPath("$.data.email").value("mohammed.alami@gmail.com"));

            verify(clientExpediteurService, times(1)).getById("client-123e4567-e89b-12d3-a456-426614174000");
        }

        @Test
        @DisplayName("Devrait récupérer tous les clients expéditeurs paginés")
        void shouldGetAllClientExpediteursPaginated() throws Exception {
            Pageable pageable = PageRequest.of(0, 10);
            Page<ClientExpediteurSimpleResponseDto> page = new PageImpl<>(
                    Arrays.asList(clientResponse),
                    pageable,
                    1
            );
            when(clientExpediteurService.getAll(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/api/clients-expediteurs")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Liste des clients expéditeurs récupérée avec succès"))
                    .andExpect(jsonPath("$.data.content", hasSize(1)))
                    .andExpect(jsonPath("$.data.totalElements").value(1));

            verify(clientExpediteurService, times(1)).getAll(any(Pageable.class));
        }

        @Test
        @DisplayName("Devrait récupérer les clients avec tri par nom")
        void shouldGetClientsWithSortByNom() throws Exception {
            Pageable pageable = PageRequest.of(0, 10);
            Page<ClientExpediteurSimpleResponseDto> page = new PageImpl<>(
                    Arrays.asList(clientResponse),
                    pageable,
                    1
            );
            when(clientExpediteurService.getAll(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/api/clients-expediteurs")
                            .param("page", "0")
                            .param("size", "10")
                            .param("sort", "nom,asc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));

            verify(clientExpediteurService, times(1)).getAll(any(Pageable.class));
        }
    }

    @Nested
    @DisplayName("Tests de recherche")
    class SearchTests {

        @Test
        @DisplayName("Devrait rechercher par nom")
        void shouldSearchByNom() throws Exception {
            List<ClientExpediteurSimpleResponseDto> clients = Arrays.asList(clientResponse);
            when(clientExpediteurService.searchByNom("Alami")).thenReturn(clients);

            mockMvc.perform(get("/api/clients-expediteurs/search")
                            .param("nom", "Alami"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Recherche de clients expéditeurs effectuée avec succès"))
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andExpect(jsonPath("$.data[0].nom").value("Alami"));

            verify(clientExpediteurService, times(1)).searchByNom("Alami");
        }

        @Test
        @DisplayName("Devrait rechercher par nom partiel")
        void shouldSearchByPartialNom() throws Exception {
            List<ClientExpediteurSimpleResponseDto> clients = Arrays.asList(clientResponse);
            when(clientExpediteurService.searchByNom("Ala")).thenReturn(clients);

            mockMvc.perform(get("/api/clients-expediteurs/search")
                            .param("nom", "Ala"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data", hasSize(1)));

            verify(clientExpediteurService, times(1)).searchByNom("Ala");
        }

        @Test
        @DisplayName("Devrait rechercher par mot-clé")
        void shouldFindByKeyword() throws Exception {
            when(clientExpediteurService.findByKeyWord("mohammed.alami@gmail.com"))
                    .thenReturn(clientResponse);

            mockMvc.perform(get("/api/clients-expediteurs/search-keyword")
                            .param("keyword", "mohammed.alami@gmail.com"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Client expéditeur trouvé avec succès"))
                    .andExpect(jsonPath("$.data.email").value("mohammed.alami@gmail.com"));

            verify(clientExpediteurService, times(1)).findByKeyWord("mohammed.alami@gmail.com");
        }

        @Test
        @DisplayName("Devrait rechercher par téléphone comme mot-clé")
        void shouldFindByKeywordWithTelephone() throws Exception {
            when(clientExpediteurService.findByKeyWord("+212612345678"))
                    .thenReturn(clientResponse);

            mockMvc.perform(get("/api/clients-expediteurs/search-keyword")
                            .param("keyword", "+212612345678"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.telephone").value("+212612345678"));

            verify(clientExpediteurService, times(1)).findByKeyWord("+212612345678");
        }

        @Test
        @DisplayName("Devrait retourner une liste vide pour une recherche sans résultat")
        void shouldReturnEmptyListForNoMatchingSearch() throws Exception {
            when(clientExpediteurService.searchByNom("NonExistent"))
                    .thenReturn(Arrays.asList());

            mockMvc.perform(get("/api/clients-expediteurs/search")
                            .param("nom", "NonExistent"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data", hasSize(0)));

            verify(clientExpediteurService, times(1)).searchByNom("NonExistent");
        }
    }

    @Nested
    @DisplayName("Tests de suppression")
    class DeleteTests {

        @Test
        @DisplayName("Devrait supprimer un client expéditeur avec succès")
        void shouldDeleteClientExpediteurSuccessfully() throws Exception {
            doNothing().when(clientExpediteurService).delete("client-123e4567-e89b-12d3-a456-426614174000");

            mockMvc.perform(delete("/api/clients-expediteurs/client-123e4567-e89b-12d3-a456-426614174000"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Client expéditeur supprimé avec succès"));

            verify(clientExpediteurService, times(1)).delete("client-123e4567-e89b-12d3-a456-426614174000");
        }
    }

    @Nested
    @DisplayName("Tests d'existence")
    class ExistsTests {

        @Test
        @DisplayName("Devrait vérifier l'existence d'un client - existe")
        void shouldCheckExistence_Exists() throws Exception {
            when(clientExpediteurService.existsById("client-123e4567-e89b-12d3-a456-426614174000"))
                    .thenReturn(true);

            mockMvc.perform(get("/api/clients-expediteurs/client-123e4567-e89b-12d3-a456-426614174000/exists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Le client expéditeur existe"))
                    .andExpect(jsonPath("$.data").value(true));

            verify(clientExpediteurService, times(1)).existsById("client-123e4567-e89b-12d3-a456-426614174000");
        }

        @Test
        @DisplayName("Devrait vérifier l'existence d'un client - n'existe pas")
        void shouldCheckExistence_NotExists() throws Exception {
            when(clientExpediteurService.existsById("unknown-id"))
                    .thenReturn(false);

            mockMvc.perform(get("/api/clients-expediteurs/unknown-id/exists"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Le client expéditeur n'existe pas"))
                    .andExpect(jsonPath("$.data").value(false));

            verify(clientExpediteurService, times(1)).existsById("unknown-id");
        }
    }

    @Nested
    @DisplayName("Tests de validation des emails")
    class EmailValidationTests {

        @Test
        @DisplayName("Devrait accepter un email valide standard")
        void shouldAcceptValidStandardEmail() throws Exception {
            ClientExpediteurCreateRequestDto validRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed@example.com",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            when(clientExpediteurService.create(any(ClientExpediteurCreateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated());

            verify(clientExpediteurService, times(1)).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait accepter un email avec sous-domaines")
        void shouldAcceptEmailWithSubdomains() throws Exception {
            ClientExpediteurCreateRequestDto validRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed@mail.company.com",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            when(clientExpediteurService.create(any(ClientExpediteurCreateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated());

            verify(clientExpediteurService, times(1)).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter un email sans @")
        void shouldRejectEmailWithoutAtSign() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammedexample.com",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter un email sans domaine")
        void shouldRejectEmailWithoutDomain() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed@",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Tests de validation des téléphones")
    class TelephoneValidationTests {

        @Test
        @DisplayName("Devrait accepter un téléphone marocain avec +212")
        void shouldAcceptMoroccanPhoneWithCountryCode() throws Exception {
            ClientExpediteurCreateRequestDto validRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed@gmail.com",
                    "+212612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            when(clientExpediteurService.create(any(ClientExpediteurCreateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated());

            verify(clientExpediteurService, times(1)).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait accepter un téléphone marocain avec 0")
        void shouldAcceptMoroccanPhoneWithZero() throws Exception {
            ClientExpediteurCreateRequestDto validRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed@gmail.com",
                    "0612345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            when(clientExpediteurService.create(any(ClientExpediteurCreateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated());

            verify(clientExpediteurService, times(1)).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait accepter tous les opérateurs marocains (05, 06, 07)")
        void shouldAcceptAllMoroccanOperators() throws Exception {
            String[] validPhones = {"+212512345678", "+212612345678", "+212712345678"};

            for (String phone : validPhones) {
                ClientExpediteurCreateRequestDto validRequest = new ClientExpediteurCreateRequestDto(
                        "Alami",
                        "Mohammed",
                        "mohammed@gmail.com",
                        phone,
                        "123 Avenue Mohammed V, Casablanca"
                );

                when(clientExpediteurService.create(any(ClientExpediteurCreateRequestDto.class)))
                        .thenReturn(clientResponse);

                mockMvc.perform(post("/api/clients-expediteurs")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(validRequest)))
                        .andExpect(status().isCreated());
            }

            verify(clientExpediteurService, times(3)).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter un téléphone avec préfixe invalide")
        void shouldRejectPhoneWithInvalidPrefix() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed@gmail.com",
                    "+212412345678",
                    "123 Avenue Mohammed V, Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }

        @Test
        @DisplayName("Devrait rejeter un téléphone trop court")
        void shouldRejectTooShortPhone() throws Exception {
            ClientExpediteurCreateRequestDto invalidRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed@gmail.com",
                    "+21261234",
                    "123 Avenue Mohammed V, Casablanca"
            );

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(clientExpediteurService, never()).create(any(ClientExpediteurCreateRequestDto.class));
        }
    }

    @Nested
    @DisplayName("Tests de cas limites")
    class EdgeCaseTests {

        @Test
        @DisplayName("Devrait gérer une page vide")
        void shouldHandleEmptyPage() throws Exception {
            Pageable pageable = PageRequest.of(0, 10);
            Page<ClientExpediteurSimpleResponseDto> emptyPage = new PageImpl<>(
                    Arrays.asList(),
                    pageable,
                    0
            );
            when(clientExpediteurService.getAll(any(Pageable.class))).thenReturn(emptyPage);

            mockMvc.perform(get("/api/clients-expediteurs")
                            .param("page", "0")
                            .param("size", "10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.content", hasSize(0)))
                    .andExpect(jsonPath("$.data.totalElements").value(0));

            verify(clientExpediteurService, times(1)).getAll(any(Pageable.class));
        }

        @Test
        @DisplayName("Devrait gérer une pagination avec grande taille de page")
        void shouldHandleLargePageSize() throws Exception {
            Pageable pageable = PageRequest.of(0, 100);
            Page<ClientExpediteurSimpleResponseDto> page = new PageImpl<>(
                    Arrays.asList(clientResponse),
                    pageable,
                    1
            );
            when(clientExpediteurService.getAll(any(Pageable.class))).thenReturn(page);

            mockMvc.perform(get("/api/clients-expediteurs")
                            .param("page", "0")
                            .param("size", "100"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true));

            verify(clientExpediteurService, times(1)).getAll(any(Pageable.class));
        }

        @Test
        @DisplayName("Devrait gérer une adresse à la longueur maximale")
        void shouldHandleMaxLengthAddress() throws Exception {
            String maxLengthAddress = "A".repeat(255);
            ClientExpediteurCreateRequestDto validRequest = new ClientExpediteurCreateRequestDto(
                    "Alami",
                    "Mohammed",
                    "mohammed@gmail.com",
                    "+212612345678",
                    maxLengthAddress
            );

            when(clientExpediteurService.create(any(ClientExpediteurCreateRequestDto.class)))
                    .thenReturn(clientResponse);

            mockMvc.perform(post("/api/clients-expediteurs")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRequest)))
                    .andExpect(status().isCreated());

            verify(clientExpediteurService, times(1)).create(any(ClientExpediteurCreateRequestDto.class));
        }
    }
}

