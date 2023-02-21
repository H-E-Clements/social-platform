package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}
