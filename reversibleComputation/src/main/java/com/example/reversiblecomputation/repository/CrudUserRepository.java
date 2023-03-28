package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Paper;
import com.example.reversiblecomputation.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CrudUserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    @Query(value="SELECT * FROM Users u WHERE u.name LIKE %:keyword%", nativeQuery = true)
    List<User> findByKeywordName(@Param("keyword") String keyword);

    @Query(value="SELECT * FROM Users u WHERE u.location LIKE %:keyword%", nativeQuery = true)
    List<User> findByKeywordLocation(@Param("keyword") String keyword);
}
