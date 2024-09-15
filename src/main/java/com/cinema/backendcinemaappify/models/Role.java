package com.cinema.backendcinemaappify.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rol")
public class Role {
    @Id
    private String id;

    private SystemRole name;

    public Role() {}

    public Role(SystemRole name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SystemRole getName() {
        return name;
    }

    public void setName(SystemRole name) {
        this.name = name;
    }
}