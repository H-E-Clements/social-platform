package com.example.reversiblecomputation.repositories;

import com.example.reversiblecomputation.models.User;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    @Procedure("pSetUsers")
    void setUser(
            @Param("pId") String id,
            @Param("pUsername") String username,
            @Param("pPassword") String password,
            @Param("pFullName") String fullName,
            @Param("pFirstName") String firstName,
            @Param("pLastName") String lastName,
            @Param("pEmailAddress") String emailAddress,
            @Param("pRoleId") String roleId
    );

    @Procedure("pGetUsers")
    List<User> getUsers(
            @Param("pId") String id,
            @Param("pUsername") String username,
            @Param("pPassword") String password,
            @Param("pFullName") String fullName,
            @Param("pFirstName") String firstName,
            @Param("pLastName") String lastName,
            @Param("pEmailAddress") String emailAddress,
            @Param("pRoleId") String roleId
    );

    @Procedure("pDelUsers")
    void delUsers(
            @Param("pId") String id,
            @Param("pUsername") String username,
            @Param("pPassword") String password,
            @Param("pFullName") String fullName,
            @Param("pFirstName") String firstName,
            @Param("pLastName") String lastName,
            @Param("pEmailAddress") String emailAddress,
            @Param("pRoleId") String roleId
    );
}
