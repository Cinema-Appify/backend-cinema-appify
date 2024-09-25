package com.cinema.backendcinemaappify.repository;

import com.cinema.backendcinemaappify.models.Cinema;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CinemaRepository extends MongoRepository<Cinema, String> {

    /**
     * Check if a cinema's email already exists in the database.
     *
     * @param correo The cinema's email to check.
     * @return A Boolean indicating whether the email exists (true) or not (false).
     */
    Boolean existsByCorreo(String correo);

    Optional<Cinema> findByCorreo(String correo);
}
