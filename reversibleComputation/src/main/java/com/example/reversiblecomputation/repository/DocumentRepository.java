package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Post, Integer> {
}
