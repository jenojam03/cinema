package org.example.cinema.controllers;

import org.example.cinema.entities.Transazione;
import org.example.cinema.repositories.TransazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('admin')")
@RestController
@RequestMapping("/transazioni")
public class TransazioneController {

    @Autowired
    private TransazioneRepository transazioneRepository;

    @GetMapping
    public ResponseEntity<List<Transazione>> getAllTransazioni() {
        List<Transazione> transazioni = transazioneRepository.findAll();
        return ResponseEntity.ok(transazioni);
    }

    @GetMapping("/cliente")
    public ResponseEntity<List<Transazione>> getTransazioniByClienteEmail(@RequestParam String email) {
        List<Transazione> transazioni = transazioneRepository.findByClienteEmail(email);
        return ResponseEntity.ok(transazioni);
    }

    @GetMapping("/esito")
    public ResponseEntity<List<Transazione>> getTransazioniByEsito(@RequestParam boolean esito) {
        List<Transazione> transazioni = transazioneRepository.findByEsito(esito);
        return ResponseEntity.ok(transazioni);
    }
}
