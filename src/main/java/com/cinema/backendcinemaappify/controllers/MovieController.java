package com.cinema.backendcinemaappify.controllers;

import com.cinema.backendcinemaappify.models.Cinema;
import com.cinema.backendcinemaappify.models.Movie;
import com.cinema.backendcinemaappify.models.Theater;
import com.cinema.backendcinemaappify.payload.request.RegisterMovie;
import com.cinema.backendcinemaappify.repository.CinemaRepository;
import com.cinema.backendcinemaappify.repository.MovieRepository;
import com.cinema.backendcinemaappify.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600) // Allow cross-origin requests for all origins
@RestController // Indicate that this class is a REST controller
@RequestMapping("/api/movie")
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private TheaterRepository theaterRepository;


//    @PreAuthorize("hasRole('ROLE_CINEMA')")
//    @PostMapping("/createMovie")
//    public ResponseEntity<?> CreateMovie(@RequestBody RegisterMovie registerMovieRequest) {
//        Optional<Cinema> cinemas = cinemaRepository.findById(registerMovieRequest.getCinemaId());
//        Optional<Theater> theaters = theaterRepository.findByName(registerMovieRequest.getTheaterName());
//
//        System.out.println(cinemas.get().getName());
//        System.out.println(theaters.get().getName());
//
//        if (cinemas.isPresent() && theaters.isPresent()) {
//            Movie newMovie = new Movie(
//                    registerMovieRequest.getName(),
//                    registerMovieRequest.getSynopsis(),
//                    registerMovieRequest.getDuration(),
//                    registerMovieRequest.getPhoto(),
//                    cinemas.get().getId(),
//                    theaters.get().getId()
//            );
//
//            System.out.println(newMovie);
//            movieRepository.save(newMovie);
//            return ResponseEntity.ok("Movie created successfully");
//        }else {
//            return ResponseEntity.badRequest().body("Error: Couldn't register movie!");
//        }
//    }

    @GetMapping("/getMovies")
    public ResponseEntity<?> GetMovies() {
        List<Movie> movies = movieRepository.findAll();
        return ResponseEntity.ok(movies);
    }

    @GetMapping("/{cinemaId}/rooms")
    public ResponseEntity<List<Theater>> getRoomsByCinema(@PathVariable String cinemaId) {
        List<Theater> rooms = theaterRepository.findByCinemaId(cinemaId);

        if (!rooms.isEmpty()) {
            return ResponseEntity.ok(rooms);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
}
