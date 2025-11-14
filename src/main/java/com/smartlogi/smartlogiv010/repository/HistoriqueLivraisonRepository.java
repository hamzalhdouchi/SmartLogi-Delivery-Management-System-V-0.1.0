package com.smartlogi.smartlogiv010.repository;

import com.smartlogi.smartlogiv010.entity.Colis;
import com.smartlogi.smartlogiv010.entity.HistoriqueLivraison;
import com.smartlogi.smartlogiv010.enums.StatutColis;
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
    List<HistoriqueLivraison> findByColisIdOrderByDateChangementDesc(@Param("colisId") String colisId);

    @Query("SELECT h FROM HistoriqueLivraison h WHERE h.dateChangement BETWEEN :startDate AND :endDate")
    List<HistoriqueLivraison> findByDateChangementBetween(@Param("startDate") LocalDateTime startDate,
                                                          @Param("endDate") LocalDateTime endDate);

    @Query("SELECT h FROM HistoriqueLivraison h WHERE h.colis.id = :colisId AND h.statut = :statut")
    List<HistoriqueLivraison> findByColisIdAndStatut(@Param("colisId") String colisId, @Param("statut") StatutColis statut);
}
