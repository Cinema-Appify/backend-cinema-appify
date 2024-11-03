package com.cinema.backendcinemaappify.repository;

import com.cinema.backendcinemaappify.models.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TheaterRepository extends MongoRepository<Theater, String> {

    List<Theater> findByCinemaId(String cinemaId);

    Optional<Theater> findByName(String name);

}