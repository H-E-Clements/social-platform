package com.example.reversiblecomputation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="document")
public class Document {
    @Id
    @GeneratedValue
    Integer id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String extension;
    @Column(nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "post", nullable = true)
    private Post post;

}
