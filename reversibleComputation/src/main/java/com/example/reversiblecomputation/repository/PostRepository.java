package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findAllByUser(User user);
}
