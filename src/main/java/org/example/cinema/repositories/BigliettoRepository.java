package org.example.cinema.repositories;

import org.example.cinema.entities.Biglietto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BigliettoRepository extends JpaRepository<Biglietto, Long> {

    List<Biglietto> findByCliente_Id(Long clienteId);

    List<Biglietto> findByProiezione_Id(Long proiezioneId);

    boolean existsByProiezione_IdAndPosto_Id(Long proiezioneId, Long postoId);
}
