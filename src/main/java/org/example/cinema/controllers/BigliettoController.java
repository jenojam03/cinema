package org.example.cinema.controllers;

import org.example.cinema.dto.AcquistoRequest;
import org.example.cinema.dto.BigliettoDTO;
import org.example.cinema.entities.Biglietto;
import org.example.cinema.entities.Cliente;
import org.example.cinema.exceptions.BigliettoNotFoundException;
import org.example.cinema.exceptions.ClienteNotFoundException;
import org.example.cinema.exceptions.DateWrongRangeException;
import org.example.cinema.jwt.CustomJwt;
import org.example.cinema.repositories.BigliettoRepository;
import org.example.cinema.repositories.ClienteRepository;
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
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/biglietti")
public class BigliettoController {

    @Autowired
    private BigliettoService bigliettoService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private BigliettoRepository bigliettoRepository;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/acquista")
    public ResponseEntity<Map<String, String>> acquistaBiglietti(@RequestBody @NotNull AcquistoRequest request) {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();
        Map<String, String> response;

        try {
            List<Biglietto> biglietti = bigliettoService.acquistaBiglietti(
                    email, //prendo l'email dal token
                    request.getIdProiezione(),
                    request.getIdPosti()
            );
            response = new HashMap<>();
            response.put("message", "Acquisto effettuato con successo.");
            return ResponseEntity.ok(response);
        } catch (ClienteNotFoundException e) {
            response = new HashMap<>();
            response.put("error", "Utente non trovato.");
            return ResponseEntity.status(404).body(response);
        }catch(Exception e){
            response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{cliente}")
    public ResponseEntity<?> getBigliettiCliente(@PathVariable @Valid Cliente cliente) {
        try {
            List<BigliettoDTO> biglietti = bigliettoService.getBigliettiCliente(cliente.getEmail());
            return ResponseEntity.ok(biglietti);
        } catch (ClienteNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found!", e);
        }
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{cliente}/{startDate}/{endDate}")
    public ResponseEntity getBigliettiPeriodo(@Valid @PathVariable("cliente") Cliente cliente,
                                              @PathVariable("startDate") @DateTimeFormat(pattern = "dd-MM-yyyy")
                                              String start, @PathVariable("endDate") @DateTimeFormat(pattern = "dd-MM-yyyy") String end) {
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

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}/annulla")
    public ResponseEntity<?> annullaBiglietto(@PathVariable Long id) {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();
        Cliente cliente = clienteRepository.findByEmail(email);
        Biglietto biglietto = bigliettoRepository.findById(id).orElse(null);

        if (cliente == null || biglietto == null || !biglietto.getCliente().getId().equals(cliente.getId())) {
            return new ResponseEntity<>("Utente o Biglietto non trovati.", HttpStatus.NOT_FOUND);
        }
        try {
            bigliettoService.annullaBiglietto(id);
            return new ResponseEntity<>("Biglietto annullato con successo.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Errore durante l'annullamento del biglietto: " + e.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
    }
    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public List<Biglietto> getTuttiIBiglietti() {
        return bigliettoService.getTuttiIBigliettiOrdered();
    }

    private Calendar parseDate(String date) {
        Calendar cal = Calendar.getInstance();
        LocalDate localDate = LocalDate.parse(date);
        cal.set(localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth());
        return cal;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/miei-biglietti")
    public ResponseEntity<List<BigliettoDTO>> getMieiBiglietti() {
        var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        String email = jwt.getName();


        try {
            List<BigliettoDTO> ordini = bigliettoService.getBigliettiCliente(email);
            return ResponseEntity.ok(ordini);
        } catch (ClienteNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.emptyList());
        }
    }
}
