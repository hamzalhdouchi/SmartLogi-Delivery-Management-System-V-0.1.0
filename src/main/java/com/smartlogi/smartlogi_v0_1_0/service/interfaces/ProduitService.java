package com.smartlogi.smartlogi_v0_1_0.service.interfaces;


import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.createDTO.ProduitCreateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.requestDTO.updateDTO.ProduitUpdateRequestDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitAdvancedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitDetailedResponseDto;
import com.smartlogi.smartlogi_v0_1_0.dto.responseDTO.Produit.ProduitSimpleResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProduitService {

    // === CRUD Operations ===
    ProduitSimpleResponseDto create(ProduitCreateRequestDto requestDto);
    ProduitSimpleResponseDto update(Long id, ProduitUpdateRequestDto requestDto);
    ProduitSimpleResponseDto getById(Long id);
    void delete(Long id);
    boolean existsById(Long id);

    // === Advanced Queries ===
    ProduitAdvancedResponseDto getByIdWithStats(Long id);
    ProduitDetailedResponseDto getByIdWithColis(Long id);

    // === Listing & Pagination ===
    Page<ProduitSimpleResponseDto> getAll(Pageable pageable);
    List<ProduitSimpleResponseDto> getAll();

    // === Filtering & Search ===
    List<ProduitSimpleResponseDto> searchByNom(String nom);
    List<ProduitSimpleResponseDto> getByCategorie(String categorie);
    List<ProduitSimpleResponseDto> searchByKeyword(String keyword);
    List<ProduitSimpleResponseDto> getByPrixBetween(BigDecimal prixMin, BigDecimal prixMax);

    // === Categories Management ===
    List<String> getAllCategories();
}
