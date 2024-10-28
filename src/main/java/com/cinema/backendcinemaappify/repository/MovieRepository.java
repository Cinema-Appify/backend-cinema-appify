package com.cinema.backendcinemaappify.repository;

import com.cinema.backendcinemaappify.models.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
}
