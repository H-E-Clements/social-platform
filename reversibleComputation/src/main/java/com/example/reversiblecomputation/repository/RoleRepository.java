package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}