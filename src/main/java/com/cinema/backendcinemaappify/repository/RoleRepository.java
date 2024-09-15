package com.cinema.backendcinemaappify.repository;

import com.cinema.backendcinemaappify.models.Role;
import com.cinema.backendcinemaappify.models.SystemRole;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    /**
     * Find a Role by its name.
     *
     * @param name The name of the role represented as an EmployeeRole enum.
     * @return An Optional containing the Role if found, or empty if not found.
     */
    Optional<Role> findByName(SystemRole name);
}