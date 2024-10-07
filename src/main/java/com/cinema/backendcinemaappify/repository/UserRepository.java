package com.cinema.backendcinemaappify.repository;

import com.cinema.backendcinemaappify.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

/**
 * Repository interface for accessing User entities in the MongoDB database.
 * It extends MongoRepository, providing CRUD operations for User objects.
 */
public interface UserRepository extends MongoRepository<User, String> {



    /**
     * Check if a username already exists in the database.
     *
     * @param email The username to check.
     * @return A Boolean indicating whether the username exists (true) or not (false).
     */
    Optional<User> findByEmail(String email);


    /**
     * Check if an email already exists in the database.
     *
     * @param email The email to check.
     * @return A Boolean indicating whether the email exists (true) or not (false).
     */
    Boolean existsByEmail(String email);
}
