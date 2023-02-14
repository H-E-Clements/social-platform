package com.example.reversiblecomputation.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="document")
public class Document {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(nullable=false)
    private String name;
    @Column(nullable=false)
    private String description;
    @Column(nullable=false)
    private String documenturl;
    @Column
    private String keywords;
    @Column(nullable=false)
    private Date uploaddate;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "documents", nullable = false)
    private User user;


}
