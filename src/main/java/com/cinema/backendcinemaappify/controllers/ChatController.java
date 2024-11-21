package com.cinema.backendcinemaappify.controllers;

import com.cinema.backendcinemaappify.payload.request.ChatRequest;
import com.cinema.backendcinemaappify.security.services.HuggingFaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*", maxAge = 3600) // Allow cross-origin requests for all origins
@RestController // Indicate that this class is a REST controller
@RequestMapping("/api/chatbot")
public class ChatController {

    @Autowired
    private HuggingFaceService huggingFaceService;

    @PostMapping("/ask")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<String> askBot(@RequestBody ChatRequest request) {
        String response = huggingFaceService.getBotResponse(request.getMessage());
        return ResponseEntity.ok(response);
    }
}
