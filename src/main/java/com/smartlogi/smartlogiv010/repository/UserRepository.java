package com.smartlogi.smartlogiv010.repository;

import com.smartlogi.smartlogiv010.entity.*;
import com.smartlogi.smartlogiv010.enums.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    @Query("SELECT U FROM User U WHERE U.nom LIKE %:keyword% OR U.prenom LIKE %:keyword% OR U.email LIKE %:keyword%")
    User searchByKeyword(@Param("keyword") String keyword);
    boolean existsByRole(Role role);
    long countByRole(Role role);
    boolean existsByEmail(String email);
    User findByNomContainingIgnoreCase(String nom);
    List<User> findByZone(Zone zone);
}
