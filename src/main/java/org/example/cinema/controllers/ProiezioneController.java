package org.example.cinema.controllers;

import org.example.cinema.entities.Film;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.exceptions.ProiezioneEsistenteException;
import org.example.cinema.repositories.ProiezioneRepository;
import org.example.cinema.services.FilmService;
import org.example.cinema.services.ProiezioneService;
import org.example.cinema.support.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/proiezione")
public class ProiezioneController {

    @Autowired
    private ProiezioneService proiezioneService;
    @Autowired
    private FilmService filmService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/create")
    public ResponseEntity<?> aggiungiProiezione(@RequestBody Proiezione proiezione) {
        try {
            proiezioneService.aggiungiProiezione(proiezione);
            // Restituire un oggetto JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "Proiezione aggiunta con successo");
            return ResponseEntity.ok(response);
        } catch (ProiezioneEsistenteException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Codice a barre già esistente");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_default-roles-my-realm-test')")
    @GetMapping("/getall")
    public List<Proiezione> getAll() {
        return proiezioneService.showAllProiezioniOrdered();
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Proiezione> ris = proiezioneService.showAllProiezioni(pageNumber, pageSize, sortBy);
        if (ris.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("Nessun Risultato!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(ris, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('admin')")
    @GetMapping("/search/by_film")
    public ResponseEntity getByName(@RequestParam(required = false) Long filmId) {
        List<Proiezione> result = proiezioneService.showProiezioniByFilmOrdered(filmId);
        if (result.isEmpty()) {
            return ResponseEntity.ok(new ResponseMessage("Nessun Risultato!"));
        }
        return ResponseEntity.ok(result);
    }
}
