package com.smartlogi.smartlogi_v0_1_0.repository;


import com.smartlogi.smartlogi_v0_1_0.entity.Colis;
import com.smartlogi.smartlogi_v0_1_0.entity.Livreur;
import com.smartlogi.smartlogi_v0_1_0.entity.Zone;
import com.smartlogi.smartlogi_v0_1_0.enums.Priorite;
import com.smartlogi.smartlogi_v0_1_0.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ColisRepository extends JpaRepository<Colis, String> {

    // Filtrage par statut
    List<Colis> findByStatut(StatutColis statut);
    Page<Colis> findByStatut(StatutColis statut, Pageable pageable);

    // Filtrage par priorité
    List<Colis> findByPriorite(Priorite priorite);
    List<Colis> findByPrioriteAndStatut(Priorite priorite, StatutColis statut);

    // Filtrage par livreur
    List<Colis> findByLivreur(Livreur livreur);
    List<Colis> findByLivreurAndStatut(Livreur livreur, StatutColis statut);

    // Filtrage par zone
    List<Colis> findByZone(Zone zone);
    List<Colis> findByZoneAndStatut(Zone zone, StatutColis statut);

    // Filtrage par ville
    List<Colis> findByVilleDestination(String villeDestination);
    List<Colis> findByVilleDestinationAndStatut(String villeDestination, StatutColis statut);

    // Recherche avancée
    @Query("SELECT c FROM Colis c WHERE " +
            "c.description LIKE %:keyword% OR " +
            "c.villeDestination LIKE %:keyword% OR " +
            "c.clientExpediteur.nom LIKE %:keyword% OR " +
            "c.destinataire.nom LIKE %:keyword%")
    List<Colis> searchByKeyword(@Param("keyword") String keyword);

    // Statistiques
    @Query("SELECT COUNT(c) FROM Colis c WHERE c.statut = :statut")
    long countByStatut(@Param("statut") StatutColis statut);

    @Query("SELECT COUNT(c) FROM Colis c WHERE c.zone.id = :zoneId AND c.statut = :statut")
    long countByZoneAndStatut(@Param("zoneId") Long zoneId, @Param("statut") StatutColis statut);

    @Query("SELECT COUNT(c) FROM Colis c WHERE c.livreur.id = :livreurId AND c.statut = :statut")
    long countByLivreurAndStatut(@Param("livreurId") Long livreurId, @Param("statut") StatutColis statut);

    // Colis en retard (créés il y a plus de 2 jours et non livrés)
    @Query("SELECT c FROM Colis c WHERE c.dateCreation < :dateLimite AND c.statut NOT IN ('LIVRÉ')")
    List<Colis> findColisEnRetard(@Param("dateLimite") LocalDateTime dateLimite);

    // Poids total par livreur
    @Query("SELECT c.livreur, SUM(c.poids) FROM Colis c WHERE c.livreur IS NOT NULL GROUP BY c.livreur")
    List<Object[]> findPoidsTotalParLivreur();

    // Colis par période
    @Query("SELECT c FROM Colis c WHERE c.dateCreation BETWEEN :startDate AND :endDate")
    List<Colis> findByDateCreationBetween(@Param("startDate") LocalDateTime startDate,
                                          @Param("endDate") LocalDateTime endDate);

    // Pour la pagination
    Page<Colis> findAll(Pageable pageable);
    Page<Colis> findByZoneId(Long zoneId, Pageable pageable);
}
