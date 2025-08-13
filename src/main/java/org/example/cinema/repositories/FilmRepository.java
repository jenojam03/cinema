package org.example.cinema.repositories;

import org.example.cinema.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    Film findByTitoloIgnoreCase(String title);

    List<Film> findByGenereIgnoreCase(String genere);

    List<Film> findByDurataBetween(int min, int max);

    List<Film> findByTitoloContainingIgnoreCase(String title);

    List<Film> findByRegistaContainingIgnoreCase(String autore);
}
