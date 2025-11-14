package com.smartlogi.smartlogiv010.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatistiquesRepository {

    @Query("SELECT c.statut, COUNT(c) FROM Colis c GROUP BY c.statut")
    List<Object[]> getStatistiquesParStatut();

    @Query("SELECT c.zone.nom, COUNT(c) FROM Colis c GROUP BY c.zone.nom")
    List<Object[]> getStatistiquesParZone();

    @Query("SELECT c.priorite, COUNT(c) FROM Colis c GROUP BY c.priorite")
    List<Object[]> getStatistiquesParPriorite();

    @Query("SELECT c.villeDestination, COUNT(c) FROM Colis c GROUP BY c.villeDestination")
    List<Object[]> getStatistiquesParVille();

    @Query("SELECT DATE(c.dateCreation), COUNT(c) FROM Colis c WHERE c.dateCreation BETWEEN :startDate AND :endDate GROUP BY DATE(c.dateCreation)")
    List<Object[]> getStatistiquesParDate(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT l.nom, COUNT(c) FROM Colis c JOIN c.livreur l GROUP BY l.nom")
    List<Object[]> getStatistiquesParLivreur();
}
