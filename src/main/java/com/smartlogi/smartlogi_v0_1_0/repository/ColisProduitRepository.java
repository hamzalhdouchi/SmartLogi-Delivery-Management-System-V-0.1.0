package com.smartlogi.smartlogi_v0_1_0.repository;

import com.smartlogi.smartlogi_v0_1_0.entity.Colis;
import com.smartlogi.smartlogi_v0_1_0.entity.ColisProduit;
import com.smartlogi.smartlogi_v0_1_0.entity.ColisProduitId; // ✅ Importer ColisProduitId
import com.smartlogi.smartlogi_v0_1_0.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ColisProduitRepository extends JpaRepository<ColisProduit, ColisProduitId> { // ✅ Changer String par ColisProduitId

    List<ColisProduit> findByColis(Colis colis);

    List<ColisProduit> findByProduit(Produit produit);

    @Query("SELECT cp FROM ColisProduit cp WHERE cp.colis.id = :colisId")
    List<ColisProduit> findByColisId(@Param("colisId") String colisId);

    @Query("SELECT cp FROM ColisProduit cp WHERE cp.produit.id = :produitId")
    List<ColisProduit> findByProduitId(@Param("produitId") String produitId);

    @Query("SELECT SUM(cp.quantite * cp.prix) FROM ColisProduit cp WHERE cp.colis.id = :colisId")
    Double calculateTotalPrixByColis(@Param("colisId") String colisId);

    @Query("SELECT SUM(cp.quantite * p.poids) FROM ColisProduit cp JOIN cp.produit p WHERE cp.colis.id = :colisId")
    Double calculateTotalPoidsByColis(@Param("colisId") String colisId);

    // ✅ CORRIGER cette méthode - utiliser existsById au lieu de existsByColisProduitId
    boolean existsById(ColisProduitId id);

    void deleteByColis(Colis colis);

    // ✅ AJOUTER cette méthode utilitaire
    @Query("SELECT CASE WHEN COUNT(cp) > 0 THEN true ELSE false END FROM ColisProduit cp WHERE cp.colis.id = :colisId AND cp.produit.id = :produitId")
    boolean existsByColisIdAndProduitId(@Param("colisId") String colisId, @Param("produitId") String produitId);

    // ✅ AJOUTER cette méthode pour trouver par colis et produit
    @Query("SELECT cp FROM ColisProduit cp WHERE cp.colis.id = :colisId AND cp.produit.id = :produitId")
    Optional<ColisProduit> findByColisIdAndProduitId(@Param("colisId") String colisId, @Param("produitId") String produitId);
}