package org.example.cinema.controllers;

import org.example.cinema.entities.Cliente;
import org.example.cinema.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/check")
public class CheckController {

    @Autowired
    private ClienteRepository clienteRepository;

    // Accessibile da chiunque: utile per il frontend per verificare che il backend risponde
    @GetMapping("/public")
    public ResponseEntity<String> publicCheck() {
        return ResponseEntity.ok("API pubblica raggiungibile");
    }

    // Accessibile da utenti autenticati
    @GetMapping("/user")
    @PreAuthorize("hasRole('cliente')")
    public ResponseEntity<?> checkCliente(Authentication authentication) {
        String email = authentication.getName();
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non trovato");

        return ResponseEntity.ok("Ciao " + cliente.getNome() + ", sei autenticato come cliente!");
    }

    // Accessibile solo da admin
    @GetMapping("/admin")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> checkAdmin() {
        return ResponseEntity.ok("Accesso Admin OK");
    }

}
