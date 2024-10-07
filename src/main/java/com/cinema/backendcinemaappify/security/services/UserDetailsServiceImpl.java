package com.cinema.backendcinemaappify.security.services;

import com.cinema.backendcinemaappify.models.User;
import com.cinema.backendcinemaappify.repository.UserRepository;
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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

        // Return UserDetails implementation for the found user
        return UserDetailsImpl.build(user);
    }
}