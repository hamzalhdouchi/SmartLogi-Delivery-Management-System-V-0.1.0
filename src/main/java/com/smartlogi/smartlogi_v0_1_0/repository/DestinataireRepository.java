package com.smartlogi.smartlogi_v0_1_0.repository;


import com.smartlogi.smartlogi_v0_1_0.entity.Destinataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinataireRepository extends JpaRepository<Destinataire, String> {

    Optional<Destinataire> findByEmail(String email);

    List<Destinataire> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT d FROM Destinataire d WHERE d.nom LIKE %:keyword% OR d.prenom LIKE %:keyword% OR d.email LIKE %:keyword%")
    List<Destinataire> searchByKeyword(@Param("keyword") String keyword);

    boolean existsDestinataireByEmail(String email);
}