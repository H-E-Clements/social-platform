package com.example.reversiblecomputation.repositories;

import com.example.reversiblecomputation.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Procedure("pSetRoles")
    void setRole(
            @Param("pId") String id,
            @Param("pName") String name
    );

    @Procedure("pGetRoles")
    List<Role> getRoles(
            @Param("pId") String id,
            @Param("pName") String name
    );

    @Procedure("pDelRoles")
    void delRoles(
            @Param("pId") String id,
            @Param("pName") String name
    );
}