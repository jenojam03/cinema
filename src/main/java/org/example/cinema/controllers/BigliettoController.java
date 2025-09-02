package org.example.cinema.controllers;

import org.example.cinema.dto.AcquistoRequest;
import org.example.cinema.dto.BigliettoDTO;
import org.example.cinema.entities.Biglietto;
import org.example.cinema.entities.Cliente;
import org.example.cinema.exceptions.BigliettoNotFoundException;
import org.example.cinema.exceptions.ClienteNotFoundException;
import org.example.cinema.exceptions.DateWrongRangeException;
//import org.example.cinema.jwt.CustomJwt;
import org.example.cinema.services.BigliettoService;
import org.example.cinema.services.ClienteService;
import org.example.cinema.support.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;

import static org.example.cinema.services.BigliettoService.PREZZO_BASE;

@RestController
@RequestMapping("/biglietti")
public class BigliettoController {

    @Autowired
    private BigliettoService bigliettoService;
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/prezzo")
    public ResponseEntity<?> prezzoTot(@RequestParam int numeroPosti) {
        double prezzoBase = PREZZO_BASE;
        double prezzoTot = numeroPosti*prezzoBase;
        return ResponseEntity.ok(prezzoTot);
    }

    @GetMapping("/prezzoBase")
    public ResponseEntity<?> prezzoBase() {
        return ResponseEntity.ok(PREZZO_BASE);
    }


    @PreAuthorize("isAuthenticated() and hasRole('cliente')")
    @PostMapping("/acquista")
    public ResponseEntity<Map<String, String>> acquistaBiglietti(@RequestBody @NotNull AcquistoRequest request) {
        //var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        //String email = jwt.getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaimAsString("email");
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
    @GetMapping("/di/{cliente}")
    public ResponseEntity<?> getBigliettiCliente(@PathVariable @Valid Cliente cliente) {
        try {
            List<BigliettoDTO> biglietti = bigliettoService.getBigliettiCliente(cliente.getEmail());
            return ResponseEntity.ok(biglietti);
        } catch (ClienteNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!", e);
        }
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{cliente}/{startDate}/{endDate}")
    public ResponseEntity<?> getBigliettiPeriodo(@Valid @PathVariable("cliente") Cliente cliente,
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
    @DeleteMapping("/{id}")
    public ResponseEntity<?> annullaBiglietto(@PathVariable Long id) {
        //var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        //String email = jwt.getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaimAsString("email");

        Cliente cliente = clienteService.getClienteByEmail(email);
        Biglietto biglietto = bigliettoService.getBigliettoById(id);

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
    @GetMapping("/all")
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
    @GetMapping("/miei_biglietti")
    public ResponseEntity<List<BigliettoDTO>> getMieiBiglietti() {
        //var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        //String email = jwt.getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaimAsString("email");

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<?> getBiglietto(@PathVariable Long id) {
        //var jwt = (CustomJwt) SecurityContextHolder.getContext().getAuthentication();
        //String email = jwt.getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String email = jwt.getClaimAsString("email");

        Biglietto biglietto = bigliettoService.getBigliettoById(id);

        if (biglietto == null) {
            return new ResponseEntity<>("Biglietto non trovato.", HttpStatus.NOT_FOUND);
        }

        //verifica che l'utente loggato sia il proprietario del biglietto
        if (!biglietto.getCliente().getEmail().equals(email) && !authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin"))) {
            return new ResponseEntity<>("Non hai i permessi per visualizzare questo biglietto.", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(biglietto, HttpStatus.OK);
    }
}
