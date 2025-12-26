package com.smartlogi.smartlogiv010.repository;

import com.smartlogi.smartlogiv010.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(String name);
}
