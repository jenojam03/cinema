package org.example.cinema.repositories;

import org.example.cinema.entities.Proiezione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;

public interface ProiezioneRepository extends JpaRepository<Proiezione, Integer> {
    List<Proiezione> findByFilm_Id(Long filmId);
    List<Proiezione> findByDataOra(Calendar dataOra);
    List<Proiezione> findByDataOraBetween(Calendar inizio, Calendar fine);

}
