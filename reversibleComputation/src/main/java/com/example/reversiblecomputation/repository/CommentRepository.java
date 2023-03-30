package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Comment;
import com.example.reversiblecomputation.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, String> {
    List<Comment> findAllByTarget(User target);
}
