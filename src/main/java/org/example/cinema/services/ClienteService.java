package org.example.cinema.services;

import org.example.cinema.entities.Cliente;
import org.example.cinema.exceptions.MailUserAlreadyExistsException;
import org.example.cinema.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cliente registraCliente(Cliente cliente) throws MailUserAlreadyExistsException{
        if (clienteRepository.existsByEmail(cliente.getEmail()) ) {
            throw new MailUserAlreadyExistsException("L'utente è già registrato");
        }
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> getAllClienti() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente getCliente(long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Cliente> getClientiByNome(String nome) {
        return clienteRepository.findByNome(nome);
    }
}
