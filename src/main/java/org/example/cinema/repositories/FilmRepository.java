package org.example.cinema.repositories;

import org.example.cinema.entities.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    List<Film> findByGenereIgnoreCase(String genere);

    List<Film> findByDurataBetween(int min, int max);

    List<Film> findByTitoloContainingIgnoreCase(String title);

    List<Film> findByRegistaContainingIgnoreCase(String autore);


    //PAGINATI
    Page<Film> findByTitoloContainingIgnoreCase(String title, Pageable pageable);
    Page<Film> findByRegistaContainingIgnoreCase(String autore, Pageable pageable);
    Page<Film> findByGenereIgnoreCase(String genere, Pageable pageable);


}
