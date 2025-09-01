package org.example.cinema.controllers;

import org.apache.coyote.Response;
import org.example.cinema.dto.ProiezioneCreateRequest;
import org.example.cinema.entities.Posto;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.exceptions.ProiezioneEsistenteException;
import org.example.cinema.exceptions.ProiezioneNotFoundException;
import org.example.cinema.repositories.ProiezioneRepository;
import org.example.cinema.services.FilmService;
import org.example.cinema.services.ProiezioneService;
import org.example.cinema.support.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/proiezioni")
public class ProiezioneController {

    @Autowired
    private ProiezioneService proiezioneService;


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
    @DeleteMapping("/delete/{id}")
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
    @PutMapping("/put/{id}")
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

    @GetMapping("/byDate")
    public ResponseEntity<?> getProiezioniByDate(@RequestParam("data") String dataString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar dataCercata = Calendar.getInstance();
            dataCercata.setTime(sdf.parse(dataString));
            return ResponseEntity.ok(proiezioneService.getProiezioneByData(dataCercata));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato data non valido. Usa dd-MM-yyyy.");
        }

    }

    @GetMapping("/betweenDates")
    public ResponseEntity<?> getProiezioniBetweenDates(
            @RequestParam("inizio") String inizioString,
            @RequestParam("fine") String fineString) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar inizio = Calendar.getInstance();
            inizio.setTime(sdf.parse(inizioString));

            Calendar fine = Calendar.getInstance();
            fine.setTime(sdf.parse(fineString));
            return ResponseEntity.ok(proiezioneService.getProiezioneBetween(inizio, fine));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato data non valido. Usa dd-MM-yyyy.");
        }
    }

    @GetMapping("/{proiezioneId}/posti_disponibili")
    public ResponseEntity<?> getPostiDisponibili(@PathVariable Long proiezioneId) {
        return ResponseEntity.ok(proiezioneService.getPostiDisponibili(proiezioneId));
    }

    @GetMapping("/{proiezioneId}/posti")
    public ResponseEntity<?> getPosti(@PathVariable Long proiezioneId) {
        return ResponseEntity.ok(proiezioneService.getPosti(proiezioneId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProiezioneById(@PathVariable Long id) {

        Optional<Proiezione> proiezione = proiezioneService.getProiezioneById(id);
        if (proiezione.isPresent()) {
            return ResponseEntity.ok(proiezione.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proiezione non trovata");
        }
    }


    //paginate e future
    @GetMapping("/future")
    public ResponseEntity<?> getFutureProiezioni(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        Page<Proiezione> futureProjections = proiezioneService.getFutureProiezioni(page, size);
        return ResponseEntity.ok(futureProjections);
    }

    @GetMapping("/betweenDatesPaged")
    public ResponseEntity<?> getProiezioniBetweenDates(
            @RequestParam("inizio") String inizioString,
            @RequestParam("fine") String fineString,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar inizio = Calendar.getInstance();
            inizio.setTime(sdf.parse(inizioString));

            Calendar fine = Calendar.getInstance();
            fine.setTime(sdf.parse(fineString));
            return ResponseEntity.ok(proiezioneService.showProiezioniBetween(inizio, fine, page, size));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato data non valido. Usa dd-MM-yyyy.");
        }
    }

    @GetMapping("/byDatePaged")
    public ResponseEntity<?> getProiezioniByDatePaged(@RequestParam("data") String dataString,
                                                      @RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "15") int size) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Calendar dataCercata = Calendar.getInstance();
            dataCercata.setTime(sdf.parse(dataString));
            return ResponseEntity.ok(proiezioneService.showProiezioniByData(dataCercata, page, size));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato data non valido. Usa dd-MM-yyyy.");
        }

    }

}
