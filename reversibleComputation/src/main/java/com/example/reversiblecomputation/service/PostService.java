package com.example.reversiblecomputation.service;

import com.example.reversiblecomputation.domain.Post;
import com.example.reversiblecomputation.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public List<Post> findAllElements() {
        return postRepository.findAll();
    }
}
