package com.smartlogi.smartlogiv010.repository;


import com.smartlogi.smartlogiv010.entity.ClientExpediteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientExpediteurRepository extends JpaRepository<ClientExpediteur, String> {

    Optional<ClientExpediteur> findByEmail(String email);

    List<ClientExpediteur> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT c FROM ClientExpediteur c WHERE c.nom LIKE %:keyword% OR c.prenom LIKE %:keyword% OR c.email LIKE %:keyword%")
    ClientExpediteur searchByKeyword(@Param("keyword") String keyword);

    boolean existsByEmail(String email);
}
