package com.example.reversiblecomputation.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "Roles")
public class Role {
    @Id
    @GeneratedValue
    @Column(name = "Id")
    private String id;

    @Column(name = "Name")
    private String name;
}
