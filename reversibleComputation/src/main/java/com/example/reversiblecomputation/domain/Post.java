package com.example.reversiblecomputation.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.print.Doc;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="post")
public class Post {


    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String description;
    @Column(nullable=false)
    private Date uploaddate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user", nullable = false)
    private User user;
    @OneToMany(mappedBy = "post")
    private Set<Document> documents = new LinkedHashSet<>();
    @Column
    private String hashtags;
//    @OneToMany(mappedBy = "post")
//    private Set<Hashtag> hashtags = new HashSet<>();

    public void addDocument(Document document) { this.documents.add(document); }
}
