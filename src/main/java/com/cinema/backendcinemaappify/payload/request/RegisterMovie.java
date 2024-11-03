package com.cinema.backendcinemaappify.payload.request;

import jakarta.validation.constraints.NotBlank;

public class RegisterMovie {

    @NotBlank
    private String name;

    @NotBlank
    private String cinemaId;

    @NotBlank
    private String theaterName;

    @NotBlank
    private String photo;

    @NotBlank
    private String synopsis;

    @NotBlank
    private String duration;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getTheaterName() {
        return theaterName;
    }

    public void setTheaterName(String theaterName) {
        this.theaterName = theaterName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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
}
