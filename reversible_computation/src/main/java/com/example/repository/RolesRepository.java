package com.example.repository;

import com.example.domain.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepository extends JpaRepository<Roles, String> {

    Roles findByName(String name);
}
