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
                .orElseThrow(() -> new EntityNotFoundException("Film non trovato con id: " + id));
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


    //PAGINATI
    @Transactional(readOnly = true)
    public Page<Film> getAllPaged(int page, int size){
        Pageable paging = PageRequest.of(page, size, Sort.by("titolo").ascending());
        Page<Film> pagedResult = filmRepository.findAll(paging);
        /*
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }*/
        return pagedResult;
    }


    @Transactional(readOnly = true)
    public Page<Film> getFilmByTitolo(String titolo, int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("titolo").ascending());
        Page<Film> pagedResult = filmRepository.findByTitoloContainingIgnoreCase(titolo, paging);
        /*if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }*/
        return pagedResult;
    }

    @Transactional(readOnly = true)
    public Page<Film> getFilmByGenere(String genere, int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("titolo").ascending());
        Page<Film> pagedResult = filmRepository.findByGenereIgnoreCase(genere, paging);
        /*if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }*/
        return pagedResult;
    }

    @Transactional(readOnly = true)
    public Page<Film> getFilmByRegista(String regista, int page, int size) {
        Pageable paging = PageRequest.of(page, size, Sort.by("titolo").ascending());
        Page<Film> pagedResult = filmRepository.findByRegistaContainingIgnoreCase(regista, paging);
        /*if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }*/
        return pagedResult;
    }


    @Transactional(readOnly = true)
    public List<Film> getFilmByTitoloContaining(String titolo) {
        return filmRepository.findByTitoloContainingIgnoreCase(titolo);
    }

    @Transactional(readOnly = true)
    public List<Film> getFilmByRegistaContaining(String regista) {
        return filmRepository.findByRegistaContainingIgnoreCase(regista);
    }

    @Transactional(readOnly = true)
    public List<Film> getFilmByGenere(String genere) {
        return filmRepository.findByGenereIgnoreCase(genere);
    }
}
