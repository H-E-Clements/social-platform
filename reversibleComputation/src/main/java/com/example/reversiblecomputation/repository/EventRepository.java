package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Event;
import com.example.reversiblecomputation.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, String> {
}
