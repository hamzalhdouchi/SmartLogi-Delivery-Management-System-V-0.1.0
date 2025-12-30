package com.smartlogi.smartlogiv010.serviceTest;//package com.smartlogi.smartlogiv010.serviceTest;
//
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ColisCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ProduitCreateRequestDto;
//import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ColisUpdateRequestDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisAdvancedResponseDto;
//import com.smartlogi.smartlogiv010.dto.responseDTO.Colis.ColisSimpleResponseDto;
//import com.smartlogi.smartlogiv010.entity.*;
//import com.smartlogi.smartlogiv010.enums.Priorite;
//import com.smartlogi.smartlogiv010.enums.StatutColis;
//import com.smartlogi.smartlogiv010.exception.ArgementNotFoundExption;
//import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
//import com.smartlogi.smartlogiv010.repository.*;
//import com.smartlogi.smartlogiv010.service.ColisServiceImpl;
//import com.smartlogi.smartlogiv010.service.NotificationService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@DisplayName("Colis Service - Complete Test Suite")
//class ColisServiceImplTest {
//
//    @Mock private ColisRepository colisRepository;
//    @Mock private ProduitRepository produitRepository;
//    @Mock private ColisProduitRepository colisProduitRepository;
//    @Mock private ClientExpediteurRepository clientExpediteurRepository;
//    @Mock private DestinataireRepository destinataireRepository;
//    @Mock private LivreurRepository livreurRepository;
//    @Mock private ZoneRepository zoneRepository;
//    @Mock private HistoriqueLivraisonRepository historiqueLivraisonRepository;
//    @Mock private SmartLogiMapper smartLogiMapper;
//    @Mock private NotificationService notificationService;
//
//    @InjectMocks private ColisServiceImpl colisService;
//
//    private Colis colis;
//    private ClientExpediteur client;
//    private Destinataire destinataire;
//    private Livreur livreur;
//    private Zone zone;
//    private Produit produit;
//    private ColisCreateRequestDto createRequestDto;
//    private ColisUpdateRequestDto updateRequestDto;
//    private ColisSimpleResponseDto simpleResponseDto;
//    private ColisAdvancedResponseDto advancedResponseDto;
//    private ColisProduit colisProduit;
//
//    @BeforeEach
//    void setUp() {
//        client = new ClientExpediteur();
//        client.setId("client-123");
//        client.setNom("Dupont");
//        client.setEmail("dupont@example.com");
//
//        destinataire = new Destinataire();
//        destinataire.setId("dest-123");
//        destinataire.setNom("Martin");
//          destinataire.setEmail("martin@example.com");
//
//        livreur = new Livreur();
//        livreur.setId("livreur-123");
//        livreur.setNom("Livreur");
//        livreur.setPrenom("Test");
//
//        zone = new Zone();
//        zone.setId("zone-123");
//        zone.setNom("Zone Test");
//
//        produit = new Produit();
//        produit.setId("produit-123");
//        produit.setNom("Laptop");
//        produit.setPrix(new BigDecimal("5999.99"));
//        produit.setPoids(new BigDecimal("2.5"));
//        produit.setCategorie("Electronique");
//
//        colis = new Colis();
//        colis.setId("colis-123");
//        colis.setDescription("Test colis");
//        colis.setPoids(new BigDecimal("10.5"));
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
//
//        colisProduit = new ColisProduit();
//        colisProduit.setId(new ColisProduitId("colis-123", "produit-123"));
//        colisProduit.setColis(colis);
//        colisProduit.setProduit(produit);
//        colisProduit.setQuantite(2);
//        colisProduit.setPrix(new BigDecimal("11999.98"));
//
//        createRequestDto = new ColisCreateRequestDto();
//        createRequestDto.setDescription("Test colis");
//        createRequestDto.setPoids(new BigDecimal("10.5"));
//        createRequestDto.setStatut(StatutColis.CREE);
//        createRequestDto.setPriorite(Priorite.NORMALE);
//        createRequestDto.setVilleDestination("Casablanca");
//        createRequestDto.setClientExpediteurId("client-123");
//        createRequestDto.setDestinataireId("dest-123");
//        createRequestDto.setLivreurId("livreur-123");
//        createRequestDto.setZoneId("zone-123");
//
//        updateRequestDto = new ColisUpdateRequestDto();
//        updateRequestDto.setId("colis-123");
//        updateRequestDto.setDescription("Updated");
//        updateRequestDto.setPoids(new BigDecimal("15.0"));
//
//        simpleResponseDto = new ColisSimpleResponseDto();
//        simpleResponseDto.setId("colis-123");
//
//        advancedResponseDto = new ColisAdvancedResponseDto();
//        advancedResponseDto.setId("colis-123");
//    }
//
//    @Test
//    @DisplayName("Create - Success without products")
//    void testCreate_WithoutProducts() {
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//        doNothing().when(notificationService).envoyerNotificationCollecte(any(), any());
//        when(historiqueLivraisonRepository.save(any(HistoriqueLivraison.class))).thenReturn(new HistoriqueLivraison());
//
//        ColisSimpleResponseDto result = colisService.create(createRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(colisRepository, times(1)).save(any(Colis.class));
//    }
//
//    @Test
//    @DisplayName("Create - Success with existing product")
//    void testCreate_WithExistingProduct() {
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setProduitId("produit-123");
//        produitDto.setQuantite(2);
//        createRequestDto.setProduits(Arrays.asList(produitDto));
//
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any())).thenReturn(colis);
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        when(colisProduitRepository.save(any())).thenReturn(colisProduit);
//        when(historiqueLivraisonRepository.save(any())).thenReturn(new HistoriqueLivraison());
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        ColisSimpleResponseDto result = colisService.create(createRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(produitRepository, times(1)).findById("produit-123");
//        verify(colisProduitRepository, times(1)).save(any());
//    }
//
//    @Test
//    @DisplayName("Create - Success with new product")
//    void testCreate_WithNewProduct() {
//        ProduitCreateRequestDto newProduit = new ProduitCreateRequestDto();
//        newProduit.setNom("New Product");
//        newProduit.setCategorie("Test");
//        newProduit.setPrix(new BigDecimal("100.00"));
//        newProduit.setPoids(new BigDecimal("1.0"));
//
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setNouveauProduit(newProduit);
//        produitDto.setQuantite(1);
//        createRequestDto.setProduits(Arrays.asList(produitDto));
//
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any())).thenReturn(colis);
//        when(produitRepository.existsByNomContainingIgnoreCase(anyString())).thenReturn(false);
//        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
//        when(colisProduitRepository.save(any())).thenReturn(colisProduit);
//        when(historiqueLivraisonRepository.save(any())).thenReturn(new HistoriqueLivraison());
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        ColisSimpleResponseDto result = colisService.create(createRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(produitRepository, times(1)).save(any(Produit.class));
//    }
//
//
//    @Test
//    @DisplayName("Create - Throw when product not found")
//    void testCreate_ProductNotFound() {
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setProduitId("produit-999");
//        produitDto.setQuantite(1);
//        createRequestDto.setProduits(Arrays.asList(produitDto));
//
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> colisService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("Produit non trouvé");
//    }
//
//    @Test
//    @DisplayName("Create - Throw when both produitId and nouveauProduit")
//    void testCreate_BothProduitIdAndNew() {
//        ProduitCreateRequestDto newProduit = new ProduitCreateRequestDto();
//        newProduit.setNom("Test");
//        newProduit.setPrix(new BigDecimal("100"));
//
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setProduitId("produit-123");
//        produitDto.setNouveauProduit(newProduit);
//        produitDto.setQuantite(1);
//        createRequestDto.setProduits(Arrays.asList(produitDto));
//
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//
//        assertThatThrownBy(() -> colisService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("à la fois");
//    }
//
//    @Test
//    @DisplayName("Create - Throw when neither produitId nor nouveauProduit")
//    void testCreate_NeitherProduitIdNorNew() {
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setQuantite(1);
//        createRequestDto.setProduits(Arrays.asList(produitDto));
//
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//
//        assertThatThrownBy(() -> colisService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("soit un produitId soit");
//    }
//
//
//    @Test
//    @DisplayName("Create - Throw when new product name blank")
//    void testCreate_NewProductNameBlank() {
//        ProduitCreateRequestDto newProduit = new ProduitCreateRequestDto();
//        newProduit.setNom("");
//        newProduit.setPrix(new BigDecimal("100"));
//
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setNouveauProduit(newProduit);
//        produitDto.setQuantite(1);
//        createRequestDto.setProduits(Arrays.asList(produitDto));
//
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//
//        assertThatThrownBy(() -> colisService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("nom du nouveau produit");
//    }
//
//
//    @Test
//    @DisplayName("Create - Throw when new product price zero")
//    void testCreate_NewProductPriceZero() {
//        ProduitCreateRequestDto newProduit = new ProduitCreateRequestDto();
//        newProduit.setNom("Test");
//        newProduit.setPrix(BigDecimal.ZERO);
//
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setNouveauProduit(newProduit);
//        produitDto.setQuantite(1);
//        createRequestDto.setProduits(Arrays.asList(produitDto));
//
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//
//        assertThatThrownBy(() -> colisService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("prix du nouveau produit");
//    }
//
//
//    @Test
//    @DisplayName("Create - Throw when quantity zero")
//    void testCreate_QuantityZero() {
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setProduitId("produit-123");
//        produitDto.setQuantite(0);
//        createRequestDto.setProduits(Arrays.asList(produitDto));
//
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//
//        assertThatThrownBy(() -> colisService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("quantité doit être supérieure");
//
//        verify(colisProduitRepository, never()).save(any());
//        verify(historiqueLivraisonRepository, never()).save(any());
//    }
//
//
//    @Test
//    @DisplayName("Create - Throw when product name exists")
//    void testCreate_ProductNameExists() {
//        ProduitCreateRequestDto newProduit = new ProduitCreateRequestDto();
//        newProduit.setNom("Existing");
//        newProduit.setPrix(new BigDecimal("100"));
//        newProduit.setPoids(new BigDecimal("1.0"));
//
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setNouveauProduit(newProduit);
//        produitDto.setQuantite(1);
//        createRequestDto.setProduits(Arrays.asList(produitDto));
//
//        when(clientExpediteurRepository.findById("client-123")).thenReturn(Optional.of(client));
//        when(destinataireRepository.findById("dest-123")).thenReturn(Optional.of(destinataire));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(smartLogiMapper.toEntity(any(ColisCreateRequestDto.class))).thenReturn(colis);
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(produitRepository.existsByNomContainingIgnoreCase("Existing")).thenReturn(true);
//
//        assertThatThrownBy(() -> colisService.create(createRequestDto))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("existe déjà");
//
//        verify(historiqueLivraisonRepository, never()).save(any());
//    }
//
//
//    @Test
//    @DisplayName("AjouterProduit - Success with existing product")
//    void testAjouterProduit_Success() {
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setProduitId("produit-123");
//        produitDto.setQuantite(2);
//
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(produitRepository.findById("produit-123")).thenReturn(Optional.of(produit));
//        when(colisProduitRepository.save(any())).thenReturn(colisProduit);
//            when(historiqueLivraisonRepository.save(any())).thenReturn(new HistoriqueLivraison());
//
//        colisService.ajouterProduit("colis-123", produitDto);
//
//        verify(colisProduitRepository, times(1)).save(any());
//        verify(historiqueLivraisonRepository, times(1)).save(any());
//    }
//
//    @Test
//    @DisplayName("AjouterProduit - Success with new product")
//    void testAjouterProduit_WithNewProduct() {
//        ProduitCreateRequestDto newProduit = new ProduitCreateRequestDto();
//        newProduit.setNom("New");
//        newProduit.setPrix(new BigDecimal("100"));
//        newProduit.setPoids(new BigDecimal("1.0"));
//        newProduit.setCategorie("Test");
//
//        ColisCreateRequestDto.ProduitColisDto produitDto = new ColisCreateRequestDto.ProduitColisDto();
//        produitDto.setNouveauProduit(newProduit);
//        produitDto.setQuantite(1);
//
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(produitRepository.existsByNomContainingIgnoreCase(anyString())).thenReturn(false);
//        when(produitRepository.save(any())).thenReturn(produit);
//        when(colisProduitRepository.save(any())).thenReturn(colisProduit);
//        when(historiqueLivraisonRepository.save(any())).thenReturn(new HistoriqueLivraison());
//
//        colisService.ajouterProduit("colis-123", produitDto);
//
//        verify(produitRepository, times(1)).save(any());
//        verify(historiqueLivraisonRepository, times(1)).save(any());
//    }
//
//    @Test
//    @DisplayName("SupprimerProduit - Success")
//    void testSupprimerProduit_Success() {
//        ColisProduitId id = new ColisProduitId("colis-123", "produit-123");
//        when(colisProduitRepository.existsById(id)).thenReturn(true);
//        doNothing().when(colisProduitRepository).deleteById(id);
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(historiqueLivraisonRepository.save(any())).thenReturn(new HistoriqueLivraison());
//
//        colisService.supprimerProduit("colis-123", "produit-123");
//
//        verify(colisProduitRepository, times(1)).deleteById(id);
//    }
//
//    @Test
//    @DisplayName("SupprimerProduit - Throw when not exists")
//    void testSupprimerProduit_NotExists() {
//        ColisProduitId id = new ColisProduitId("colis-123", "produit-999");
//        when(colisProduitRepository.existsById(id)).thenReturn(false);
//
//        assertThatThrownBy(() -> colisService.supprimerProduit("colis-123", "produit-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("Produit non trouvé dans ce colis");
//    }
//
//    @Test
//    @DisplayName("MettreAJourQuantiteProduit - Success")
//    void testMettreAJourQuantiteProduit_Success() {
//        ColisProduitId id = new ColisProduitId("colis-123", "produit-123");
//        when(colisProduitRepository.findById(id)).thenReturn(Optional.of(colisProduit));
//        when(colisProduitRepository.save(any())).thenReturn(colisProduit);
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(historiqueLivraisonRepository.save(any())).thenReturn(new HistoriqueLivraison());
//
//        colisService.mettreAJourQuantiteProduit("colis-123", "produit-123", 5);
//
//        verify(colisProduitRepository, times(1)).save(any());
//    }
//
//    @Test
//    @DisplayName("MettreAJourQuantiteProduit - Throw when quantity zero")
//    void testMettreAJourQuantiteProduit_QuantityZero() {
//        ColisProduitId id = new ColisProduitId("colis-123", "produit-123");
//        when(colisProduitRepository.findById(id)).thenReturn(Optional.of(colisProduit));
//
//        assertThatThrownBy(() -> colisService.mettreAJourQuantiteProduit("colis-123", "produit-123", 0))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("quantité doit être supérieure à 0");
//    }
//
//    @Test
//    @DisplayName("GetByLivreur - Should get colis by livreur")
//    void testGetByLivreur_Success() {
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(colisRepository.findByLivreur(livreur)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toAdvancedResponseDto(any(Colis.class))).thenReturn(advancedResponseDto);
//
//        List<ColisAdvancedResponseDto> result = colisService.getByLivreur("livreur-123");
//
//        assertThat(result).hasSize(1);
//        verify(livreurRepository, times(1)).findById("livreur-123");
//        verify(colisRepository, times(1)).findByLivreur(livreur);
//    }
//
//    @Test
//    @DisplayName("GetByLivreur - Should throw when livreur not found")
//    void testGetByLivreur_LivreurNotFound() {
//        when(livreurRepository.findById("livreur-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> colisService.getByLivreur("livreur-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Livreur non trouvé");
//
//        verify(colisRepository, never()).findByLivreur(any());
//    }
//
//    @Test
//    @DisplayName("GetByLivreur - Should return empty list")
//    void testGetByLivreur_EmptyList() {
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(colisRepository.findByLivreur(livreur)).thenReturn(new ArrayList<>());
//
//        List<ColisAdvancedResponseDto> result = colisService.getByLivreur("livreur-123");
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("GetByZone - Should get colis by zone")
//    void testGetByZone_Success() {
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(colisRepository.findByZone(zone)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByZone("zone-123");
//
//        assertThat(result).hasSize(1);
//        verify(zoneRepository, times(1)).findById("zone-123");
//        verify(colisRepository, times(1)).findByZone(zone);
//    }
//
//    @Test
//    @DisplayName("GetByZone - Should throw when zone not found")
//    void testGetByZone_ZoneNotFound() {
//        when(zoneRepository.findById("zone-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> colisService.getByZone("zone-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Zone non trouvée");
//
//        verify(colisRepository, never()).findByZone(any());
//    }
//
//    @Test
//    @DisplayName("GetByZone - Should return empty list")
//    void testGetByZone_EmptyList() {
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(colisRepository.findByZone(zone)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByZone("zone-123");
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("SearchByVilleDestination - Should find colis by ville")
//    void testSearchByVilleDestination_Success() {
//        when(colisRepository.findByVilleDestination("Casablanca")).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.searchByVilleDestination("Casablanca");
//
//        assertThat(result).hasSize(1);
//        verify(colisRepository, times(1)).findByVilleDestination("Casablanca");
//    }
//
//    @Test
//    @DisplayName("SearchByVilleDestination - Should return empty list")
//    void testSearchByVilleDestination_EmptyList() {
//        when(colisRepository.findByVilleDestination("Paris")).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.searchByVilleDestination("Paris");
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("AssignerLivreur - Should assign livreur successfully")
//    void testAssignerLivreur_Success() {
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(historiqueLivraisonRepository.save(any(HistoriqueLivraison.class))).thenReturn(new HistoriqueLivraison());
//
//        colisService.assignerLivreur("colis-123", "livreur-123");
//
//        verify(colisRepository, times(1)).save(colis);
//        verify(historiqueLivraisonRepository, times(1)).save(any(HistoriqueLivraison.class));
//    }
//
//    @Test
//    @DisplayName("AssignerLivreur - Should throw when colis not found")
//    void testAssignerLivreur_ColisNotFound() {
//        when(colisRepository.findById("colis-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> colisService.assignerLivreur("colis-999", "livreur-123"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Colis non trouvé");
//
//        verify(colisRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("AssignerLivreur - Should throw when livreur not found")
//    void testAssignerLivreur_LivreurNotFound() {
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(livreurRepository.findById("livreur-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> colisService.assignerLivreur("colis-123", "livreur-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Livreur non trouvé");
//
//        verify(colisRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("ChangerStatut - Should change statut successfully")
//    void testChangerStatut_Success() {
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(historiqueLivraisonRepository.save(any(HistoriqueLivraison.class))).thenReturn(new HistoriqueLivraison());
//
//        colisService.changerStatut("colis-123", StatutColis.EN_TRANSIT, "En route");
//
//        verify(colisRepository, times(1)).save(colis);
//        verify(historiqueLivraisonRepository, times(1)).save(any(HistoriqueLivraison.class));
//    }
//
//    @Test
//    @DisplayName("ChangerStatut - Should send notification when LIVRE")
//    void testChangerStatut_SendNotificationWhenLivre() {
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(historiqueLivraisonRepository.save(any(HistoriqueLivraison.class))).thenReturn(new HistoriqueLivraison());
//        doNothing().when(notificationService).envoyerNotificationLivraison(any(Destinataire.class), any(Colis.class));
//
//        colisService.changerStatut("colis-123", StatutColis.LIVRE, "Livré");
//
//        verify(notificationService, times(1)).envoyerNotificationLivraison(destinataire, colis);
//        verify(colisRepository, times(1)).save(colis);
//    }
//
//    @Test
//    @DisplayName("ChangerStatut - Should not send notification when not LIVRE")
//    void testChangerStatut_NoNotificationWhenNotLivre() {
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(historiqueLivraisonRepository.save(any(HistoriqueLivraison.class))).thenReturn(new HistoriqueLivraison());
//
//        colisService.changerStatut("colis-123", StatutColis.EN_TRANSIT, "En route");
//
//        verify(notificationService, never()).envoyerNotificationLivraison(any(), any());
//    }
//
//    @Test
//    @DisplayName("ChangerStatut - Should use default comment when null")
//    void testChangerStatut_DefaultComment() {
//        colis.setStatut(StatutColis.CREE);
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(historiqueLivraisonRepository.save(any(HistoriqueLivraison.class))).thenReturn(new HistoriqueLivraison());
//
//        colisService.changerStatut("colis-123", StatutColis.EN_TRANSIT, null);
//
//        verify(historiqueLivraisonRepository, times(1)).save(any(HistoriqueLivraison.class));
//    }
//
//    @Test
//    @DisplayName("ChangerStatut - Should throw when colis not found")
//    void testChangerStatut_ColisNotFound() {
//        when(colisRepository.findById("colis-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> colisService.changerStatut("colis-999", StatutColis.EN_TRANSIT, "Test"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Colis non trouvé");
//
//        verify(colisRepository, never()).save(any());
//    }
//
//    @Test
//    @DisplayName("Delete - Should delete colis successfully")
//    void testDelete_Success() {
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        doNothing().when(colisRepository).delete(any(Colis.class));
//
//        colisService.delete("colis-123");
//
//        verify(colisRepository, times(1)).delete(colis);
//    }
//
//    @Test
//    @DisplayName("Delete - Should throw when colis not found")
//    void testDelete_ColisNotFound() {
//        when(colisRepository.findById("colis-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> colisService.delete("colis-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Colis non trouvé");
//
//        verify(colisRepository, never()).delete(any());
//    }
//
//    @Test
//    @DisplayName("GetByPriorite - Should get colis by priorite")
//    void testGetByPriorite_Success() {
//        when(colisRepository.findByPriorite(Priorite.NORMALE)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByPriorite(Priorite.NORMALE);
//
//        assertThat(result).hasSize(1);
//        verify(colisRepository, times(1)).findByPriorite(Priorite.NORMALE);
//    }
//
//    @Test
//    @DisplayName("GetByPriorite - Should return empty list")
//    void testGetByPriorite_EmptyList() {
//        when(colisRepository.findByPriorite(Priorite.URGENTE)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByPriorite(Priorite.URGENTE);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("GetByPrioriteAndStatut - Should get colis by priorite and statut")
//    void testGetByPrioriteAndStatut_Success() {
//        when(colisRepository.findByPrioriteAndStatut(Priorite.NORMALE, StatutColis.CREE)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByPrioriteAndStatut(Priorite.NORMALE, StatutColis.CREE);
//
//        assertThat(result).hasSize(1);
//        verify(colisRepository, times(1)).findByPrioriteAndStatut(Priorite.NORMALE, StatutColis.CREE);
//    }
//
//    @Test
//    @DisplayName("GetByPrioriteAndStatut - Should return empty list")
//    void testGetByPrioriteAndStatut_EmptyList() {
//        when(colisRepository.findByPrioriteAndStatut(Priorite.URGENTE, StatutColis.LIVRE)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByPrioriteAndStatut(Priorite.URGENTE, StatutColis.LIVRE);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("Update - Success without livreur and zone")
//    void testUpdate_WithoutLivreurAndZone() {
//        updateRequestDto.setLivreurId(null);
//        updateRequestDto.setZoneId(null);
//
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        doNothing().when(smartLogiMapper).updateEntityFromDto(any(ColisUpdateRequestDto.class), any(Colis.class));
//        when(colisRepository.save(any(Colis.class))).thenReturn(colis);
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        ColisSimpleResponseDto result = colisService.update("colis-123", updateRequestDto);
//
//        assertThat(result).isNotNull();
//        verify(livreurRepository, never()).findById(anyString());
//        verify(zoneRepository, never()).findById(anyString());
//    }
//
//    @Test
//    @DisplayName("GetByClientExpediteur - Success")
//    void testGetByClientExpediteur_Success() {
//        when(colisRepository.searchByKeyword("client-123")).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByClientExpediteur("client-123");
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("GetByDestinataire - Success")
//    void testGetByDestinataire_Success() {
//        when(colisRepository.searchByKeyword("dest-123")).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByDestinataire("dest-123");
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("GetByLivreurAndStatut - Success")
//    void testGetByLivreurAndStatut_Success() {
//        when(livreurRepository.findById("livreur-123")).thenReturn(Optional.of(livreur));
//        when(colisRepository.findByLivreurAndStatut(livreur, StatutColis.CREE)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByLivreurAndStatut("livreur-123", StatutColis.CREE);
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("GetByZoneAndStatut - Success")
//    void testGetByZoneAndStatut_Success() {
//        when(zoneRepository.findById("zone-123")).thenReturn(Optional.of(zone));
//        when(colisRepository.findByZoneAndStatut(zone, StatutColis.CREE)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByZoneAndStatut("zone-123", StatutColis.CREE);
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("GetByVilleDestinationAndStatut - Success")
//    void testGetByVilleDestinationAndStatut_Success() {
//        when(colisRepository.findByVilleDestinationAndStatut("Casablanca", StatutColis.CREE))
//                .thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByVilleDestinationAndStatut("Casablanca", StatutColis.CREE);
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("SearchByKeyword - Success")
//    void testSearchByKeyword_Success() {
//        when(colisRepository.searchByKeyword("test")).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.searchByKeyword("test");
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("GetByZoneId - Success with pagination")
//    void testGetByZoneId_Success() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Colis> page = new PageImpl<>(Arrays.asList(colis), pageable, 1);
//        when(colisRepository.findByZoneId("zone-123", pageable)).thenReturn(page);
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        Page<ColisSimpleResponseDto> result = colisService.getByZoneId("zone-123", pageable);
//
//        assertThat(result.getContent()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("GetByStatut - Success with pagination")
//    void testGetByStatut_WithPagination() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Colis> page = new PageImpl<>(Arrays.asList(colis), pageable, 1);
//        when(colisRepository.findByStatut(StatutColis.CREE, pageable)).thenReturn(page);
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        Page<ColisSimpleResponseDto> result = colisService.getByStatut(StatutColis.CREE, pageable);
//
//        assertThat(result.getContent()).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("CountByZoneAndStatut - Success")
//    void testCountByZoneAndStatut_Success() {
//        when(colisRepository.countByZoneAndStatut("zone-123", StatutColis.CREE)).thenReturn(5L);
//
//        long result = colisService.countByZoneAndStatut("zone-123", StatutColis.CREE);
//
//        assertThat(result).isEqualTo(5L);
//    }
//
//    @Test
//    @DisplayName("CountByLivreurAndStatut - Success")
//    void testCountByLivreurAndStatut_Success() {
//        when(colisRepository.countByLivreurAndStatut("livreur-123", StatutColis.CREE)).thenReturn(3L);
//
//        long result = colisService.countByLivreurAndStatut("livreur-123", StatutColis.CREE);
//
//        assertThat(result).isEqualTo(3L);
//    }
//
//    @Test
//    @DisplayName("GetProduitsByColis - Success")
//    void testGetProduitsByColis_Success() {
//        when(colisProduitRepository.findByColisId("colis-123")).thenReturn(Arrays.asList(colisProduit));
//
//        List<ColisProduit> result = colisService.getProduitsByColis("colis-123");
//
//        assertThat(result).hasSize(1);
//    }
//
//
//    @Test
//    @DisplayName("GetPoidsTotalParLivreur - Throw on error")
//    void testGetPoidsTotalParLivreur_Error() {
//        when(colisRepository.findPoidsTotalParLivreur()).thenThrow(new RuntimeException("DB Error"));
//
//        assertThatThrownBy(() -> colisService.getPoidsTotalParLivreur())
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("Impossible de calculer le poids par livreur");
//    }
//
//
//
//    @Test
//    @DisplayName("GetPoidsDetailParLivreur - Throw on error")
//    void testGetPoidsDetailParLivreur_Error() {
//        when(colisRepository.findPoidsDetailParLivreur()).thenThrow(new RuntimeException("DB Error"));
//
//        assertThatThrownBy(() -> colisService.getPoidsDetailParLivreur())
//                .isInstanceOf(RuntimeException.class)
//                .hasMessageContaining("Impossible de calculer le détail poids par livreur");
//    }
//
//    @Test
//    @DisplayName("GetColisEnRetard - Should get colis en retard")
//    void testGetColisEnRetard_Success() {
//        when(colisRepository.findColisEnRetard(any(LocalDateTime.class))).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getColisEnRetard();
//
//        assertThat(result).hasSize(1);
//        verify(colisRepository, times(1)).findColisEnRetard(any(LocalDateTime.class));
//    }
//
//    @Test
//    @DisplayName("GetColisEnRetard - Should return empty list")
//    void testGetColisEnRetard_EmptyList() {
//        when(colisRepository.findColisEnRetard(any(LocalDateTime.class))).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getColisEnRetard();
//
//        assertThat(result).isEmpty();
//        verify(colisRepository, times(1)).findColisEnRetard(any(LocalDateTime.class));
//    }
//
//    @Test
//    @DisplayName("GetColisEnRetard - Should use correct date limit")
//    void testGetColisEnRetard_CorrectDateLimit() {
//        ArgumentCaptor<LocalDateTime> dateCaptor = ArgumentCaptor.forClass(LocalDateTime.class);
//        when(colisRepository.findColisEnRetard(any(LocalDateTime.class))).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        colisService.getColisEnRetard();
//
//        verify(colisRepository).findColisEnRetard(dateCaptor.capture());
//        LocalDateTime capturedDate = dateCaptor.getValue();
//        LocalDateTime expected = LocalDateTime.now().minusDays(2);
//
//        assertThat(capturedDate)
//                .isBefore(LocalDateTime.now())
//                .isAfter(expected.minusSeconds(5));
//
//    }
//
//    @Test
//    @DisplayName("GetColisEnRetard - Should handle multiple colis")
//    void testGetColisEnRetard_MultipleColis() {
//        Colis colis2 = new Colis();
//        colis2.setId("colis-456");
//
//        ColisSimpleResponseDto dto2 = new ColisSimpleResponseDto();
//        dto2.setId("colis-456");
//
//        when(colisRepository.findColisEnRetard(any(LocalDateTime.class))).thenReturn(Arrays.asList(colis, colis2));
//        when(smartLogiMapper.toSimpleResponseDto(colis)).thenReturn(simpleResponseDto);
//        when(smartLogiMapper.toSimpleResponseDto(colis2)).thenReturn(dto2);
//
//        List<ColisSimpleResponseDto> result = colisService.getColisEnRetard();
//
//        assertThat(result).hasSize(2);
//    }
//
//    @Test
//    @DisplayName("GetByDateCreationBetween - Should get colis in date range")
//    void testGetByDateCreationBetween_Success() {
//        LocalDateTime start = LocalDateTime.now().minusDays(7);
//        LocalDateTime end = LocalDateTime.now();
//
//        when(colisRepository.findByDateCreationBetween(start, end)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByDateCreationBetween(start, end);
//
//        assertThat(result).hasSize(1);
//        verify(colisRepository, times(1)).findByDateCreationBetween(start, end);
//    }
//
//    @Test
//    @DisplayName("GetByDateCreationBetween - Should return empty list")
//    void testGetByDateCreationBetween_EmptyList() {
//        LocalDateTime start = LocalDateTime.now().minusDays(30);
//        LocalDateTime end = LocalDateTime.now().minusDays(20);
//
//        when(colisRepository.findByDateCreationBetween(start, end)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByDateCreationBetween(start, end);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("GetByDateCreationBetween - Should handle same start and end date")
//    void testGetByDateCreationBetween_SameDate() {
//        LocalDateTime sameDate = LocalDateTime.now();
//
//        when(colisRepository.findByDateCreationBetween(sameDate, sameDate)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByDateCreationBetween(sameDate, sameDate);
//
//        assertThat(result).hasSize(1);
//    }
//
//    @Test
//    @DisplayName("GetByDateCreationBetween - Should handle large date range")
//    void testGetByDateCreationBetween_LargeDateRange() {
//        LocalDateTime start = LocalDateTime.now().minusYears(1);
//        LocalDateTime end = LocalDateTime.now().plusDays(1);
//
//        when(colisRepository.findByDateCreationBetween(start, end)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByDateCreationBetween(start, end);
//
//        assertThat(result).hasSize(1);
//        verify(colisRepository, times(1)).findByDateCreationBetween(start, end);
//    }
//
//    @Test
//    @DisplayName("GetByDateCreationBetween - Should handle multiple results")
//    void testGetByDateCreationBetween_MultipleResults() {
//        LocalDateTime start = LocalDateTime.now().minusDays(7);
//        LocalDateTime end = LocalDateTime.now();
//
//        Colis colis1 = new Colis();
//        colis1.setId("colis-1");
//        colis1.setDateCreation(LocalDateTime.now().minusDays(5));
//
//        Colis colis2 = new Colis();
//        colis2.setId("colis-2");
//        colis2.setDateCreation(LocalDateTime.now().minusDays(3));
//
//        Colis colis3 = new Colis();
//        colis3.setId("colis-3");
//        colis3.setDateCreation(LocalDateTime.now().minusDays(1));
//
//        ColisSimpleResponseDto dto1 = new ColisSimpleResponseDto();
//        dto1.setId("colis-1");
//        ColisSimpleResponseDto dto2 = new ColisSimpleResponseDto();
//        dto2.setId("colis-2");
//        ColisSimpleResponseDto dto3 = new ColisSimpleResponseDto();
//        dto3.setId("colis-3");
//
//        when(colisRepository.findByDateCreationBetween(start, end)).thenReturn(Arrays.asList(colis1, colis2, colis3));
//        when(smartLogiMapper.toSimpleResponseDto(colis1)).thenReturn(dto1);
//        when(smartLogiMapper.toSimpleResponseDto(colis2)).thenReturn(dto2);
//        when(smartLogiMapper.toSimpleResponseDto(colis3)).thenReturn(dto3);
//
//        List<ColisSimpleResponseDto> result = colisService.getByDateCreationBetween(start, end);
//
//        assertThat(result).hasSize(3);
//        verify(smartLogiMapper, times(3)).toSimpleResponseDto(any(Colis.class));
//    }
//
//    @Test
//    @DisplayName("GetByDateCreationBetween - Should handle future dates")
//    void testGetByDateCreationBetween_FutureDates() {
//        LocalDateTime start = LocalDateTime.now().plusDays(1);
//        LocalDateTime end = LocalDateTime.now().plusDays(7);
//
//        when(colisRepository.findByDateCreationBetween(start, end)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByDateCreationBetween(start, end);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("GetByDateCreationBetween - Should handle past dates only")
//    void testGetByDateCreationBetween_PastDatesOnly() {
//        LocalDateTime start = LocalDateTime.now().minusMonths(2);
//        LocalDateTime end = LocalDateTime.now().minusMonths(1);
//
//        when(colisRepository.findByDateCreationBetween(start, end)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByDateCreationBetween(start, end);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("GetByDateCreationBetween - Should handle one day range")
//    void testGetByDateCreationBetween_OneDayRange() {
//        LocalDateTime start = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0);
//        LocalDateTime end = LocalDateTime.now().minusDays(1).withHour(23).withMinute(59).withSecond(59);
//
//        when(colisRepository.findByDateCreationBetween(start, end)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByDateCreationBetween(start, end);
//
//        assertThat(result).hasSize(1);
//    }
//
//
//    @Test
//    @DisplayName("GetById - Should get colis by id successfully")
//    void testGetById_Success() {
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        ColisSimpleResponseDto result = colisService.getById("colis-123");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("colis-123");
//        verify(colisRepository, times(1)).findById("colis-123");
//        verify(smartLogiMapper, times(1)).toSimpleResponseDto(colis);
//    }
//
//    @Test
//    @DisplayName("GetById - Should throw exception when colis not found")
//    void testGetById_NotFound() {
//        when(colisRepository.findById("colis-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> colisService.getById("colis-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Colis non trouvé");
//
//        verify(colisRepository, times(1)).findById("colis-999");
//        verify(smartLogiMapper, never()).toSimpleResponseDto(any(Colis.class));
//    }
//
//    @Test
//    @DisplayName("GetByIdWithDetails - Should get colis with details successfully")
//    void testGetByIdWithDetails_Success() {
//        when(colisRepository.findById("colis-123")).thenReturn(Optional.of(colis));
//        when(smartLogiMapper.toAdvancedResponseDto(any(Colis.class))).thenReturn(advancedResponseDto);
//
//        ColisAdvancedResponseDto result = colisService.getByIdWithDetails("colis-123");
//
//        assertThat(result).isNotNull();
//        assertThat(result.getId()).isEqualTo("colis-123");
//        verify(colisRepository, times(1)).findById("colis-123");
//        verify(smartLogiMapper, times(1)).toAdvancedResponseDto(colis);
//    }
//
//    @Test
//    @DisplayName("GetByIdWithDetails - Should throw exception when colis not found")
//    void testGetByIdWithDetails_NotFound() {
//        when(colisRepository.findById("colis-999")).thenReturn(Optional.empty());
//
//        assertThatThrownBy(() -> colisService.getByIdWithDetails("colis-999"))
//                .isInstanceOf(RuntimeException.class)
//                .hasMessage("Colis non trouvé");
//
//        verify(colisRepository, times(1)).findById("colis-999");
//        verify(smartLogiMapper, never()).toAdvancedResponseDto(any(Colis.class));
//    }
//
//    @Test
//    @DisplayName("GetAll - Should get all colis with pagination")
//    void testGetAll_WithPagination() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Colis> page = new PageImpl<>(Arrays.asList(colis), pageable, 1);
//
//        when(colisRepository.findAll(any(Pageable.class))).thenReturn(page);
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        Page<ColisSimpleResponseDto> result = colisService.getAll(pageable);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).hasSize(1);
//        assertThat(result.getTotalElements()).isEqualTo(1);
//        verify(colisRepository, times(1)).findAll(pageable);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should return empty page when no colis exist")
//    void testGetAll_EmptyPage() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Colis> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);
//
//        when(colisRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);
//
//        Page<ColisSimpleResponseDto> result = colisService.getAll(pageable);
//
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).isEmpty();
//        assertThat(result.getTotalElements()).isZero();
//    }
//
//    @Test
//    @DisplayName("GetAll - Should handle multiple pages")
//    void testGetAll_MultiplePages() {
//        Pageable pageable = PageRequest.of(1, 5);
//        List<Colis> colisList = Arrays.asList(colis);
//        Page<Colis> page = new PageImpl<>(colisList, pageable, 25);
//
//        when(colisRepository.findAll(any(Pageable.class))).thenReturn(page);
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        Page<ColisSimpleResponseDto> result = colisService.getAll(pageable);
//
//        assertThat(result.getTotalPages()).isEqualTo(5);
//        assertThat(result.getNumber()).isEqualTo(1);
//        assertThat(result.getSize()).isEqualTo(5);
//    }
//
//    @Test
//    @DisplayName("GetAll - Should handle first page")
//    void testGetAll_FirstPage() {
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Colis> page = new PageImpl<>(Arrays.asList(colis), pageable, 50);
//
//        when(colisRepository.findAll(any(Pageable.class))).thenReturn(page);
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        Page<ColisSimpleResponseDto> result = colisService.getAll(pageable);
//
//        assertThat(result.isFirst()).isTrue();
//        assertThat(result.hasNext()).isTrue();
//    }
//
//    @Test
//    @DisplayName("GetAll - Should handle last page")
//    void testGetAll_LastPage() {
//        Pageable pageable = PageRequest.of(4, 10);
//        Page<Colis> page = new PageImpl<>(Arrays.asList(colis), pageable, 45);
//
//        when(colisRepository.findAll(any(Pageable.class))).thenReturn(page);
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        Page<ColisSimpleResponseDto> result = colisService.getAll(pageable);
//
//        assertThat(result.isLast()).isTrue();
//        assertThat(result.hasNext()).isFalse();
//    }
//
//    @Test
//    @DisplayName("GetByStatut - Should get colis by statut CREE")
//    void testGetByStatut_CREE() {
//        when(colisRepository.findByStatut(StatutColis.CREE)).thenReturn(Arrays.asList(colis));
//        when(smartLogiMapper.toSimpleResponseDto(any(Colis.class))).thenReturn(simpleResponseDto);
//
//        List<ColisSimpleResponseDto> result = colisService.getByStatut(StatutColis.CREE);
//
//        assertThat(result).hasSize(1);
//        verify(colisRepository, times(1)).findByStatut(StatutColis.CREE);
//    }
//
//    @Test
//    @DisplayName("GetByStatut - Should get colis by statut COLLECTE")
//    void testGetByStatut_COLLECTE() {
//        when(colisRepository.findByStatut(StatutColis.COLLECTE)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByStatut(StatutColis.COLLECTE);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("GetByStatut - Should get colis by statut EN_STOCK")
//    void testGetByStatut_EN_STOCK() {
//        when(colisRepository.findByStatut(StatutColis.EN_STOCK)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByStatut(StatutColis.EN_STOCK);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("GetByStatut - Should get colis by statut EN_TRANSIT")
//    void testGetByStatut_EN_TRANSIT() {
//        when(colisRepository.findByStatut(StatutColis.EN_TRANSIT)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByStatut(StatutColis.EN_TRANSIT);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("GetByStatut - Should get colis by statut LIVRE")
//    void testGetByStatut_LIVRE() {
//        when(colisRepository.findByStatut(StatutColis.LIVRE)).thenReturn(new ArrayList<>());
//
//        List<ColisSimpleResponseDto> result = colisService.getByStatut(StatutColis.LIVRE);
//
//        assertThat(result).isEmpty();
//    }
//
//    @Test
//    @DisplayName("GetByStatut - Should handle multiple colis with same statut")
//    void testGetByStatut_MultipleColis() {
//        Colis colis1 = new Colis();
//        colis1.setId("colis-1");
//        colis1.setStatut(StatutColis.EN_TRANSIT);
//
//        Colis colis2 = new Colis();
//        colis2.setId("colis-2");
//        colis2.setStatut(StatutColis.EN_TRANSIT);
//
//        ColisSimpleResponseDto dto1 = new ColisSimpleResponseDto();
//        dto1.setId("colis-1");
//        ColisSimpleResponseDto dto2 = new ColisSimpleResponseDto();
//        dto2.setId("colis-2");
//
//        when(colisRepository.findByStatut(StatutColis.EN_TRANSIT)).thenReturn(Arrays.asList(colis1, colis2));
//        when(smartLogiMapper.toSimpleResponseDto(colis1)).thenReturn(dto1);
//        when(smartLogiMapper.toSimpleResponseDto(colis2)).thenReturn(dto2);
//
//        List<ColisSimpleResponseDto> result = colisService.getByStatut(StatutColis.EN_TRANSIT);
//
//        assertThat(result).hasSize(2);
//        verify(smartLogiMapper, times(2)).toSimpleResponseDto(any(Colis.class));
//    }
//
//    @Test
//    @DisplayName("ProduitExisteDansColis - Should return true when product exists in colis")
//    void testProduitExisteDansColis_True() {
//        ColisProduitId id = new ColisProduitId("colis-123", "produit-123");
//        when(colisProduitRepository.existsById(id)).thenReturn(true);
//
//        boolean result = colisService.produitExisteDansColis("colis-123", "produit-123");
//
//        assertThat(result).isTrue();
//        verify(colisProduitRepository, times(1)).existsById(id);
//    }
//
//    @Test
//    @DisplayName("ProduitExisteDansColis - Should return false when product does not exist")
//    void testProduitExisteDansColis_False() {
//        ColisProduitId id = new ColisProduitId("colis-123", "produit-999");
//        when(colisProduitRepository.existsById(id)).thenReturn(false);
//
//        boolean result = colisService.produitExisteDansColis("colis-123", "produit-999");
//
//        assertThat(result).isFalse();
//        verify(colisProduitRepository, times(1)).existsById(id);
//    }
//
//    @Test
//    @DisplayName("ProduitExisteDansColis - Should handle non-existent colis")
//    void testProduitExisteDansColis_NonExistentColis() {
//        ColisProduitId id = new ColisProduitId("colis-999", "produit-123");
//        when(colisProduitRepository.existsById(id)).thenReturn(false);
//
//        boolean result = colisService.produitExisteDansColis("colis-999", "produit-123");
//
//        assertThat(result).isFalse();
//    }
//
//    @Test
//    @DisplayName("GetPrixTotalColis - Should calculate total prix successfully")
//    void testGetPrixTotalColis_Success() {
//        ColisProduit cp1 = new ColisProduit();
//        cp1.setPrix(new BigDecimal("1999.99"));
//
//        ColisProduit cp2 = new ColisProduit();
//        cp2.setPrix(new BigDecimal("500.00"));
//
//        when(colisProduitRepository.findByColisId("colis-123")).thenReturn(Arrays.asList(cp1, cp2));
//
//        BigDecimal result = colisService.getPrixTotalColis("colis-123");
//
//        assertThat(result).isEqualByComparingTo(new BigDecimal("2499.99"));
//        verify(colisProduitRepository, times(1)).findByColisId("colis-123");
//    }
//
//    @Test
//    @DisplayName("GetPrixTotalColis - Should return zero when no products")
//    void testGetPrixTotalColis_NoProduits() {
//        when(colisProduitRepository.findByColisId("colis-123")).thenReturn(new ArrayList<>());
//
//        BigDecimal result = colisService.getPrixTotalColis("colis-123");
//
//        assertThat(result).isEqualByComparingTo(BigDecimal.ZERO);
//    }
//
//    @Test
//    @DisplayName("GetPrixTotalColis - Should handle single product")
//    void testGetPrixTotalColis_SingleProduct() {
//        ColisProduit cp = new ColisProduit();
//        cp.setPrix(new BigDecimal("999.99"));
//
//        when(colisProduitRepository.findByColisId("colis-123")).thenReturn(Arrays.asList(cp));
//
//        BigDecimal result = colisService.getPrixTotalColis("colis-123");
//
//        assertThat(result).isEqualByComparingTo(new BigDecimal("999.99"));
//    }
//
//    @Test
//    @DisplayName("GetPrixTotalColis - Should handle multiple products")
//    void testGetPrixTotalColis_MultipleProducts() {
//        ColisProduit cp1 = new ColisProduit();
//        cp1.setPrix(new BigDecimal("100.00"));
//
//        ColisProduit cp2 = new ColisProduit();
//        cp2.setPrix(new BigDecimal("200.50"));
//
//        ColisProduit cp3 = new ColisProduit();
//        cp3.setPrix(new BigDecimal("300.25"));
//
//        when(colisProduitRepository.findByColisId("colis-123")).thenReturn(Arrays.asList(cp1, cp2, cp3));
//
//        BigDecimal result = colisService.getPrixTotalColis("colis-123");
//
//        assertThat(result).isEqualByComparingTo(new BigDecimal("600.75"));
//    }
//
//    @Test
//    @DisplayName("CalculateTotal - Should calculate total poids successfully")
//    void testCalculateTotal_Success() {
//        when(colisRepository.existsById("colis-123")).thenReturn(true);
//        when(colisProduitRepository.calculateTotalPoidsByColis("colis-123")).thenReturn(15.5);
//
//        Double result = colisService.calculateTotal("colis-123");
//
//        assertThat(result).isEqualTo(15.5);
//        verify(colisRepository, times(1)).existsById("colis-123");
//        verify(colisProduitRepository, times(1)).calculateTotalPoidsByColis("colis-123");
//    }
//
//    @Test
//    @DisplayName("CalculateTotal - Should throw exception when colis not found")
//    void testCalculateTotal_ColisNotFound() {
//        when(colisRepository.existsById("colis-999")).thenReturn(false);
//
//        assertThatThrownBy(() -> colisService.calculateTotal("colis-999"))
//                .isInstanceOf(ArgementNotFoundExption.class)
//                .hasMessageContaining("this colis is not found");
//
//        verify(colisProduitRepository, never()).calculateTotalPoidsByColis(anyString());
//    }
//
//    @Test
//    @DisplayName("CalculateTotal - Should handle zero poids")
//    void testCalculateTotal_ZeroPoids() {
//        when(colisRepository.existsById("colis-123")).thenReturn(true);
//        when(colisProduitRepository.calculateTotalPoidsByColis("colis-123")).thenReturn(0.0);
//
//        Double result = colisService.calculateTotal("colis-123");
//
//        assertThat(result).isZero();
//    }
//
//    @Test
//    @DisplayName("CalculateTotal - Should handle large poids")
//    void testCalculateTotal_LargePoids() {
//        when(colisRepository.existsById("colis-123")).thenReturn(true);
//        when(colisProduitRepository.calculateTotalPoidsByColis("colis-123")).thenReturn(999.99);
//
//        Double result = colisService.calculateTotal("colis-123");
//
//        assertThat(result).isEqualTo(999.99);
//    }
//
//    @Test
//    @DisplayName("CalculateTotalPrix - Should calculate total prix successfully")
//    void testCalculateTotalPrix_Success() {
//        when(colisRepository.existsById("colis-123")).thenReturn(true);
//        when(colisProduitRepository.calculateTotalPrixByColis("colis-123")).thenReturn(1999.99);
//
//        Double result = colisService.calculateTotalPrix("colis-123");
//
//        assertThat(result).isEqualTo(1999.99);
//        verify(colisRepository, times(1)).existsById("colis-123");
//        verify(colisProduitRepository, times(1)).calculateTotalPrixByColis("colis-123");
//    }
//
//    @Test
//    @DisplayName("CalculateTotalPrix - Should throw exception when colis not found")
//    void testCalculateTotalPrix_ColisNotFound() {
//        when(colisRepository.existsById("colis-999")).thenReturn(false);
//
//        assertThatThrownBy(() -> colisService.calculateTotalPrix("colis-999"))
//                .isInstanceOf(ArgementNotFoundExption.class)
//                .hasMessageContaining("this colis is not found");
//
//        verify(colisProduitRepository, never()).calculateTotalPrixByColis(anyString());
//    }
//
//    @Test
//    @DisplayName("CalculateTotalPrix - Should handle zero prix")
//    void testCalculateTotalPrix_ZeroPrix() {
//        when(colisRepository.existsById("colis-123")).thenReturn(true);
//        when(colisProduitRepository.calculateTotalPrixByColis("colis-123")).thenReturn(0.0);
//
//        Double result = colisService.calculateTotalPrix("colis-123");
//
//        assertThat(result).isZero();
//    }
//
//    @Test
//    @DisplayName("CalculateTotalPrix - Should handle large prix")
//    void testCalculateTotalPrix_LargePrix() {
//        when(colisRepository.existsById("colis-123")).thenReturn(true);
//        when(colisProduitRepository.calculateTotalPrixByColis("colis-123")).thenReturn(99999.99);
//
//        Double result = colisService.calculateTotalPrix("colis-123");
//
//        assertThat(result).isEqualTo(99999.99);
//    }
//
//    @Test
//    @DisplayName("CountByStatut - Should count colis by statut CREE")
//    void testCountByStatut_CREE() {
//        when(colisRepository.countByStatut(StatutColis.CREE)).thenReturn(5L);
//
//        long result = colisService.countByStatut(StatutColis.CREE);
//
//        assertThat(result).isEqualTo(5L);
//        verify(colisRepository, times(1)).countByStatut(StatutColis.CREE);
//    }
//
//    @Test
//    @DisplayName("CountByStatut - Should count colis by statut COLLECTE")
//    void testCountByStatut_COLLECTE() {
//        when(colisRepository.countByStatut(StatutColis.COLLECTE)).thenReturn(3L);
//
//        long result = colisService.countByStatut(StatutColis.COLLECTE);
//
//        assertThat(result).isEqualTo(3L);
//    }
//
//    @Test
//    @DisplayName("CountByStatut - Should count colis by statut EN_STOCK")
//    void testCountByStatut_EN_STOCK() {
//        when(colisRepository.countByStatut(StatutColis.EN_STOCK)).thenReturn(7L);
//
//        long result = colisService.countByStatut(StatutColis.EN_STOCK);
//
//        assertThat(result).isEqualTo(7L);
//    }
//
//    @Test
//    @DisplayName("CountByStatut - Should count colis by statut EN_TRANSIT")
//    void testCountByStatut_EN_TRANSIT() {
//        when(colisRepository.countByStatut(StatutColis.EN_TRANSIT)).thenReturn(10L);
//
//        long result = colisService.countByStatut(StatutColis.EN_TRANSIT);
//
//        assertThat(result).isEqualTo(10L);
//    }
//
//    @Test
//    @DisplayName("CountByStatut - Should count colis by statut LIVRE")
//    void testCountByStatut_LIVRE() {
//        when(colisRepository.countByStatut(StatutColis.LIVRE)).thenReturn(15L);
//
//        long result = colisService.countByStatut(StatutColis.LIVRE);
//
//        assertThat(result).isEqualTo(15L);
//    }
//
//    @Test
//    @DisplayName("CountByStatut - Should return zero when no colis")
//    void testCountByStatut_Zero() {
//        when(colisRepository.countByStatut(StatutColis.CREE)).thenReturn(0L);
//
//        long result = colisService.countByStatut(StatutColis.CREE);
//
//        assertThat(result).isZero();
//    }
//
//    @Test
//    @DisplayName("CountByStatut - Should handle large count")
//    void testCountByStatut_LargeCount() {
//        when(colisRepository.countByStatut(StatutColis.LIVRE)).thenReturn(10000L);
//
//        long result = colisService.countByStatut(StatutColis.LIVRE);
//
//        assertThat(result).isEqualTo(10000L);
//    }
//
//
//}
