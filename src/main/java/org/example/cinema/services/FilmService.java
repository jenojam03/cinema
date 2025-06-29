package org.example.cinema.services;

import org.example.cinema.entities.Film;
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
}
