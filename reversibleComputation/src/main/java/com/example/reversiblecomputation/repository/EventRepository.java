package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Event;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepository extends CrudRepository<Event, String> {
    List<Event> findAll();

    @Query(value="SELECT * FROM Event e WHERE e.title LIKE %:keyword%", nativeQuery = true)
    List<Event> findByKeywordTitle(@Param("keyword") String keyword);

    @Query(value="SELECT * FROM Event e WHERE e.description LIKE %:keyword%", nativeQuery = true)
    List<Event> findByKeywordDescription(@Param("keyword") String keyword);

    @Query(value="SELECT * FROM Event e WHERE e.date LIKE %:keyword%", nativeQuery = true)
    List<Event> findByKeywordDate(@Param("keyword") String keyword);

    @Query(value="SELECT * FROM Event e WHERE e.author LIKE %:keyword%", nativeQuery = true)
    List<Event> findByKeywordAuthor(@Param("keyword") String keyword);

    @Query(value="SELECT * FROM Event e WHERE e.location LIKE %:keyword%", nativeQuery = true)
    List<Event> findByKeywordLocation(@Param("keyword") String keyword);
    //SQL queries - lets people search for events with keywords

}
