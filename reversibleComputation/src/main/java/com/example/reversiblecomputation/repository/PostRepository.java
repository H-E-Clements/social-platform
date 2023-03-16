package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, String> {
}
