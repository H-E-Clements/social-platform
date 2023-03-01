package com.example.reversiblecomputation.repository;

import com.example.reversiblecomputation.domain.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Integer> {
}
