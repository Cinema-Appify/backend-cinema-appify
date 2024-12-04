package com.cinema.backendcinemaappify.security.services;

import com.cinema.backendcinemaappify.models.Movie;
import com.cinema.backendcinemaappify.models.Theater;
import com.cinema.backendcinemaappify.repository.CinemaRepository;
import com.cinema.backendcinemaappify.repository.MovieRepository;
import com.cinema.backendcinemaappify.repository.TheaterRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private TheaterRepository theaterRepository;

    @Autowired
    private CinemaRepository cinemaRepository;

    @Value("${huggingface.api.token}")
    private String apiToken;

    private static final String API_URL = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.3";
    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceService.class);


    public Map<String, Object> getBotResponse(String userMessage) {
        String lowerMessage = userMessage.toLowerCase();
        logger.info(userMessage);
        if (lowerMessage.contains("salas") || lowerMessage.contains("sala")) {
            return getTheaters();
        } else if (lowerMessage.contains("peliculas") || lowerMessage.contains("película")) {
            return getMovies();
        } else if (lowerMessage.contains("horarios") || lowerMessage.contains("horario")) {
            return getSchedules();
        } else {
            return getAIResponse(userMessage);
        }

    }

    private Map<String, Object> getTheaters() {
        List<Theater> theaters = theaterRepository.findAll();

        if (theaters.isEmpty()) {
            return Map.of("message", "No encontré salas disponibles en la base de datos.", "type", "error");
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm"); // Formato para la hora
        StringBuilder response = new StringBuilder("Aquí tienes las salas disponibles con sus películas y horarios:\n");

        for (Theater theater : theaters) {
            response.append("- Sala: ").append(theater.getName()).append("\n");

            // películas de la sala
            List<Movie> movies = movieRepository.findByTheaterId(theater.getId());
            if (movies.isEmpty()) {
                response.append("  Sin películas disponibles.\n");
            } else {
                response.append("  Películas:\n");
                for (Movie movie : movies) {
                    response.append("    - ").append(movie.getName()).append(" (").append(movie.getDuration()).append(")\n");
                }
            }
                LocalDateTime dateTime = LocalDateTime.parse(theater.getSchedule().trim());
                String formattedTime = dateTime.format(timeFormatter); // Formateamos solo la hora
                response.append("  Horarios: ").append(formattedTime).append("\n\n");

        }

        logger.info(response.toString());
        return Map.of("message", response.toString(), "type", "theater");
    }

    private Map<String, Object> getMovies() {
        List<Movie> movies = movieRepository.findAll();

        if (movies.isEmpty()) {
            return Map.of("message", "No encontré películas disponibles en la base de datos.", "type", "error");
        }

        StringBuilder response = new StringBuilder("Aquí tienes las películas disponibles:\n");
        for (Movie movie : movies) {
            response.append("- ").append(movie.getName()).append("\n")
                    .append("  Duración: ").append(movie.getDuration()).append("\n\n");
        }

        return Map.of("message", response.toString(), "type", "movie");
    }

    private Map<String, Object> getSchedules() {
        List<Theater> theaters = theaterRepository.findAll();

        if (theaters.isEmpty()) {
            return Map.of("message", "No encontré horarios disponibles en la base de datos.", "type", "error");
        }

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        StringBuilder response = new StringBuilder("Aquí tienes los horarios disponibles por sala:\n");
        for (Theater theater : theaters) {
            response.append("- Sala: ").append(theater.getName()).append("\n");

            LocalDateTime dateTime = LocalDateTime.parse(theater.getSchedule().trim());
            String formattedTime = dateTime.format(timeFormatter);

            response.append("  Horarios: ").append(formattedTime).append("\n\n");
        }

        return Map.of("message", response.toString(), "type", "schedule");
    }


    private Map<String, Object> getAIResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        Map<String, Object> payload = new HashMap<>();
        payload.put("inputs", userMessage);
        payload.put("parameters", Map.of("max_length", 10));

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, String>> responseBody = mapper.readValue(response.getBody(), new TypeReference<>() {});
            String generatedText = responseBody.getFirst().get("generated_text");
            logger.info(generatedText);
            return Map.of("message", generatedText, "type", "ai");
        } catch (Exception e) {
            logger.error("Error al llamar a Hugging Face API", e);
            return Map.of("message", "Lo siento, no puedo procesar tu mensaje en este momento.", "type", "error");
        }

    }
}
