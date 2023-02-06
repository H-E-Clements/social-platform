package com.example.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Roles {

    @Id
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
