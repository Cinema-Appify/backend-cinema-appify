package com.cinema.backendcinemaappify.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Document(collection = "theater")
public class Theater {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String Schedule;

    @NotBlank
    private String cinemaId;

    public Theater() {
    }

    public Theater(String name, String schedule, String cinemaId) {
        this.name = name;
        Schedule = schedule;
        this.cinemaId = cinemaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String schedule) {
        Schedule = schedule;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }
}