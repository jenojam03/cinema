package org.example.cinema.controllers;

import jakarta.persistence.EntityNotFoundException;
import org.example.cinema.dto.ClienteCreateRequest;
import org.example.cinema.dto.ClienteDTO;
import org.example.cinema.dto.ClienteUpdateRequest;
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
    public ResponseEntity<?> create(@RequestBody @Valid ClienteCreateRequest request) {
        try {
            ClienteDTO nuovo = clienteService.registraCliente(request);
            return ResponseEntity.ok(nuovo);
        } catch (MailUserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"));

        }
    }

    //@PreAuthorize(value="hasRole('admin')")
    @GetMapping(value="/search/{id}")
    public @ResponseBody ResponseEntity<ClienteDTO> readById(@PathVariable(value = "id") long id) throws ClienteNotFoundException {
        ClienteDTO cliente = clienteService.getCliente(id);
        if(cliente == null) { return ResponseEntity.notFound().build(); }
        return ResponseEntity.ok(cliente);
    }

    /*//@PreAuthorize(value="hasRole('admin')")
    @GetMapping(value="/search/by_name/{name}")
    public @ResponseBody List<ClienteDTO> readByName(@PathVariable(value = "name") String name) {
        return clienteService.getClientiByNome(name);
    }*/

    //@PreAuthorize(value="hasRole('admin')")
    @GetMapping(value="/search/by_surname/{surname}")
    public @ResponseBody List<ClienteDTO> readBySurname(@PathVariable(value = "surname") String surname) {
        return clienteService.getClientiByCognome(surname);
    }

    //@PreAuthorize(value="hasRole('admin')")
    @GetMapping(value="/search/by_name_surname/{name}/{surname}")
    public @ResponseBody List<ClienteDTO> readByNameSurname(@PathVariable(value = "name") String name, @PathVariable(value = "surname") String surname) {
        return clienteService.getClientiByNomeAndCognome(name,surname);
    }

    //@PreAuthorize(value="hasRole('admin')")
    @GetMapping("search/by_email")
    public ResponseEntity<?> getClienteByEmail(@RequestParam String email) {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null) {
            return ResponseEntity.status(404).body("Cliente non trovato.");
        }
        return ResponseEntity.ok(clienteService.toDTO(cliente));
    }

    //@PreAuthorize(value="hasRole('admin')")
    @GetMapping
    public List<ClienteDTO> getTuttiClienti() {
        return clienteService.getAllClienti();
    }

    @GetMapping("/me")
    public ResponseEntity<ClienteDTO> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        Cliente currentUser = clienteRepository.findByEmail(email);
        if (currentUser != null) {
            return ResponseEntity.ok(clienteService.toDTO(currentUser));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ClienteDTO> updateCurrentUser(@RequestBody @Valid ClienteUpdateRequest request, Authentication authentication) {
        String email = authentication.getName();
        Cliente currentUser = clienteRepository.findByEmail(email);
        if (currentUser != null) {
            currentUser.setNome(request.getNome());
            currentUser.setCognome(request.getCognome());
            clienteRepository.save(currentUser);
            return ResponseEntity.ok(clienteService.toDTO(currentUser));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    //@PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminaCliente(@PathVariable Long id) {
        try {
            clienteService.deleteCliente(id);
            return ResponseEntity.ok(new ResponseMessage("Cliente eliminato con successo."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }
}
