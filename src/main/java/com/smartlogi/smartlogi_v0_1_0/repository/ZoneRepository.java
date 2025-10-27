package com.smartlogi.smartlogi_v0_1_0.repository;


import com.smartlogi.smartlogi_v0_1_0.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    Optional<Zone> findByCodePostal(String codePostal);

    Optional<Zone> findByNom(String nom);

    List<Zone> findByNomContainingIgnoreCase(String nom);

    @Query("SELECT z FROM Zone z WHERE z.nom LIKE %:keyword% OR z.codePostal LIKE %:keyword%")
    List<Zone> searchByKeyword(@Param("keyword") String keyword);

    boolean existsByCodePostal(String codePostal);
}
