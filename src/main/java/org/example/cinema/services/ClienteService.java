package org.example.cinema.services;

import org.example.cinema.dto.ClienteCreateRequest;
import org.example.cinema.dto.ClienteDTO;
import org.example.cinema.entities.Cliente;
import org.example.cinema.exceptions.ClienteNotFoundException;
import org.example.cinema.exceptions.MailUserAlreadyExistsException;
import org.example.cinema.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ClienteDTO registraCliente(ClienteCreateRequest req) throws MailUserAlreadyExistsException{
        if (clienteRepository.existsByEmail(req.getEmail()) ) {
            throw new MailUserAlreadyExistsException("L'utente è già registrato");
        }
        Cliente cliente = new Cliente();
        cliente.setNome(req.getNome());
        cliente.setEmail(req.getEmail());
        cliente.setCognome(req.getCognome());
        Cliente salvato = clienteRepository.save(cliente);
        return toDTO(salvato);
    }

    public ClienteDTO toDTO(Cliente cliente) {
        return new ClienteDTO(cliente.getId(), cliente.getNome(), cliente.getEmail());
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> getAllClienti() {
        return clienteRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteDTO getCliente(long id) {
        Cliente cliente =  clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente non trovato con id: " + id));
        return toDTO(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> getClientiByNome(String nome) {
        return clienteRepository.findByNome(nome)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> getClientiByCognome(String cognome) {
        return clienteRepository.findByCognome(cognome)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> getClientiByNomeAndCognome(String nome, String cognome) {
        return clienteRepository.findByNomeAndCognome(nome,cognome)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteCliente(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ClienteNotFoundException("Cliente non trovato con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Cliente getClienteByEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    @Transactional
    public Cliente saveCliente(Cliente c){
        return clienteRepository.save(c);
    }
}
