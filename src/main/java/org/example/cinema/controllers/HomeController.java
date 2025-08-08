/*package org.example.cinema.controllers;

import org.example.cinema.entities.Cliente;
import org.example.cinema.jwt.CustomJwt;
import org.example.cinema.repositories.ClienteRepository;
import org.example.cinema.repositories.FilmRepository;
import org.example.cinema.repositories.ProiezioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(
        origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE }
)
@RequestMapping("")
public class HomeController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private ProiezioneRepository proiezioneRepository;

    /**
     * Endpoint per verificare se l'utente è autenticato e presente nel DB come cliente.
     * Se non lo è, lo registra automaticamente usando le info del token JWT (con Keycloak).
     *//*
    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> home() {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();

        Cliente loggato = clienteRepository.findByEmail(email);

        if (loggato == null) {
            Cliente nuovo = new Cliente();
            nuovo.setEmail(email);
            nuovo.setNome(jwt.getFirstname());
            nuovo.setCognome(jwt.getLastname());
            clienteRepository.save(nuovo);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Utente autenticato correttamente e presente nel database.");
        return ResponseEntity.ok(response);
    }
}*/
