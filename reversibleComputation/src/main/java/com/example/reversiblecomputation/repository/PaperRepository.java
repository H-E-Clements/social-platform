package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Paper;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PaperRepository extends CrudRepository<Paper, String> {
    List<Paper> findAll();

    @Query(value="SELECT * FROM Paper p WHERE p.description LIKE %:keyword%", nativeQuery = true)
    List<Paper> findByKeywordDescription(@Param("keyword") String keyword);

    @Query(value="SELECT * FROM Paper p WHERE p.author LIKE %:keyword%", nativeQuery = true)
    List<Paper> findByKeywordAuthor(@Param("keyword") String keyword);

    @Query(value="SELECT * FROM Paper p WHERE p.file_name LIKE %:keyword%", nativeQuery = true)
    List<Paper> findByKeywordFileName(@Param("keyword") String keyword);

    @Query(value="SELECT * FROM Paper p WHERE p.upload_date LIKE %:keyword%", nativeQuery = true)
    List<Paper> findByKeywordUploadDate(@Param("keyword") String keyword);
}