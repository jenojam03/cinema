package org.example.cinema.repositories;

import org.example.cinema.entities.Transazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransazioneRepository extends JpaRepository<Transazione, Integer> {
    List<Transazione> findByClienteEmail(String email);
    List<Transazione> findByEsito(boolean esito);
}
