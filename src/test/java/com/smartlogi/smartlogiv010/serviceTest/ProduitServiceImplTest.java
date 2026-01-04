package com.smartlogi.smartlogiv010.serviceTest;

import com.smartlogi.smartlogiv010.dto.requestDTO.createDTO.ProduitCreateRequestDto;
import com.smartlogi.smartlogiv010.dto.requestDTO.updateDTO.ProduitUpdateRequestDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Produit.ProduitAdvancedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Produit.ProduitDetailedResponseDto;
import com.smartlogi.smartlogiv010.dto.responseDTO.Produit.ProduitSimpleResponseDto;
import com.smartlogi.smartlogiv010.entity.ColisProduit;
import com.smartlogi.smartlogiv010.entity.Produit;
import com.smartlogi.smartlogiv010.mapper.SmartLogiMapper;
import com.smartlogi.smartlogiv010.repository.ProduitRepository;
import com.smartlogi.smartlogiv010.service.ProduitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests unitaires pour ProduitServiceImpl")
class ProduitServiceImplTest {

    @Mock
    private ProduitRepository produitRepository;

    @Mock
    private SmartLogiMapper smartLogiMapper;

    @InjectMocks
    private ProduitServiceImpl produitService;

    private Produit produit;
    private ProduitCreateRequestDto produitCreateRequestDto;
    private ProduitUpdateRequestDto produitUpdateRequestDto;
    private ProduitSimpleResponseDto produitSimpleResponseDto;
    private ProduitAdvancedResponseDto produitAdvancedResponseDto;
    private ProduitDetailedResponseDto produitDetailedResponseDto;

    @BeforeEach
    void setUp() {
        produit = new Produit();
        produit.setId("produit-1");
        produit.setNom("Smartphone");
        produit.setCategorie("Électronique");
        produit.setPrix(BigDecimal.valueOf(599.99));
        produit.setPoids(BigDecimal.valueOf(0.5));

        produitCreateRequestDto = new ProduitCreateRequestDto();
        produitCreateRequestDto.setNom("Smartphone");
        produitCreateRequestDto.setCategorie("Électronique");
        produitCreateRequestDto.setPrix(BigDecimal.valueOf(599.99));
        produitCreateRequestDto.setPoids(BigDecimal.valueOf(0.5));

        produitUpdateRequestDto = new ProduitUpdateRequestDto();
        produitUpdateRequestDto.setNom("Smartphone Pro");
        produitUpdateRequestDto.setPrix(BigDecimal.valueOf(699.99));

        produitSimpleResponseDto = new ProduitSimpleResponseDto();
        produitSimpleResponseDto.setId("produit-1");
        produitSimpleResponseDto.setNom("Smartphone");

        produitAdvancedResponseDto = new ProduitAdvancedResponseDto();
        produitAdvancedResponseDto.setId("produit-1");

        produitDetailedResponseDto = new ProduitDetailedResponseDto();
        produitDetailedResponseDto.setId("produit-1");
    }

    @Test
    @DisplayName("Devrait créer un produit avec succès")
    void testCreate_Success() {
        when(produitRepository.findByNomContainingIgnoreCase("Smartphone"))
                .thenReturn(Collections.emptyList());
        when(smartLogiMapper.toEntity(produitCreateRequestDto)).thenReturn(produit);
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        when(smartLogiMapper.toSimpleResponseDto(produit)).thenReturn(produitSimpleResponseDto);

        ProduitSimpleResponseDto result = produitService.create(produitCreateRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("produit-1");
        assertThat(result.getNom()).isEqualTo("Smartphone");
        verify(produitRepository, times(1)).save(any(Produit.class));
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si produit avec nom existant")
    void testCreate_DuplicateName() {
        Produit existingProduit = new Produit();
        existingProduit.setNom("Smartphone");

        when(produitRepository.findByNomContainingIgnoreCase("Smartphone"))
                .thenReturn(Arrays.asList(existingProduit));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.create(produitCreateRequestDto));

        assertThat(exception.getMessage()).contains("Un produit avec ce nom existe déjà");
        verify(produitRepository, never()).save(any(Produit.class));
    }

    @Test
    @DisplayName("Devrait créer un produit avec nom similaire mais pas identique")
    void testCreate_SimilarButNotIdenticalName() {
        Produit existingProduit = new Produit();
        existingProduit.setNom("Smartphone Pro");

        when(produitRepository.findByNomContainingIgnoreCase("Smartphone"))
                .thenReturn(Arrays.asList(existingProduit));
        when(smartLogiMapper.toEntity(produitCreateRequestDto)).thenReturn(produit);
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        when(smartLogiMapper.toSimpleResponseDto(produit)).thenReturn(produitSimpleResponseDto);

        ProduitSimpleResponseDto result = produitService.create(produitCreateRequestDto);

        assertThat(result).isNotNull();
        verify(produitRepository, times(1)).save(any(Produit.class));
    }

    @Test
    @DisplayName("Devrait mettre à jour un produit avec succès")
    void testUpdate_Success() {
        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        when(produitRepository.findByNomContainingIgnoreCase("Smartphone Pro"))
                .thenReturn(Collections.emptyList());
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        when(smartLogiMapper.toSimpleResponseDto(produit)).thenReturn(produitSimpleResponseDto);
        doNothing().when(smartLogiMapper).updateEntityFromDto(produitUpdateRequestDto, produit);

        ProduitSimpleResponseDto result = produitService.update("produit-1", produitUpdateRequestDto);

        assertThat(result).isNotNull();
        verify(produitRepository, times(1)).save(produit);
        verify(smartLogiMapper, times(1)).updateEntityFromDto(produitUpdateRequestDto, produit);
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si produit non trouvé lors de la mise à jour")
    void testUpdate_NotFound() {
        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.update("produit-999", produitUpdateRequestDto));

        assertThat(exception.getMessage()).contains("Produit non trouvé");
        verify(produitRepository, never()).save(any(Produit.class));
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si nouveau nom existe déjà")
    void testUpdate_DuplicateName() {
        Produit existingProduit = new Produit();
        existingProduit.setId("produit-2");
        existingProduit.setNom("Smartphone Pro");

        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        when(produitRepository.findByNomContainingIgnoreCase("Smartphone Pro"))
                .thenReturn(Arrays.asList(existingProduit));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.update("produit-1", produitUpdateRequestDto));

        assertThat(exception.getMessage()).contains("Un produit avec ce nom existe déjà");
    }

    @Test
    @DisplayName("Devrait mettre à jour sans vérifier le nom si nom non modifié")
    void testUpdate_SameNameNoCheck() {
        produitUpdateRequestDto.setNom(null);

        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        when(produitRepository.save(any(Produit.class))).thenReturn(produit);
        when(smartLogiMapper.toSimpleResponseDto(produit)).thenReturn(produitSimpleResponseDto);
        doNothing().when(smartLogiMapper).updateEntityFromDto(produitUpdateRequestDto, produit);

        ProduitSimpleResponseDto result = produitService.update("produit-1", produitUpdateRequestDto);

        assertThat(result).isNotNull();
        verify(produitRepository, never()).findByNomContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("Devrait récupérer un produit par ID")
    void testGetById_Success() {
        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        when(smartLogiMapper.toSimpleResponseDto(produit)).thenReturn(produitSimpleResponseDto);

        ProduitSimpleResponseDto result = produitService.getById("produit-1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("produit-1");
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si produit non trouvé")
    void testGetById_NotFound() {
        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.getById("produit-999"));

        assertThat(exception.getMessage()).contains("Produit non trouvé");
    }

    @Test
    @DisplayName("Devrait récupérer un produit avec statistiques")
    void testGetByIdWithStats_Success() {
        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        when(smartLogiMapper.toAdvancedResponseDto(produit)).thenReturn(produitAdvancedResponseDto);

        ProduitAdvancedResponseDto result = produitService.getByIdWithStats("produit-1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("produit-1");
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si produit non trouvé avec stats")
    void testGetByIdWithStats_NotFound() {
        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> produitService.getByIdWithStats("produit-999"));
    }

    @Test
    @DisplayName("Devrait récupérer un produit avec colis associés")
    void testGetByIdWithColis_Success() {
        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        when(smartLogiMapper.toDetailedResponseDto(produit)).thenReturn(produitDetailedResponseDto);

        ProduitDetailedResponseDto result = produitService.getByIdWithColis("produit-1");

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("produit-1");
    }

    @Test
    @DisplayName("Devrait récupérer tous les produits paginés")
    void testGetAllPaginated_Success() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Produit> produits = Arrays.asList(produit);
        Page<Produit> produitPage = new PageImpl<>(produits, pageable, 1);

        when(produitRepository.findAll(pageable)).thenReturn(produitPage);
        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class)))
                .thenReturn(produitSimpleResponseDto);

        Page<ProduitSimpleResponseDto> result = produitService.getAll(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    @DisplayName("Devrait récupérer tous les produits sous forme de liste")
    void testGetAllList_Success() {
        List<Produit> produits = Arrays.asList(produit);
        when(produitRepository.findAll()).thenReturn(produits);
        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class)))
                .thenReturn(produitSimpleResponseDto);

        List<ProduitSimpleResponseDto> result = produitService.getAll();

        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Devrait retourner une liste vide si aucun produit")
    void testGetAllList_Empty() {
        when(produitRepository.findAll()).thenReturn(Collections.emptyList());

        List<ProduitSimpleResponseDto> result = produitService.getAll();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Devrait rechercher des produits par nom")
    void testSearchByNom_Success() {
        List<Produit> produits = Arrays.asList(produit);
        when(produitRepository.findByNomContainingIgnoreCase("Smart"))
                .thenReturn(produits);
        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class)))
                .thenReturn(produitSimpleResponseDto);

        List<ProduitSimpleResponseDto> result = produitService.searchByNom("Smart");

        assertThat(result).hasSize(1);
        verify(produitRepository, times(1)).findByNomContainingIgnoreCase("Smart");
    }

    @Test
    @DisplayName("Devrait retourner une liste vide si aucun produit trouvé par nom")
    void testSearchByNom_Empty() {
        when(produitRepository.findByNomContainingIgnoreCase("NonExistant"))
                .thenReturn(Collections.emptyList());

        List<ProduitSimpleResponseDto> result = produitService.searchByNom("NonExistant");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Devrait récupérer les produits par catégorie")
    void testGetByCategorie_Success() {
        List<Produit> produits = Arrays.asList(produit);
        when(produitRepository.findByCategorie("Électronique")).thenReturn(produits);
        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class)))
                .thenReturn(produitSimpleResponseDto);

        List<ProduitSimpleResponseDto> result = produitService.getByCategorie("Électronique");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Devrait rechercher des produits par mot-clé")
    void testSearchByKeyword_Success() {
        List<Produit> produits = Arrays.asList(produit);
        when(produitRepository.searchByKeyword("phone")).thenReturn(produits);
        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class)))
                .thenReturn(produitSimpleResponseDto);

        List<ProduitSimpleResponseDto> result = produitService.searchByKeyword("phone");

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Devrait récupérer les produits dans une fourchette de prix")
    void testGetByPrixBetween_Success() {
        BigDecimal prixMin = BigDecimal.valueOf(500);
        BigDecimal prixMax = BigDecimal.valueOf(700);
        List<Produit> produits = Arrays.asList(produit);

        when(produitRepository.findByPrixBetween(prixMin, prixMax)).thenReturn(produits);
        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class)))
                .thenReturn(produitSimpleResponseDto);

        List<ProduitSimpleResponseDto> result = produitService.getByPrixBetween(prixMin, prixMax);

        assertThat(result).hasSize(1);
        verify(produitRepository, times(1)).findByPrixBetween(prixMin, prixMax);
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si prix minimum null")
    void testGetByPrixBetween_NullMinPrice() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.getByPrixBetween(null, BigDecimal.valueOf(700)));

        assertThat(exception.getMessage())
                .contains("Les prix minimum et maximum sont obligatoires");
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si prix maximum null")
    void testGetByPrixBetween_NullMaxPrice() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.getByPrixBetween(BigDecimal.valueOf(500), null));

        assertThat(exception.getMessage())
                .contains("Les prix minimum et maximum sont obligatoires");
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si prix minimum > prix maximum")
    void testGetByPrixBetween_MinGreaterThanMax() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.getByPrixBetween(
                        BigDecimal.valueOf(700),
                        BigDecimal.valueOf(500)
                ));

        assertThat(exception.getMessage())
                .contains("Le prix minimum ne peut pas être supérieur au prix maximum");
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si prix minimum négatif")
    void testGetByPrixBetween_NegativeMinPrice() {
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.getByPrixBetween(
                        BigDecimal.valueOf(-100),
                        BigDecimal.valueOf(500)
                ));

        assertThat(exception.getMessage())
                .contains("Les prix ne peuvent pas être négatifs");
    }


    @Test
    @DisplayName("Devrait accepter prix minimum égal à prix maximum")
    void testGetByPrixBetween_EqualPrices() {
        BigDecimal prix = BigDecimal.valueOf(599.99);
        when(produitRepository.findByPrixBetween(prix, prix))
                .thenReturn(Arrays.asList(produit));
        when(smartLogiMapper.toSimpleResponseDto(any(Produit.class)))
                .thenReturn(produitSimpleResponseDto);

        List<ProduitSimpleResponseDto> result = produitService.getByPrixBetween(prix, prix);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Devrait récupérer toutes les catégories")
    void testGetAllCategories_Success() {
        List<String> categories = Arrays.asList("Électronique", "Vêtements", "Alimentation");
        when(produitRepository.findAllCategories()).thenReturn(categories);

        List<String> result = produitService.getAllCategories();

        assertThat(result).hasSize(3);
        assertThat(result).contains("Électronique", "Vêtements", "Alimentation");
    }

    @Test
    @DisplayName("Devrait retourner une liste vide si aucune catégorie")
    void testGetAllCategories_Empty() {
        when(produitRepository.findAllCategories()).thenReturn(Collections.emptyList());

        List<String> result = produitService.getAllCategories();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Devrait supprimer un produit avec succès")
    void testDelete_Success() {
        produit.setColisProduits(Collections.emptyList());
        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));
        doNothing().when(produitRepository).delete(produit);

        produitService.delete("produit-1");

        verify(produitRepository, times(1)).delete(produit);
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si produit non trouvé lors de la suppression")
    void testDelete_NotFound() {
        when(produitRepository.findById("produit-999")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.delete("produit-999"));

        assertThat(exception.getMessage()).contains("Produit non trouvé");
        verify(produitRepository, never()).delete(any(Produit.class));
    }

    @Test
    @DisplayName("Devrait lancer RuntimeException si produit associé à des colis")
    void testDelete_HasAssociatedColis() {
        ColisProduit colisProduit = new ColisProduit();
        produit.setColisProduits(Arrays.asList(colisProduit));

        when(produitRepository.findById("produit-1")).thenReturn(Optional.of(produit));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> produitService.delete("produit-1"));

        assertThat(exception.getMessage())
                .contains("Impossible de supprimer le produit : il est associé à des colis");
        verify(produitRepository, never()).delete(any(Produit.class));
    }

    @Test
    @DisplayName("Devrait retourner true si le produit existe")
    void testExistsById_True() {
        when(produitRepository.existsById("produit-1")).thenReturn(true);

        boolean result = produitService.existsById("produit-1");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Devrait retourner false si le produit n'existe pas")
    void testExistsById_False() {
        when(produitRepository.existsById("produit-999")).thenReturn(false);

        boolean result = produitService.existsById("produit-999");

        assertThat(result).isFalse();
    }
}
