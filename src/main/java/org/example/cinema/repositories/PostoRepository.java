package org.example.cinema.repositories;

import org.example.cinema.entities.Posto;
import org.example.cinema.entities.Proiezione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostoRepository extends JpaRepository<Posto, Long> {

    boolean existsByFilaAndNumero(String fila, int numero);
    Posto findByFilaAndNumero(String fila, int numero);
    Posto findById(long id);


    @Query("SELECT p FROM Posto p WHERE p.proiezione = :proiezione AND p.occupato = true")
    List<Posto> findPostiOccupatiByProiezione(@Param("proiezione") Proiezione proiezione);

    @Query("SELECT p FROM Posto p WHERE p.proiezione = :proiezione AND p.occupato = false")
    List<Posto> findPostiLiberiByProiezione(@Param("proiezione") Proiezione proiezione);
}
