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
public interface ColisProduitRepository extends JpaRepository<ColisProduit, ColisProduitId> {

    List<ColisProduit> findByColis(Colis colis);

    @Query("SELECT cp FROM ColisProduit cp WHERE cp.colis.id = :colisId")
    List<ColisProduit> findByColisId(@Param("colisId") String colisId);

    @Query("SELECT cp FROM ColisProduit cp WHERE cp.produit.id = :produitId")
    List<ColisProduit> findByProduitId(@Param("produitId") String produitId);

    @Query("SELECT SUM(cp.quantite * cp.prix) FROM ColisProduit cp WHERE cp.colis.id = :colisId")
    Double calculateTotalPrixByColis(@Param("colisId") String colisId);

    @Query("SELECT SUM(cp.quantite * p.poids) FROM ColisProduit cp JOIN cp.produit p WHERE cp.colis.id = :colisId")
    Double calculateTotalPoidsByColis(@Param("colisId") String colisId);

    boolean existsById(ColisProduitId id);

}