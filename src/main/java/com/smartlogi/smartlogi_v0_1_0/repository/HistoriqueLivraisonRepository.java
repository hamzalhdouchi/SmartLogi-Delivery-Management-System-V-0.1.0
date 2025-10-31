package com.smartlogi.smartlogi_v0_1_0.repository;

import com.smartlogi.smartlogi_v0_1_0.entity.Colis;
import com.smartlogi.smartlogi_v0_1_0.entity.HistoriqueLivraison;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoriqueLivraisonRepository extends JpaRepository<HistoriqueLivraison, String> {

    List<HistoriqueLivraison> findByColis(Colis colis);

    List<HistoriqueLivraison> findByColisOrderByDateChangementAsc(Colis colis);

    List<HistoriqueLivraison> findByColisOrderByDateChangementDesc(Colis colis);

    List<HistoriqueLivraison> findByStatut(StatutColis statut);

    @Query("SELECT h FROM HistoriqueLivraison h WHERE h.colis.id = :colisId ORDER BY h.dateChangement DESC")
    List<HistoriqueLivraison> findByColisIdOrderByDateChangementDesc(@Param("colisId") Long colisId);

    @Query("SELECT h FROM HistoriqueLivraison h WHERE h.dateChangement BETWEEN :startDate AND :endDate")
    List<HistoriqueLivraison> findByDateChangementBetween(@Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT h FROM HistoriqueLivraison h WHERE h.colis.id = :colisId AND h.statut = :statut")
    List<HistoriqueLivraison> findByColisIdAndStatut(@Param("colisId") Long colisId, @Param("statut") StatutColis statut);
}
