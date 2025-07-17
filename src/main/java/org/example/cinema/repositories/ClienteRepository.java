package org.example.cinema.repositories;

import org.example.cinema.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNome(String nome);
    List<Cliente> findByCognome(String cognome);
    List<Cliente> findByNomeAndCognome(String nome, String cognome);
    Cliente findByEmail(String email);
    boolean existsByEmail(String email);




}
