package com.smartlogi.smartlogi_v0_1_0.repository;


import com.smartlogi.smartlogi_v0_1_0.entity.Livreur;
import com.smartlogi.smartlogi_v0_1_0.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, String> {

    List<Livreur> findByZone(Zone zone);

    List<Livreur> findByNomContainingIgnoreCase(String nom);

    Optional<Livreur> findByTelephone(String telephone);

    @Query("SELECT l FROM Livreur l WHERE l.nom LIKE %:keyword% OR l.prenom LIKE %:keyword% OR l.telephone LIKE %:keyword%")
    List<Livreur> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT l FROM Livreur l WHERE l.zone.id = :zoneId")
    List<Livreur> findByZoneId(@Param("zoneId") String zoneId);

    long countByZone(Zone zone);

    boolean existsByTelephone(String telephone);
}