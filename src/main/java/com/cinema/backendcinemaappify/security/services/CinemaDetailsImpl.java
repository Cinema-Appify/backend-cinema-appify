package com.cinema.backendcinemaappify.security.services;

import com.cinema.backendcinemaappify.models.Cinema;
import com.cinema.backendcinemaappify.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CinemaDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L; // Serializable version identifier

    private String id; // Unique identifier for the user
    private String name;
    private String email; // Email address of the user
    private String state;

    @JsonIgnore // Prevent serialization of the password field
    private String password; // Password of the user
    private Collection<? extends GrantedAuthority> authorities; // Collection of user's authorities (roles)

    /**
     * Constructor to initialize UserDetailsImpl.
     *
     * @param id           The unique identifier of the user.
     * @param email        The email of the user.
     * @param password     The password of the user.
     * @param authorities  The collection of user's authorities.
     */
    public CinemaDetailsImpl(String id, String name, String email, String state, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id; // Set user ID
        this.name = name;
        this.email = email; // Set email
        this.state = state;
        this.password = password; // Set password
        this.authorities = authorities; // Set authorities
    }

    /**
     * Builds a UserDetailsImpl instance from a User object.
     *
     * @param cinema The User object.
     * @return A UserDetailsImpl instance.
     */
    public static CinemaDetailsImpl build(Cinema cinema) {
        // Map the roles of the user to GrantedAuthority
        List<GrantedAuthority> authorities = cinema.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name())) // Convert each role to SimpleGrantedAuthority
                .collect(Collectors.toList()); // Collect into a list

        // Return a new UserDetailsImpl object
        return new CinemaDetailsImpl(
                cinema.getId(), // User ID
                cinema.getName(),
                cinema.getEmail(), // Email
                cinema.getState(),
                cinema.getPassword(), // Password
                authorities); // User authorities
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities; // Return user's authorities
    }

    public String getId() {
        return id; // Return user ID
    }
    public String getEmail() {
        return email; // Return email
    }

    public String getName() { return name; }

    public String getState() { return state; }

    @Override
    public String getPassword() {
        return password; // Return password
    }

    @Override
    public String getUsername() {
        return email; // Return username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Credentials are not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // Account is enabled
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) // Check if the same object
            return true;
        if (o == null || getClass() != o.getClass()) // Check if the object is null or not of the same class
            return false;
        CinemaDetailsImpl cinema = (CinemaDetailsImpl) o; // Cast to UserDetailsImpl
        return Objects.equals(id, cinema.id); // Check if IDs are equal
    }
}
