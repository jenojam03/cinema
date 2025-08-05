package org.example.cinema.repositories;

import org.example.cinema.entities.Biglietto;
import org.example.cinema.entities.Cliente;
import org.example.cinema.entities.Posto;
import org.example.cinema.entities.Proiezione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface BigliettoRepository extends JpaRepository<Biglietto, Long> {

    List<Biglietto> findByCliente_Id(Long clienteId);

    List<Biglietto> findByProiezione_Id(Long proiezioneId);

    boolean existsByProiezione_IdAndPosto_Id(Long proiezioneId, Long postoId);

    @Query("SELECT b.posto FROM Biglietto b WHERE b.proiezione = :proiezione")
    List<Posto> findPostiOccupatiByProiezione(@Param("proiezione") Proiezione proiezione);

    List<Biglietto> findByClienteIdAndDataAcquistoBetween(Long clienteId, Calendar dataInizio, Calendar dataFine);

    List<Biglietto> findByCliente_IdOrderByDataAcquistoDesc(Long clienteId);

    List<Biglietto> findAllByOrderByDataAcquistoDesc();

}

