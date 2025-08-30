package org.example.cinema.controllers;

import org.example.cinema.entities.Transazione;
import org.example.cinema.repositories.TransazioneRepository;
import org.example.cinema.services.TransazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/transazioni")
public class TransazioneController {

    @Autowired
    private TransazioneService transazioneService;

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public ResponseEntity<List<Transazione>> getAllTransazioni() {
        List<Transazione> transazioni = transazioneService.getTransazioni();
        return ResponseEntity.ok(transazioni);
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/cliente")
    public ResponseEntity<List<Transazione>> getTransazioniByClienteEmail(@RequestParam String email) {
        List<Transazione> transazioni = transazioneService.getTransazioniByEmail(email);
        return ResponseEntity.ok(transazioni);
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping("/esito")
    public ResponseEntity<List<Transazione>> getTransazioniByEsito(@RequestParam boolean esito) {
        List<Transazione> transazioni = transazioneService.getTransazioniByEsito(esito);
        return ResponseEntity.ok(transazioni);
    }
}
