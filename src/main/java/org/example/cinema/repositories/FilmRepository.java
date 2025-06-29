package org.example.cinema.repositories;

import org.example.cinema.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {

    Film findByTitoloIgnoreCase(String title);

    List<Film> findByGenereIgnoreCase(String genere);

    List<Film> findByRegistaIgnoreCase(String regista);

    List<Film> findByDurataBetween(int min, int max);

}
