package org.example.cinema.repositories;

import org.example.cinema.entities.Biglietto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BigliettoRepository extends JpaRepository<Biglietto, Long> {

    List<Biglietto> findByCliente_Id(Long clienteId);

    List<Biglietto> findByProiezione_Id(Long proiezioneId);

    boolean existsByProiezione_IdAndPosto_Id(Long proiezioneId, Long postoId);
}
