package com.cinema.backendcinemaappify.controllers;

import com.cinema.backendcinemaappify.models.Cinema;
import com.cinema.backendcinemaappify.models.Theater;
import com.cinema.backendcinemaappify.payload.request.RegisterTheaterRequest;
import com.cinema.backendcinemaappify.payload.response.MessageResponse;
import com.cinema.backendcinemaappify.repository.CinemaRepository;
import com.cinema.backendcinemaappify.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600) // Allow cross-origin requests for all origins
@RestController // Indicate that this class is a REST controller
@RequestMapping("/api/theater")
public class TheaterController {

    @Autowired
    TheaterRepository theaterRepository;

    @Autowired
    CinemaRepository cinemaRepository;


//    @GetMapping("/getAllTheaters")
//    public ResponseEntity<?> GetAllTheaters() {
//        List<Theater> theaters = theaterRepository.findAll();
//        return ResponseEntity.ok(theaters);
//    }
//
//    @PostMapping("/createTheater")
//    public ResponseEntity<?> CreateTheater(@RequestBody RegisterTheaterRequest registerTheaterRequest) {
//
//        Optional<Cinema> cinema = cinemaRepository.findById(registerTheaterRequest.getCinemaId());
//
//        if (cinema.isPresent()) {
//            Theater newTheater = new Theater(
//                    registerTheaterRequest.getName(),
//                    registerTheaterRequest.getSchedule(),
//                    cinema.get().getId()
//            );
//
//            theaterRepository.save(newTheater);
//            System.out.println(newTheater);
//            return ResponseEntity.ok(new MessageResponse("Theater created successfully"));
//
//        }else {
//
//            return ResponseEntity.badRequest().body("Cinema ID is invalid");
//        }
//    }

    @PreAuthorize("hasRole('ROLE_CINEMA')")
    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<?> GetAllTheatersByCinemaId(@PathVariable String cinemaId) {
        List<Theater> theaters = theaterRepository.findByCinemaId(cinemaId);
        System.out.println(theaters);
        System.out.println(cinemaId);
        if (!theaters.isEmpty()) {
            return ResponseEntity.ok(theaters);
        }else {
            return ResponseEntity.status(404).body("Couldn't theaters with cinema ID: " + cinemaId);
        }
    }



}