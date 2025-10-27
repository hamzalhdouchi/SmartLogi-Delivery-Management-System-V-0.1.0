package com.smartlogi.smartlogi_v0_1_0.repository;

import com.smartlogi.smartlogi_v0_1_0.entity.Colis;
import com.smartlogi.smartlogi_v0_1_0.entity.ColisProduit;
import com.smartlogi.smartlogi_v0_1_0.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColisProduitRepository extends JpaRepository<ColisProduit, Long> {

    List<ColisProduit> findByColis(Colis colis);

    List<ColisProduit> findByProduit(Produit produit);

    @Query("SELECT cp FROM ColisProduit cp WHERE cp.colis.id = :colisId")
    List<ColisProduit> findByColisId(@Param("colisId") Long colisId);

    @Query("SELECT cp FROM ColisProduit cp WHERE cp.produit.id = :produitId")
    List<ColisProduit> findByProduitId(@Param("produitId") Long produitId);

    @Query("SELECT SUM(cp.quantite * cp.prix) FROM ColisProduit cp WHERE cp.colis.id = :colisId")
    Double calculateTotalPrixByColis(@Param("colisId") Long colisId);

    @Query("SELECT SUM(cp.quantite * p.poids) FROM ColisProduit cp JOIN cp.produit p WHERE cp.colis.id = :colisId")
    Double calculateTotalPoidsByColis(@Param("colisId") Long colisId);

    void deleteByColis(Colis colis);
}