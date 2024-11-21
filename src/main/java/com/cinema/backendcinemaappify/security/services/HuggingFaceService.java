package com.cinema.backendcinemaappify.security.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Map;

@Service
public class HuggingFaceService {

    @Value("${huggingface.api.token}")
    private String apiToken;

    // URL del modelo en español
    private static final String API_URL = "https://api-inference.huggingface.co/models/mistralai/Mistral-7B-Instruct-v0.3";

    public String getBotResponse(String userMessage) {
        RestTemplate restTemplate = new RestTemplate();

        // Construimos la petición
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiToken);

        // Payload con los parámetros del modelo
        Map<String, Object> payload = new HashMap<>();
        payload.put("inputs", userMessage);
        payload.put("parameters", new HashMap<String, Object>() {{
            put("max_length", 10);
        }});

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
            return response.getBody();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Lo siento, no puedo procesar tu mensaje en este momento.";
        }
    }
}