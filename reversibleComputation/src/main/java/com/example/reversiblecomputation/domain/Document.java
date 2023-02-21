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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "post", nullable = false)
    private Post post;

}
