package com.cinema.backendcinemaappify.security.services;

import com.cinema.backendcinemaappify.models.Movie;
import com.cinema.backendcinemaappify.repository.MovieRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HuggingFaceService {

    @Autowired
    private MovieRepository movieRepository;

    @Value("${huggingface.api.token}")
    private String apiToken;

    private static final String API_URL = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.3";
    private static final Logger logger = LoggerFactory.getLogger(HuggingFaceService.class);

    public Map<String, Object> getBotResponse(String userMessage) {
        // Si el mensaje contiene 'peliculas', devuelve las películas
        if (userMessage.toLowerCase().contains("peliculas") || userMessage.toLowerCase().contains("movie")) {
            List<Movie> movies = movieRepository.findAll();
            if (movies.isEmpty()) {
                return Map.of("message", "No encontré películas en la base de datos.", "type", "error");
            }

            StringBuilder response = new StringBuilder("Aquí tienes las películas disponibles:\n");
            movies.forEach(movie -> response.append("- ").append(movie.getName()).append("\n"));
            return Map.of("message", response.toString(), "type", "movie");
        }

        // Llamada a la API para obtener la respuesta generada por la IA
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
            String generatedText = response.getBody();
            return Map.of("message", generatedText, "type", "ai");
        } catch (Exception e) {
            logger.error("Error al llamar a Hugging Face API", e);
            return Map.of("message", "Lo siento, no puedo procesar tu mensaje en este momento.", "type", "error");
        }
    }
}
