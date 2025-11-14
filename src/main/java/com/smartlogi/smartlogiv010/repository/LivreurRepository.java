package com.smartlogi.smartlogiv010.repository;


import com.smartlogi.smartlogiv010.entity.Livreur;
import com.smartlogi.smartlogiv010.entity.Zone;
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

    long countByZone(Zone zone);

    boolean existsByTelephone(String telephone);
}