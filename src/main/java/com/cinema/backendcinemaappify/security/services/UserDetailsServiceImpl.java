package com.cinema.backendcinemaappify.security.services;

import com.cinema.backendcinemaappify.models.User;
import com.cinema.backendcinemaappify.models.Cinema;
import com.cinema.backendcinemaappify.repository.UserRepository;
import com.cinema.backendcinemaappify.repository.CinemaRepository;
import org.springframework.beans.factory.annotation.Autowired; // Import for dependency injection
import org.springframework.security.core.userdetails.UserDetails; // Import UserDetails interface
import org.springframework.security.core.userdetails.UserDetailsService; // Import UserDetailsService interface
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Import for handling user not found
import org.springframework.stereotype.Service; // Import for service annotation
import org.springframework.transaction.annotation.Transactional; // Import for transaction management

/**
 * Implementation of UserDetailsService to load user-specific data.
 */
@Service // Indicates that this class is a service component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired // Automatically injects UserRepository bean
    UserRepository userRepository;

    @Autowired // Automatically injects CinemaRepository bean
    CinemaRepository cinemaRepository;

    /**
     * Loads user details by username (email).
     *
     * @param email The email of the user or cinema.
     * @return UserDetails containing user or cinema information.
     * @throws UsernameNotFoundException if neither user nor cinema is found.
     */
    @Override
    @Transactional // Ensures that the method is transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Attempt to find the user by email in the UserRepository
        User user = userRepository.findByEmail(email).orElse(null);

        // If the user is found, return the user details
        if (user != null) {
            return UserDetailsImpl.build(user);
        }

        // If the user is not found, attempt to find the cinema by email in the CinemaRepository
        Cinema cinema = cinemaRepository.findByEmail(email).orElse(null);

        // If the cinema is found, return the cinema details
        if (cinema != null) {
            return CinemaDetailsImpl.build(cinema);
        }

        // If neither user nor cinema is found, throw an exception
        throw new UsernameNotFoundException("No User or Cinema found with email: " + email);
    }
}