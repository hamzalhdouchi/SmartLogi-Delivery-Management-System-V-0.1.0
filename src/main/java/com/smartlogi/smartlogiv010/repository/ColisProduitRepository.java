package com.smartlogi.smartlogiv010.repository;

import com.smartlogi.smartlogiv010.entity.Colis;
import com.smartlogi.smartlogiv010.entity.ColisProduit;
import com.smartlogi.smartlogiv010.entity.ColisProduitId; // ✅ Importer ColisProduitId
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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