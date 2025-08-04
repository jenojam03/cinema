package org.example.cinema.controllers;

import org.example.cinema.entities.Cliente;
import org.example.cinema.exceptions.ClienteNotFoundException;
import org.example.cinema.exceptions.MailUserAlreadyExistsException;
import org.example.cinema.support.ResponseMessage;
import org.example.cinema.repositories.ClienteRepository;
import org.example.cinema.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/clienti")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ClienteRepository clienteRepository;


    @PostMapping("/registrazione")
    public ResponseEntity create(@RequestBody @Valid Cliente cliente) {
        try {
            Cliente nuovo = clienteService.registraCliente(cliente);
            return new ResponseEntity(nuovo, HttpStatus.OK);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize(value="hasRole('admin')")
    @GetMapping(value="/search/{id}")
    public @ResponseBody Cliente readById(@PathVariable(value = "id") long id) throws ClienteNotFoundException {
        return clienteService.getCliente(id);
    }

    @PreAuthorize(value="hasRole('admin')")
    @GetMapping(value="/search/by_name/{name}")
    public @ResponseBody List<Cliente> readByName(@PathVariable(value = "name") String name) {
        return clienteService.getClientiByNome(name);
    }

    @PreAuthorize(value="hasRole('admin')")
    @GetMapping("search/by-email/{email}")
    public ResponseEntity<?> getClienteByEmail(@PathVariable String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            return ResponseEntity.status(404).body("Cliente non trovato.");
        }
        return ResponseEntity.ok(cliente);
    }

    @PreAuthorize(value="hasRole('admin')")
    @GetMapping
    public List<Cliente> getTuttiClienti() {
        return clienteService.getAllClienti();
    }

    @GetMapping("/me")
    public ResponseEntity<Cliente> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        Cliente currentUser = clienteRepository.findByEmail(email);
        if (currentUser != null) {
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Cliente> updateCurrentUser(@RequestBody @Valid Cliente updatedUser, Authentication authentication) {
        String email = authentication.getName();
        Cliente currentUser = clienteRepository.findByEmail(email);
        if (currentUser != null) {
            currentUser.setNome(updatedUser.getNome());
            clienteRepository.save(currentUser);
            return ResponseEntity.ok(currentUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
