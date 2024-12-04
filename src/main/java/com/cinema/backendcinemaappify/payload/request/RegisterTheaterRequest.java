package com.cinema.backendcinemaappify.payload.request;

import jakarta.validation.constraints.NotBlank;

public class RegisterTheaterRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String schedule;

    @NotBlank
    private String cinemaId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(String cinemaId) {
        this.cinemaId = cinemaId;
    }
}
