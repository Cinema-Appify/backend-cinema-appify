package com.cinema.backendcinemaappify.security.services;

import com.cinema.backendcinemaappify.models.Cinema;
import com.cinema.backendcinemaappify.models.User;
import com.cinema.backendcinemaappify.repository.CinemaRepository;
import com.cinema.backendcinemaappify.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public class CinemaDetailsServiceImpl implements UserDetailsService {

    @Autowired // Automatically injects UserRepository bean
    CinemaRepository cinemaRepository;

    /**
     * Loads user details by username.
     *
     * @param email The username of the user.
     * @return UserDetails containing user information.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    @Transactional // Ensures that the method is transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Attempt to find the user by username
        Cinema cinema = cinemaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Cinema Not Found with email: " + email));

        // Return UserDetails implementation for the found user
        return CinemaDetailsImpl.build(cinema);
    }
}
