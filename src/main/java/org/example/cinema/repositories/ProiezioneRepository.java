package org.example.cinema.repositories;

import org.example.cinema.entities.Proiezione;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface ProiezioneRepository extends JpaRepository<Proiezione, Long> {
    List<Proiezione> findByFilm_Id(Long filmId);
    List<Proiezione> findByDataOra(Calendar dataOra);
    List<Proiezione> findByDataOraBetween(Calendar inizio, Calendar fine);
    List<Proiezione> findAllByOrderByDataOraAsc();
    List<Proiezione> findByFilm_IdOrderByDataOraAsc(Long filmId);
    Proiezione findProiezioneById(Long id);

    List<Proiezione> findByDataOraAfterOrderByDataOraAsc(Calendar dataOra);
    List<Proiezione> findByFilmIdAndDataOraAfterOrderByDataOraAsc(Long filmId, Calendar dataOra);


    //PAGINATI
    Page<Proiezione> findByDataOraAfter(Calendar dataOra, Pageable pageable);

    Page<Proiezione> findByFilmIdAndDataOraAfter(Long filmId, Calendar dataOra, Pageable pageable);

    Page<Proiezione> findByDataOraBetween(Calendar inizio, Calendar fine, Pageable pageable);

    Page<Proiezione> findByDataOra(Calendar dataOra, Pageable pageable);
}
