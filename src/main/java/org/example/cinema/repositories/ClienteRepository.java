package org.example.cinema.repositories;

import org.example.cinema.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNome(String nome);
    List<Cliente> findByCognome(String cognome);
    List<Cliente> findByNomeAndCognome(String nome, String cognome);
    List<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);




}
