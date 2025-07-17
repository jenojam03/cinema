package org.example.cinema.controllers;

import org.example.cinema.dto.AcquistoRequest;
import org.example.cinema.entities.Biglietto;
import org.example.cinema.exceptions.BigliettoNotFoundException;
import org.example.cinema.exceptions.ClienteNotFoundException;
import org.example.cinema.services.BigliettoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/biglietti")
public class BigliettoController {

    @Autowired
    private BigliettoService bigliettoService;

    @PostMapping("/acquista")
    public ResponseEntity<?> acquistaBiglietti(@RequestBody AcquistoRequest request) {
        try {
            List<Biglietto> biglietti = bigliettoService.acquistaBiglietti(
                    request.getEmailCliente(),
                    request.getIdProiezione(),
                    request.getIdPosti()
            );
            return ResponseEntity.ok(biglietti);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/cliente/{email}")
    public ResponseEntity<?> getBigliettiCliente(@PathVariable String email) {
        try {
            List<Biglietto> biglietti = bigliettoService.getBigliettiCliente(email);
            return ResponseEntity.ok(biglietti);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/cliente/{email}/periodo")
    public ResponseEntity<?> getBigliettiPeriodo(
            @PathVariable String email,
            @RequestParam String da,
            @RequestParam String finoA) {
        try {
            Calendar inizio = parseDate(da);
            Calendar fine = parseDate(finoA);
            List<Biglietto> biglietti = bigliettoService.getBigliettiClientePeriodo(email, inizio, fine);
            return ResponseEntity.ok(biglietti);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> annullaBiglietto(@PathVariable Long id) {
        try {
            bigliettoService.annullaBiglietto(id);
            return ResponseEntity.ok("Biglietto annullato con successo.");
        } catch (BigliettoNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public List<Biglietto> getTuttiIBiglietti() {
        return bigliettoService.getTuttiIBiglietti();
    }

    private Calendar parseDate(String date) {
        Calendar cal = Calendar.getInstance();
        LocalDate localDate = LocalDate.parse(date);
        cal.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        return cal;
    }
}
