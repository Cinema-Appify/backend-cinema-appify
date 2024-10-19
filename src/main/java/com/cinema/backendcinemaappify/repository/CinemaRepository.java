package com.cinema.backendcinemaappify.repository;

import com.cinema.backendcinemaappify.models.Cinema;
import com.cinema.backendcinemaappify.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface CinemaRepository extends MongoRepository<Cinema, String> {

    Optional<Cinema> findByEmail(String email);
    /**
     * Check if a cinema's email already exists in the database.
     *
     * @param email The cinema's email to check.
     * @return A Boolean indicating whether the email exists (true) or not (false).
     */
    Boolean existsByEmail(String email);
}