package com.smartlogi.smartlogiv010.repository;


import com.smartlogi.smartlogiv010.entity.Destinataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DestinataireRepository extends JpaRepository<Destinataire, String> {

    List<Destinataire> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT d FROM Destinataire d WHERE d.nom LIKE %:keyword% OR d.prenom LIKE %:keyword% OR d.email LIKE %:keyword%")
    Destinataire searchByKeyword(@Param("keyword") String keyword);

    boolean existsDestinataireByEmail(String email);
}