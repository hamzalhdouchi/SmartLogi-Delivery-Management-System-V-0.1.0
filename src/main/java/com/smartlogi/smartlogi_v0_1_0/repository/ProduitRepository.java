package com.smartlogi.smartlogi_v0_1_0.repository;


import com.smartlogi.smartlogi_v0_1_0.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, String> {

    List<Produit> findByNomContainingIgnoreCase(String nom);

    List<Produit> findByCategorie(String categorie);

    @Query("SELECT p FROM Produit p WHERE p.nom LIKE %:keyword% OR p.categorie LIKE %:keyword%")
    List<Produit> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT DISTINCT p.categorie FROM Produit p WHERE p.categorie IS NOT NULL")
    List<String> findAllCategories();

    List<Produit> findByPrixBetween(BigDecimal prixMin, BigDecimal prixMax);

    boolean existsByNomContainingIgnoreCase(String nom);
}
