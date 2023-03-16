package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Feed;
import com.example.reversiblecomputation.domain.Paper;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FeedRepository extends CrudRepository<Feed, String> {
    List<Feed> findAll();
}
