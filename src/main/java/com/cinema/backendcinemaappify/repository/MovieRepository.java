package com.cinema.backendcinemaappify.repository;

import com.cinema.backendcinemaappify.models.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends MongoRepository<Movie, String> {

    Optional<Movie> findByName(String name);

    List<Movie> findByCinemaId(String cinemaId);
}
