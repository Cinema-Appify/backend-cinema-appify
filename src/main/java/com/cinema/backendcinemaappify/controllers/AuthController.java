package com.cinema.backendcinemaappify.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.cinema.backendcinemaappify.models.*;
import com.cinema.backendcinemaappify.payload.request.*;
import com.cinema.backendcinemaappify.payload.response.JwtResponse;
import com.cinema.backendcinemaappify.payload.response.MessageResponse;
import com.cinema.backendcinemaappify.repository.*;
import com.cinema.backendcinemaappify.security.jwt.JwtUtils;
import com.cinema.backendcinemaappify.security.services.CinemaDetailsImpl;
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

@CrossOrigin(origins = "*", maxAge = 3600) // Allow cross-origin requests for all origins
@RestController // Indicate that this class is a REST controller
@RequestMapping("/api/auth") // Base URL for authentication-related endpoints
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager; // Handles user authentication

    @Autowired
    TheaterRepository theaterRepository;

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
    @Autowired
    private MovieRepository movieRepository;

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

        if(userRepository.findByEmail(loginRequest.getEmail()).isPresent()) {
            // Get user details from the authentication object
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            // Extract user roles into a list
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // Return a response containing the JWT and user details
            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getName(),
                    userDetails.getFirstName(),
                    userDetails.getLastName(),
                    userDetails.getEmail(),
                    roles));
        } else {
            // Get user details from the authentication object
            CinemaDetailsImpl cinemaDetails = (CinemaDetailsImpl) authentication.getPrincipal();

            // Extract user roles into a list
            List<String> roles = cinemaDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            // Return a response containing the JWT and user details
            return ResponseEntity.ok(new JwtResponse(jwt,
                    cinemaDetails.getId(),
                    cinemaDetails.getName(),
                    cinemaDetails.getEmail(),
                    cinemaDetails.getState(),
                    roles));
        }
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

    //----------------------------------------------------------------------------------------------

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

    @GetMapping("/getCinemas")
    public ResponseEntity<?> getAllCinema() {
        List<Cinema> cinemas = cinemaRepository.findAll();
        return ResponseEntity.ok(cinemas);
    }

    @GetMapping("/getUsers")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }


//------------------------------------------------------------------------------------------------------------


    @PostMapping("/createTheater")
    public ResponseEntity<?> CreateTheater(@RequestBody RegisterTheaterRequest registerTheaterRequest) {

        Optional<Cinema> cinema = cinemaRepository.findById(registerTheaterRequest.getCinemaId());

        if (cinema.isPresent()) {
            Theater newTheater = new Theater(
                    registerTheaterRequest.getName(),
                    registerTheaterRequest.getSchedule(),
                    cinema.get().getId()
            );
            System.out.println(newTheater);
            theaterRepository.save(newTheater);

            return ResponseEntity.ok(new MessageResponse("Theater registered successfully!"));

        }else {

            return ResponseEntity.badRequest().body("Cinema ID is invalid");
        }
    }


    @GetMapping("/cinema/{cinemaId}")
    public ResponseEntity<?> GetAllTheatersByCinemaId(@PathVariable String cinemaId) {
        List<Theater> theaters = theaterRepository.findByCinemaId(cinemaId);
        System.out.println(theaters);
        System.out.println(cinemaId);
        if (!theaters.isEmpty()) {
            return ResponseEntity.ok(theaters);
        }else {
            return ResponseEntity.status(404).body("Couldn't theaters with cinema ID: " + cinemaId);
        }
    }


//-------------------------------------------------------------------------------

    @PostMapping("/createMovie")
    public ResponseEntity<?> CreateMovie(@RequestBody RegisterMovie registerMovieRequest) {
        // Verificar si el usuario está autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: No autorizado");
        }

        // Loguear el usuario autenticado
        System.out.println("Usuario autenticado: " + authentication.getName());

        // Loguear la solicitud recibida
        System.out.println("Solicitud para crear película: " + registerMovieRequest);

        // Buscar el cine y el teatro
        Optional<Cinema> cinemas = cinemaRepository.findById(registerMovieRequest.getCinemaId());
        Optional<Theater> theaters = theaterRepository.findByName(registerMovieRequest.getTheaterName());

        // Loguear resultados de las búsquedas
        if (cinemas.isPresent()) {
            System.out.println("Cine encontrado: " + cinemas.get().getName());
        } else {
            System.out.println("Cine no encontrado con ID: " + registerMovieRequest.getCinemaId());
        }

        if (theaters.isPresent()) {
            System.out.println("Teatro encontrado: " + theaters.get().getName());
        } else {
            System.out.println("Teatro no encontrado con nombre: " + registerMovieRequest.getTheaterName());
        }

        // Comprobar si se encontraron cine y teatro
        if (cinemas.isPresent() && theaters.isPresent()) {
            Movie newMovie = new Movie(
                    registerMovieRequest.getName(),
                    registerMovieRequest.getSynopsis(),
                    registerMovieRequest.getDuration(),
                    registerMovieRequest.getPhoto(),
                    cinemas.get().getId(),
                    theaters.get().getId()
            );

            // Loguear la película que se va a guardar
            System.out.println("Guardando nueva película: " + newMovie);
            movieRepository.save(newMovie);
            return ResponseEntity.ok(Map.of("message", "Movie created successfully!"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Couldn't create movie!"));
        }
    }




    @GetMapping("/cinema/{cinemaId}/movies")
    public ResponseEntity<?> GetMovies(@PathVariable String cinemaId) {
        List<Movie> movies = movieRepository.findByCinemaId(cinemaId);

        if (!movies.isEmpty()) {
            return ResponseEntity.ok(movies);
        } else {
            return ResponseEntity.status(404).body("Couldn't find movies with cinema ID: " + cinemaId);
        }
    }


    @GetMapping("/movie/getAll")
    public ResponseEntity<?> GetAllMovies() {
        System.out.println("Ejecutando GetAllMovies...");

        List<Movie> movies = movieRepository.findAll();
        System.out.println("Películas encontradas: " + movies.size());

        // Imprimir los detalles de cada película (opcional)
        for (Movie movie : movies) {
            System.out.println("Detalles de la película: " + movie);
        }

        if (!movies.isEmpty()) {
            System.out.println("Devolviendo películas con éxito.");
            return ResponseEntity.ok(movies);
        } else {
            System.out.println("No se encontraron películas.");
            return ResponseEntity.status(404).body("Couldn't find theaters with cinema ID: ");
        }
    }


    @GetMapping("/cinema/{cinemaId}/salas")
    public ResponseEntity<?> GetTheatersByCinemaId(@PathVariable String cinemaId) {
        List<Theater> theaters = theaterRepository.findByCinemaId(cinemaId);
        System.out.println(theaters);
        System.out.println(cinemaId);
        if (!theaters.isEmpty()) {
            return ResponseEntity.ok(theaters);
        }else {
            return ResponseEntity.status(404).body("Couldn't find theaters with cinema ID: " + cinemaId);
        }
    }


    // ---------------------------------------------------------------------------------------------

    @DeleteMapping("/deleteTheater")
    public ResponseEntity<?> DeleteTheater(@PathVariable String theaterName) {
        Optional<Theater> theater = theaterRepository.findByName(theaterName);
        if (theater.isPresent()) {
            theaterRepository.delete(theater.get());

            return ResponseEntity.ok("Theater deleted successfully");
        }   else {
            return ResponseEntity.badRequest().body("Error: Couldn't delete theater!");
        }
    }

    @DeleteMapping("/deleteMovie")
    public ResponseEntity<?> DeleteMovie(@PathVariable String movieName) {

        Optional<Movie> movie = movieRepository.findByName(movieName);
        System.out.println(movie);
        if (movie.isPresent()) {
            movieRepository.delete(movie.get());
            return ResponseEntity.ok("Movie deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("Error: Couldn't delete movie!");
        }

    }
}