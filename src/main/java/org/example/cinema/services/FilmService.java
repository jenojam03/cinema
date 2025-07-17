package org.example.cinema.services;

import jakarta.persistence.EntityNotFoundException;
import org.example.cinema.entities.Film;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.repositories.FilmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService {

    @Autowired
    private FilmRepository filmRepository;

    @Transactional(readOnly = true)
    public List<Film> showAllFilms() {
        return filmRepository.findAll();
    }

    //ricerca paginata
    @Transactional(readOnly = true)
    public List<Film> showAllFilms(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Film> pagedResult = filmRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<Film> showFilmsByTitolo(String titolo) {
        return filmRepository.findByTitoloContainingIgnoreCase(titolo);
    }

    @Transactional
    public Film creaFilm(Film film) {
        return filmRepository.save(film);
    }

    @Transactional(readOnly = true)
    public Film getFilmById(Long id) {
        return filmRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    public void eliminaFilm(Long id) {
        if (!filmRepository.existsById(id)) {
            throw new EntityNotFoundException("Film non trovato con id: " + id);
        }
        filmRepository.deleteById(id);
    }

    @Transactional
    public Film aggiornaFilm(Long id, Film modificato) {
        Film film = getFilmById(id);
        film.setTitolo(modificato.getTitolo());
        film.setDurata(modificato.getDurata());
        film.setGenere(modificato.getGenere());
        film.setRegista(modificato.getRegista());
        return filmRepository.save(film);
    }

    @Transactional
    public List<Proiezione> getProiezioniFilm(Long filmId) {
        Film film = getFilmById(filmId);
        return film.getProiezioni();
    }




}
