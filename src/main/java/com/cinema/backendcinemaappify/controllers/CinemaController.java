package com.cinema.backendcinemaappify.controllers;


import com.cinema.backendcinemaappify.models.Cinema;
import com.cinema.backendcinemaappify.models.Theater;
import com.cinema.backendcinemaappify.models.User;
import com.cinema.backendcinemaappify.repository.CinemaRepository;
import com.cinema.backendcinemaappify.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600) // Allow cross-origin requests for all origins
@RestController // Indicate that this class is a REST controller
@RequestMapping("/api/cinema")
public class CinemaController {

    @Autowired
   private CinemaRepository cinemaRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/getCinemas")
    public ResponseEntity<?> getAllCinema() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        return ResponseEntity.ok(cinemas);
    }




}
