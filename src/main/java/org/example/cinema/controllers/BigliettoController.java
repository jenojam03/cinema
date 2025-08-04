package org.example.cinema.controllers;

import org.example.cinema.dto.AcquistoRequest;
import org.example.cinema.entities.Biglietto;
import org.example.cinema.entities.Cliente;
import org.example.cinema.exceptions.BigliettoNotFoundException;
import org.example.cinema.exceptions.ClienteNotFoundException;
import org.example.cinema.exceptions.DateWrongRangeException;
import org.example.cinema.services.BigliettoService;
import org.example.cinema.support.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
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
            return new ResponseEntity<>(biglietti, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{cliente}")
    public ResponseEntity<?> getBigliettiCliente(@PathVariable @Valid Cliente cliente) {
        try {
            List<Biglietto> biglietti = bigliettoService.getBigliettiCliente(cliente.getEmail());
            return ResponseEntity.ok(biglietti);
        } catch (ClienteNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }


    @GetMapping("/{cliente}/{startDate}/{endDate}")
    public ResponseEntity getBigliettiPeriodo(@Valid @PathVariable("cliente") Cliente cliente, @PathVariable("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy") String start, @PathVariable("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") String end) {
        try {
            Calendar inizio = parseDate(start);
            Calendar fine = parseDate(end);
            List<Biglietto> result = bigliettoService.getBigliettiClientePeriodo(cliente.getEmail(), inizio, fine);
            if (result.isEmpty()) {
                return new ResponseEntity<>(new ResponseMessage("No results!"), HttpStatus.OK);
            }
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (ClienteNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found XXX!", e);
        } catch (DateWrongRangeException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date must be previous end date XXX!", e);
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

    /*@PreAuthorize("isAuthenticated()")
    @GetMapping("/miei-biglietti")
    public ResponseEntity<List<bigliettoDTO>> getMieiBiglietti() {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();


        try {
            List<bigliettoDTO> ordini = bigliettoService.getBigliettiCliente(email);
            return ResponseEntity.ok(ordini);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }*/
}
