package com.cinema.backendcinemaappify.models;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "movie")
public class Movie {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String synopsis;

    @NotBlank
    private String duration;

    @NotBlank
    private String photo;

    @NotBlank
    private String cinemaId;

    @NotBlank
    private String theaterId;

    public Movie() {
    }

    public Movie(String name, String synopsis, String duration, String photo, String cinemaId, String theaterId) {
        this.name = name;
        this.synopsis = synopsis;
        this.duration = duration;
        this.photo = photo;
        this.cinemaId = cinemaId;
        this.theaterId = theaterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(String theaterId) {
        this.theaterId = theaterId;
    }
}
