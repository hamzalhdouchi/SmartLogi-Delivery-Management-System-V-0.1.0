package com.smartlogi.smartlogiv010.serviceTest;

import com.smartlogi.security.exception.DuplicateResourceException;
import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ColisCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ProduitCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ColisUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisSimpleResponseDto;
import com.smartlogi.smartlogiv010.entity.*;
import com.smartlogi.smartlogiv010.enums.Priorite;
import com.smartlogi.smartlogiv010.enums.StatutColis;
import com.smartlogi.smartlogiv010.exception.ArgementNotFoundExption;
import com.smartlogi.smartlogiv010.exception.BusinessException;
import com.smartlogi.smartlogiv010.exception.ResourceNotFoundException;
import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
import com.smartlogi.smartlogiv010.repository.*;
import com.smartlogi.smartlogiv010.service.ColisServiceImpl;
import com.smartlogi.smartlogiv010.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires pour ColisServiceImpl")
class ColisServiceImplTest {

    @Mock
    private ColisRepository colisRepository;

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private ColisProduitRepository colisProduitRepository;

    @Mock
    private ClientExpediteurRepository clientExpediteurRepository;

    @Mock
    private DestinataireRepository destinataireRepository;

    @Mock
    private LivreurRepository livreurRepository;

    @Mock
    private ZoneRepository zoneRepository;

    @Mock
    private HistoriqueLivraisonRepository historiqueLivraisonRepository;

    @Mock
    private SmartLogiMapper smartLogiMapper;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ColisServiceImpl colisService;

    private Colis colis;
    private ColisCreateRequestDto colisCreateRequestDto;
    private ColisSimpleResponseDto colisSimpleResponseDto;
    private ClientExpediteur clientExpediteur;
    private Destinataire destinataire;
    private Livreur livreur;
    private Zone zone;
    private Produit produit;

    @BeforeEach
    void setUp() {
        // Initialisation des objets de test
        clientExpediteur = new ClientExpediteur();
        clientExpediteur.setId("client-1");
        clientExpediteur.setNom("Client Test");

        destinataire = new Destinataire();
        destinataire.setId("dest-1");
        destinataire.setNom("Destinataire Test");

        livreur = new Livreur();
        livreur.setId("livreur-1");
        livreur.setNom("Livreur");
        livreur.setPrenom("Test");

        zone = new Zone();
        zone.setId("zone-1");
        zone.setNom("Zone Test");

        colis = new Colis();
        colis.setId("colis-1");
        colis.setStatut(StatutColis.CREE);
        colis.setClientExpediteur(clientExpediteur);
        colis.setDestinataire(destinataire);
        colis.setDateCreation(LocalDateTime.now());

        produit = new Produit();
        produit.setId("produit-1");
        produit.setNom("Produit Test");
        produit.setPrix(BigDecimal.valueOf(100));
        produit.setPoids(BigDecimal.valueOf(2.5));

        colisSimpleResponseDto = new ColisSimpleResponseDto();
        colisSimpleResponseDto.setId("colis-1");

        colisCreateRequestDto = new ColisCreateRequestDto();
        colisCreateRequestDto.setClientExpediteurId("client-1");
        colisCreateRequestDto.setDestinataireId("dest-1");
    }

    // ==================== Tests pour la méthode create ====================

    @Test
    @DisplayName("Devrait créer un colis avec succès")
    void testCreate_Success() {
        // Given
        when(clientExpediteurRepository.findById("client-1")).thenReturn(Optional.of(clientExpediteur));
        when(destinataireRepository.findById("dest-1")).thenReturn(Optional.of(destinataire));
        when(smartLogiMapper.toEntity(colisCreateRequestDto)).thenReturn(colis);
        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
        when(smartLogiMapper.toSimpleResponseDto(colis)).thenReturn(colisSimpleResponseDto);

        // Correction : utiliser doNothing() seulement pour les méthodes void
        doNothing().when(notificationService).envoyerNotificationCollecte(any(), any());

        // Pour historiqueLivraisonRepository.save(), utiliser when().thenReturn() car elle retourne un objet
        when(historiqueLivraisonRepository.save(any(HistoriqueLivraison.class)))
                .thenReturn(new HistoriqueLivraison());

        // When
        ColisSimpleResponseDto result = colisService.create(colisCreateRequestDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("colis-1");
        verify(colisRepository, times(1)).save(any(Colis.class));
        verify(notificationService, times(1)).envoyerNotificationCollecte(clientExpediteur, colis);
        verify(historiqueLivraisonRepository, times(1)).save(any(HistoriqueLivraison.class));
    }


    @Test
    @DisplayName("Devrait lancer ResourceNotFoundException si client non trouvé")
    void testCreate_ClientNotFound() {
        // Given
        when(clientExpediteurRepository.findById("client-1")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> colisService.create(colisCreateRequestDto));
        verify(colisRepository, never()).save(any(Colis.class));
    }

    @Test
    @DisplayName("Devrait lancer ResourceNotFoundException si destinataire non trouvé")
    void testCreate_DestinataireNotFound() {
        // Given
        when(clientExpediteurRepository.findById("client-1")).thenReturn(Optional.of(clientExpediteur));
        when(destinataireRepository.findById("dest-1")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> colisService.create(colisCreateRequestDto));
        verify(colisRepository, never()).save(any(Colis.class));
    }

    @Test
    @DisplayName("Devrait créer un colis avec livreur assigné")
    void testCreate_WithLivreur() {
        // Given
        colisCreateRequestDto.setLivreurId("livreur-1");
        when(clientExpediteurRepository.findById("client-1")).thenReturn(Optional.of(clientExpediteur));
        when(destinataireRepository.findById("dest-1")).thenReturn(Optional.of(destinataire));
        when(livreurRepository.findById("livreur-1")).thenReturn(Optional.of(livreur));
        when(smartLogiMapper.toEntity(colisCreateRequestDto)).thenReturn(colis);
        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
        when(smartLogiMapper.toSimpleResponseDto(colis)).thenReturn(colisSimpleResponseDto);

        // When
        ColisSimpleResponseDto result = colisService.create(colisCreateRequestDto);

        // Then
        assertThat(result).isNotNull();
        verify(livreurRepository, times(1)).findById("livreur-1");
    }

    // ==================== Tests pour la méthode ajouterProduit ====================

    @Test
    @DisplayName("Devrait ajouter un produit existant au colis")
    void testAjouterProduit_ExistingProduct() {
        // Given
        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
        produitDto.setProduitId("produit-1");
        produitDto.setQuantite(3);

        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        when(colisProduitRepository.save(any(ColisProduit.class))).thenReturn(new ColisProduit());

        // When
        colisService.ajouterProduit("colis-1", produitDto);

        // Then
        verify(colisProduitRepository, times(1)).save(any(ColisProduit.class));
        verify(historiqueLivraisonRepository, times(1)).save(any(HistoriqueLivraison.class));
    }

    @Test
    @DisplayName("Devrait créer et ajouter un nouveau produit au colis")
    void testAjouterProduit_NewProduct() {
        // Given
        ProduitCreateRequestDto nouveauProduitDto = new ProduitCreateRequestDto();
        nouveauProduitDto.setNom("Nouveau Produit");
        nouveauProduitDto.setPrix(BigDecimal.valueOf(150));
        nouveauProduitDto.setPoids(BigDecimal.valueOf(3.00));

        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
        produitDto.setNouveauProduit(nouveauProduitDto);
        produitDto.setQuantite(2);

        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        when(produitRepository.existsByNomContainingIgnoreCase("Nouveau Produit")).thenReturn(false);
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        when(colisProduitRepository.save(any(ColisProduit.class))).thenReturn(new ColisProduit());

        // When
        colisService.ajouterProduit("colis-1", produitDto);

        // Then
        verify(produitRepository, times(1)).save(any(Produit.class));
        verify(colisProduitRepository, times(1)).save(any(ColisProduit.class));
    }

    @Test
    @DisplayName("Devrait lancer BusinessException si produitId et nouveauProduit sont fournis")
    void testAjouterProduit_BothIdAndNew() {
        // Given
        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
        produitDto.setProduitId("produit-1");
        produitDto.setNouveauProduit(new ProduitCreateRequestDto());
        produitDto.setQuantite(1);

        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));

        // When & Then
        assertThrows(BusinessException.class, () -> colisService.ajouterProduit("colis-1", produitDto));
    }

    @Test
    @DisplayName("Devrait lancer DuplicateResourceException si produit existe déjà")
    void testAjouterProduit_DuplicateProduct() {
        // Given
        ProduitCreateRequestDto nouveauProduitDto = new ProduitCreateRequestDto();
        nouveauProduitDto.setNom("Produit Existant");
        nouveauProduitDto.setPrix(BigDecimal.valueOf(100));

        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
        produitDto.setNouveauProduit(nouveauProduitDto);
        produitDto.setQuantite(1);

        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        when(produitRepository.existsByNomContainingIgnoreCase("Produit Existant")).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> colisService.ajouterProduit("colis-1", produitDto));
    }

    // ==================== Tests pour la méthode supprimerProduit ====================

    @Test
    @DisplayName("Devrait supprimer un produit du colis")
    void testSupprimerProduit_Success() {
        // Given
        ColisProduitId colisProduitId = new ColisProduitId("colis-1", "produit-1");
        when(colisProduitRepository.existsById(colisProduitId)).thenReturn(true);
        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        doNothing().when(colisProduitRepository).deleteById(colisProduitId);

        // When
        colisService.supprimerProduit("colis-1", "produit-1");

        // Then
        verify(colisProduitRepository, times(1)).deleteById(colisProduitId);
        verify(historiqueLivraisonRepository, times(1)).save(any(HistoriqueLivraison.class));
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si produit non trouvé dans le colis")
    void testSupprimerProduit_NotFound() {
        // Given
        ColisProduitId colisProduitId = new ColisProduitId("colis-1", "produit-1");
        when(colisProduitRepository.existsById(colisProduitId)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> colisService.supprimerProduit("colis-1", "produit-1"));
        verify(colisProduitRepository, never()).deleteById(any());
    }

    // ==================== Tests pour la méthode mettreAJourQuantiteProduit ====================

    @Test
    @DisplayName("Devrait mettre à jour la quantité d'un produit")
    void testMettreAJourQuantiteProduit_Success() {
        // Given
        ColisProduitId colisProduitId = new ColisProduitId("colis-1", "produit-1");
        ColisProduit colisProduit = new ColisProduit();
        colisProduit.setId(colisProduitId);
        colisProduit.setProduit(produit);
        colisProduit.setQuantite(5);

        when(colisProduitRepository.findById(colisProduitId)).thenReturn(Optional.of(colisProduit));
        when(colisProduitRepository.save(any(ColisProduit.class))).thenReturn(colisProduit);
        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));

        // When
        colisService.mettreAJourQuantiteProduit("colis-1", "produit-1", 10);

        // Then
        ArgumentCaptor<ColisProduit> captor = ArgumentCaptor.forClass(ColisProduit.class);
        verify(colisProduitRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getQuantite()).isEqualTo(10);
        assertThat(captor.getValue().getPrix()).isEqualTo(BigDecimal.valueOf(1000));
    }

    @Test
    @DisplayName("Devrait lancer BusinessException si quantité <= 0")
    void testMettreAJourQuantiteProduit_InvalidQuantity() {
        // Given
        ColisProduitId colisProduitId = new ColisProduitId("colis-1", "produit-1");
        ColisProduit colisProduit = new ColisProduit();
        colisProduit.setId(colisProduitId);

        when(colisProduitRepository.findById(colisProduitId)).thenReturn(Optional.of(colisProduit));

        // When & Then
        assertThrows(BusinessException.class, () ->
                colisService.mettreAJourQuantiteProduit("colis-1", "produit-1", 0));
    }

    // ==================== Tests pour la méthode update ====================

    @Test
    @DisplayName("Devrait mettre à jour un colis")
    void testUpdate_Success() {
        // Given
        ColisUpdateRequestDto updateDto = new ColisUpdateRequestDto();
        updateDto.setLivreurId("livreur-1");
        updateDto.setZoneId("zone-1");

        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        when(livreurRepository.findById("livreur-1")).thenReturn(Optional.of(livreur));
        when(zoneRepository.findById("zone-1")).thenReturn(Optional.of(zone));
        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
        when(smartLogiMapper.toSimpleResponseDto(colis)).thenReturn(colisSimpleResponseDto);
        doNothing().when(smartLogiMapper).updateEntityFromDto(updateDto, colis);

        // When
        ColisSimpleResponseDto result = colisService.update("colis-1", updateDto);

        // Then
        assertThat(result).isNotNull();
        verify(colisRepository, times(1)).save(colis);
    }

    @Test
    @DisplayName("Devrait lancer ArgementNotFoundExption si colis non trouvé")
    void testUpdate_ColisNotFound() {
        // Given
        ColisUpdateRequestDto updateDto = new ColisUpdateRequestDto();
        when(colisRepository.findById("colis-1")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ArgementNotFoundExption.class, () -> colisService.update("colis-1", updateDto));
    }

    // ==================== Tests pour la méthode getById ====================

    @Test
    @DisplayName("Devrait récupérer un colis par ID")
    void testGetById_Success() {
        // Given
        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        when(smartLogiMapper.toSimpleResponseDto(colis)).thenReturn(colisSimpleResponseDto);

        // When
        ColisSimpleResponseDto result = colisService.getById("colis-1");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("colis-1");
    }

    @Test
    @DisplayName("Devrait lancer ResourceNotFoundException si colis non trouvé")
    void testGetById_NotFound() {
        // Given
        when(colisRepository.findById("colis-1")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> colisService.getById("colis-1"));
    }

    // ==================== Tests pour la méthode getAll ====================

    @Test
    @DisplayName("Devrait récupérer tous les colis paginés")
    void testGetAll_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Colis> colisList = Arrays.asList(colis);
        Page<Colis> colisPage = new PageImpl<>(colisList, pageable, 1);

        when(colisRepository.findAll(pageable)).thenReturn(colisPage);
        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(colisSimpleResponseDto);

        // When
        Page<ColisSimpleResponseDto> result = colisService.getAll(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    // ==================== Tests pour la méthode assignerLivreur ====================

    @Test
    @DisplayName("Devrait assigner un livreur à un colis")
    void testAssignerLivreur_Success() {
        // Given
        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        when(livreurRepository.findById("livreur-1")).thenReturn(Optional.of(livreur));
        when(colisRepository.save(any(Colis.class))).thenReturn(colis);

        // When
        colisService.assignerLivreur("colis-1", "livreur-1");

        // Then
        verify(colisRepository, times(1)).save(colis);
        verify(historiqueLivraisonRepository, times(1)).save(any(HistoriqueLivraison.class));
    }

    // ==================== Tests pour la méthode changerStatut ====================

    @Test
    @DisplayName("Devrait changer le statut d'un colis")
    void testChangerStatut_Success() {
        // Given
        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        when(colisRepository.save(any(Colis.class))).thenReturn(colis);

        // When
        colisService.changerStatut("colis-1", StatutColis.EN_TRANSIT, "En cours de livraison");

        // Then
        ArgumentCaptor<Colis> colisCaptor = ArgumentCaptor.forClass(Colis.class);
        verify(colisRepository, times(1)).save(colisCaptor.capture());
        assertThat(colisCaptor.getValue().getStatut()).isEqualTo(StatutColis.EN_TRANSIT);
    }

    @Test
    @DisplayName("Devrait envoyer notification si statut = LIVRE")
    void testChangerStatut_NotificationOnLivraison() {
        // Given
        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
        doNothing().when(notificationService).envoyerNotificationLivraison(any(), any());

        // When
        colisService.changerStatut("colis-1", StatutColis.LIVRE, "Colis livré");

        // Then
        verify(notificationService, times(1)).envoyerNotificationLivraison(destinataire, colis);
    }

    // ==================== Tests pour la méthode delete ====================

    @Test
    @DisplayName("Devrait supprimer un colis")
    void testDelete_Success() {
        // Given
        when(colisRepository.findById("colis-1")).thenReturn(Optional.of(colis));
        doNothing().when(colisRepository).delete(colis);

        // When
        colisService.delete("colis-1");

        // Then
        verify(colisRepository, times(1)).delete(colis);
    }

    // ==================== Tests pour les méthodes de recherche ====================

    @Test
    @DisplayName("Devrait récupérer les colis par statut")
    void testGetByStatut_Success() {
        // Given
        List<Colis> colisList = Arrays.asList(colis);
        when(colisRepository.findByStatut(StatutColis.CREE)).thenReturn(colisList);
        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(colisSimpleResponseDto);

        // When
        List<ColisSimpleResponseDto> result = colisService.getByStatut(StatutColis.CREE);

        // Then
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Devrait récupérer les colis par livreur")
    void testGetByLivreur_Success() {
        // Given
        List<Colis> colisList = Arrays.asList(colis);
        ColisAdvancedResponseDto advancedDto = new ColisAdvancedResponseDto();

        when(livreurRepository.findById("livreur-1")).thenReturn(Optional.of(livreur));
        when(colisRepository.findByLivreur(livreur)).thenReturn(colisList);
        when(smartLogiMapper.toAdvancedResponseDto(any(Colis.class))).thenReturn(advancedDto);

        // When
        List<ColisAdvancedResponseDto> result = colisService.getByLivreur("livreur-1");

        // Then
        assertThat(result).hasSize(1);
    }

    // ==================== Tests pour les méthodes de calcul ====================

    @Test
    @DisplayName("Devrait calculer le poids total d'un colis")
    void testCalculateTotal_Success() {
        // Given
        when(colisRepository.existsById("colis-1")).thenReturn(true);
        when(colisProduitRepository.calculateTotalPoidsByColis("colis-1")).thenReturn(15.5);

        // When
        Double result = colisService.calculateTotal("colis-1");

        // Then
        assertThat(result).isEqualTo(15.5);
    }

    @Test
    @DisplayName("Devrait lancer ArgementNotFoundExption si colis n'existe pas")
    void testCalculateTotal_ColisNotFound() {
        // Given
        when(colisRepository.existsById("colis-1")).thenReturn(false);

        // When & Then
        assertThrows(ArgementNotFoundExption.class, () -> colisService.calculateTotal("colis-1"));
    }

    @Test
    @DisplayName("Devrait calculer le prix total d'un colis")
    void testCalculateTotalPrix_Success() {
        // Given
        when(colisRepository.existsById("colis-1")).thenReturn(true);
        when(colisProduitRepository.calculateTotalPrixByColis("colis-1")).thenReturn(500.0);

        // When
        Double result = colisService.calculateTotalPrix("colis-1");

        // Then
        assertThat(result).isEqualTo(500.0);
    }

    @Test
    @DisplayName("Devrait récupérer le prix total d'un colis")
    void testGetPrixTotalColis_Success() {
        // Given
        ColisProduit colisProduit1 = new ColisProduit();
        colisProduit1.setPrix(BigDecimal.valueOf(100));

        ColisProduit colisProduit2 = new ColisProduit();
        colisProduit2.setPrix(BigDecimal.valueOf(200));

        List<ColisProduit> produits = Arrays.asList(colisProduit1, colisProduit2);
        when(colisProduitRepository.findByColisId("colis-1")).thenReturn(produits);

        // When
        BigDecimal result = colisService.getPrixTotalColis("colis-1");

        // Then
        assertThat(result).isEqualByComparingTo(BigDecimal.valueOf(300));
    }

    // ==================== Tests pour les méthodes de statistiques ====================

    @Test
    @DisplayName("Devrait compter les colis par statut")
    void testCountByStatut_Success() {
        // Given
        when(colisRepository.countByStatut(StatutColis.CREE)).thenReturn(5L);

        // When
        long result = colisService.countByStatut(StatutColis.CREE);

        // Then
        assertThat(result).isEqualTo(5L);
    }

    @Test
    @DisplayName("Devrait vérifier si un produit existe dans un colis")
    void testProduitExisteDansColis_Success() {
        // Given
        ColisProduitId colisProduitId = new ColisProduitId("colis-1", "produit-1");
        when(colisProduitRepository.existsById(colisProduitId)).thenReturn(true);

        // When
        boolean result = colisService.produitExisteDansColis("colis-1", "produit-1");

        // Then
        assertThat(result).isTrue();
    }
}
