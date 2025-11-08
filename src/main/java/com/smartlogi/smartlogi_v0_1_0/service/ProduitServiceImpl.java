package com.smartlogi.smartlogi_v0_1_0.service;

import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ProduitCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ProduitUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitSimpleResponseDto;
import com.smartlogi.smartlogi_v0_1_0.entity.Produit;
import com.smartlogi.smartlogi_v0_1_0.mapper.SmartLogiMapper;
import com.smartlogi.smartlogi_v0_1_0.repository.ProduitRepository;
import com.smartlogi.smartlogi_v0_1_0.service.interfaces.ProduitService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class    ProduitServiceImpl implements ProduitService {

    private final ProduitRepository produitRepository;
    private final SmartLogiMapper smartLogiMapper;

    @Override
    public ProduitSimpleResponseDto create(ProduitCreateRequestDto requestDto) {
        if (produitRepository.findByNomContainingIgnoreCase(requestDto.getNom()).stream()
                .anyMatch(p -> p.getNom().equalsIgnoreCase(requestDto.getNom()))) {
            throw new RuntimeException("Un produit avec ce nom existe déjà");
        }

        Produit produit = smartLogiMapper.toEntity(requestDto);
        Produit savedProduit = produitRepository.save(produit);
        return smartLogiMapper.toSimpleResponseDto(savedProduit);
    }

    @Override
    public ProduitSimpleResponseDto update(String id, ProduitUpdateRequestDto requestDto) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        // Vérifier l'unicité du nom si modifié
        if (requestDto.getNom() != null &&
                !requestDto.getNom().equals(produit.getNom()) &&
                produitRepository.findByNomContainingIgnoreCase(requestDto.getNom()).stream()
                        .anyMatch(p -> p.getNom().equalsIgnoreCase(requestDto.getNom()))) {
            throw new RuntimeException("Un produit avec ce nom existe déjà");
        }

        smartLogiMapper.updateEntityFromDto(requestDto, produit);
        Produit updatedProduit = produitRepository.save(produit);
        return smartLogiMapper.toSimpleResponseDto(updatedProduit);
    }

    @Override
    public ProduitSimpleResponseDto getById(String id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        return smartLogiMapper.toSimpleResponseDto(produit);
    }

    @Override
    public ProduitAdvancedResponseDto getByIdWithStats(String id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        return smartLogiMapper.toAdvancedResponseDto(produit);
    }

    @Override
    public ProduitDetailedResponseDto getByIdWithColis(String id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        return smartLogiMapper.toDetailedResponseDto(produit);
    }

    @Override
    public Page<ProduitSimpleResponseDto> getAll(Pageable pageable) {
        return produitRepository.findAll(pageable)
                .map(smartLogiMapper::toSimpleResponseDto);
    }

    @Override
    public List<ProduitSimpleResponseDto> getAll() {
        return produitRepository.findAll()
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProduitSimpleResponseDto> searchByNom(String nom) {
        return produitRepository.findByNomContainingIgnoreCase(nom)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProduitSimpleResponseDto> getByCategorie(String categorie) {
        return produitRepository.findByCategorie(categorie)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProduitSimpleResponseDto> searchByKeyword(String keyword) {
        return produitRepository.searchByKeyword(keyword)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProduitSimpleResponseDto> getByPrixBetween(BigDecimal prixMin, BigDecimal prixMax) {
        // Validation des prix
        if (prixMin == null || prixMax == null) {
            throw new RuntimeException("Les prix minimum et maximum sont obligatoires");
        }

        if (prixMin.compareTo(prixMax) > 0) {
            throw new RuntimeException("Le prix minimum ne peut pas être supérieur au prix maximum");
        }

        if (prixMin.compareTo(BigDecimal.ZERO) < 0 || prixMax.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Les prix ne peuvent pas être négatifs");
        }

        return produitRepository.findByPrixBetween(prixMin, prixMax)
                .stream()
                .map(smartLogiMapper::toSimpleResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllCategories() {
        return produitRepository.findAllCategories();
    }

    @Override
    public void delete(String id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        if (!produit.getColisProduits().isEmpty()) {
            throw new RuntimeException("Impossible de supprimer le produit : il est associé à des colis");
        }

        produitRepository.delete(produit);
    }

    @Override
    public boolean existsById(String id) {
        return produitRepository.existsById(id);
    }

}
