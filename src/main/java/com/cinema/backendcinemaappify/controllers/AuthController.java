package com.cinema.backendcinemaappify.controllers;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.cinema.backendcinemaappify.models.Cinema;
import com.cinema.backendcinemaappify.models.Role;
import com.cinema.backendcinemaappify.models.SystemRole;
import com.cinema.backendcinemaappify.models.User;
import com.cinema.backendcinemaappify.payload.request.LoginRequest;
import com.cinema.backendcinemaappify.payload.request.SignUpCinemaRequest;
import com.cinema.backendcinemaappify.payload.request.SignupRequest;
import com.cinema.backendcinemaappify.payload.response.JwtResponse;
import com.cinema.backendcinemaappify.payload.response.MessageResponse;
import com.cinema.backendcinemaappify.repository.CinemaRepository;
import com.cinema.backendcinemaappify.repository.RoleRepository;
import com.cinema.backendcinemaappify.repository.UserRepository;
import com.cinema.backendcinemaappify.security.jwt.JwtUtils;
import com.cinema.backendcinemaappify.security.services.CloudinaryServiceImpl;
import com.cinema.backendcinemaappify.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600) // Allow cross-origin requests for all origins
@RestController // Indicate that this class is a REST controller
@RequestMapping("/api/auth") // Base URL for authentication-related endpoints
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager; // Handles user authentication

    @Autowired
    CloudinaryServiceImpl cloudinaryService;

    @Autowired
    UserRepository userRepository; // Repository for user-related database operations

    @Autowired
    CinemaRepository cinemaRepository; // Repository for cinema

    @Autowired
    RoleRepository roleRepository; // Repository for role-related database operations

    @Autowired
    PasswordEncoder encoder; // Encoder for password hashing

    @Autowired
    JwtUtils jwtUtils; // Utility for generating JWT tokens

    /**
     * Authenticate user and return a JWT token if successful.
     *
     * @param loginRequest The login request containing username and password.
     * @return A ResponseEntity containing the JWT response or an error message.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        // Authenticate the user with the provided username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));

        // Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token based on the authentication
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Get user details from the authentication object
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Extract user roles into a list
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        // Return a response containing the JWT and user details
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles));
    }

    /**
     * Register a new user account.
     *
     * @param signUpRequest The signup request containing user details.
     * @return A ResponseEntity indicating success or error message.
     */
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {

        // Check if the email is already in use
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create a new user's account
        User user = new User(
                signUpRequest.getEmail(),
                signUpRequest.getName(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                encoder.encode(signUpRequest.getPassword())); // Encode the password

        Set<String> strRoles = signUpRequest.getRoles(); // Get the roles from the request
        Set<Role> roles = new HashSet<>(); // Initialize a set to hold the user roles

        // Assign roles based on the request or default to user role
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(SystemRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(SystemRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(SystemRole.ROLE_CINEMA)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(SystemRole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        // Assign roles to the user and save it to the database
        user.setRoles(roles);
        userRepository.save(user);

        // Return a success message upon successful registration
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @PostMapping("/signUpCinema")
    public ResponseEntity<?> registerCinema(@Valid @RequestBody SignUpCinemaRequest cinemaRequest) {
        System.out.println("URL de la foto recibida: " + cinemaRequest.getPhoto());

        if (cinemaRepository.existsByEmail(cinemaRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        Cinema cinema = new Cinema(
                cinemaRequest.getName(),
                cinemaRequest.getEmail(),
                encoder.encode(cinemaRequest.getPassword())
        );

        Set<String> strRoles = cinemaRequest.getRoles(); // Obtener los roles de la solicitud
        Set<Role> roles = new HashSet<>(); // Inicializar un conjunto para almacenar los roles

        // Asignar roles según la solicitud o por defecto al rol de cine
        if (strRoles == null) {
            Role userRole = roleRepository.findByName(SystemRole.ROLE_CINEMA)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        }

        // Manejar la URL de la foto
        if (cinemaRequest.getPhoto() != null && !cinemaRequest.getPhoto().isEmpty()) {
            cinema.setPhoto(cinemaRequest.getPhoto());
        } else {
            System.out.println("No se proporcionó una URL de foto válida.");
        }

        // Asignar roles al cine y guardarlo en la base de datos
        cinema.setRoles(roles);
        System.out.println(cinema);
        cinemaRepository.save(cinema);

        return ResponseEntity.ok(new MessageResponse("Cinema registered successfully!"));
    }




}
