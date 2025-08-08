package org.example.cinema.controllers;

import org.example.cinema.dto.ProiezioneCreateRequest;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.exceptions.ProiezioneEsistenteException;
import org.example.cinema.exceptions.ProiezioneNotFoundException;
import org.example.cinema.services.FilmService;
import org.example.cinema.services.ProiezioneService;
import org.example.cinema.support.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/proiezioni")
public class ProiezioneController {

    @Autowired
    private ProiezioneService proiezioneService;
    @Autowired
    private FilmService filmService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/create")
    public ResponseEntity<?> aggiungiProiezione(@RequestBody ProiezioneCreateRequest proiezione) {
        try {
            proiezioneService.aggiungiProiezione(proiezione);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Proiezione aggiunta con successo");
            return ResponseEntity.ok(response);
        } catch (ProiezioneEsistenteException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Proiezione già esistente");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping()
    public List<Proiezione> getAll() {
        return proiezioneService.showAllProiezioniOrdered();
    }

    @GetMapping("/paged")
    public ResponseEntity getAll(@RequestParam(value = "pageNumber", defaultValue = "0") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "id") String sortBy) {
        List<Proiezione> ris = proiezioneService.showAllProiezioni(pageNumber, pageSize, sortBy);
        if (ris.isEmpty()) {
            return new ResponseEntity<>(new ResponseMessage("Nessun Risultato!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(ris, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProiezione(@PathVariable Long id) {
        try {
            proiezioneService.deleteProiezione(id);
            return ResponseEntity.ok("Proiezione eliminata con successo");
        } catch (ProiezioneNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proiezione non trovata");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante l'eliminazione della proiezione");
        }
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProiezione(@PathVariable Long id, @RequestBody Calendar nuovaDataOra) {
        try {
            proiezioneService.aggiornaProiezione(id, nuovaDataOra);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Proiezione aggiornata con successo");
            return ResponseEntity.ok(response);
        } catch (ProiezioneNotFoundException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Proiezione non trovata");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Errore durante l'aggiornamento della proiezione: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
