package org.example.cinema.controllers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.websocket.server.PathParam;
import org.example.cinema.entities.Film;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.repositories.FilmRepository;
import org.example.cinema.services.FilmService;
import org.example.cinema.services.ProiezioneService;
import org.example.cinema.support.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/film")
public class FilmController {

    @Autowired
    private FilmService filmService;
    @Autowired
    private FilmRepository filmRepository;
    @Autowired
    private ProiezioneService proiezioneService;

    @GetMapping()
    public List<Film> getAllFilms() {
        return filmService.showAllFilms();
    }

    @GetMapping("/{id}/proiezioni")
    public ResponseEntity<?> getProiezioniPerFilm(@PathVariable Long id) {
       List<Proiezione> proiezioni = proiezioneService.showProiezioniByFilmOrdered(id);
        return ResponseEntity.ok(proiezioni);
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/create")
    public ResponseEntity<Film> creaFilm(@RequestBody Film film) {
        return ResponseEntity.ok(filmService.creaFilm(film));
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<?> aggiornaFilm(@PathVariable Long id, @RequestBody Film modificato) {
        try {
            return ResponseEntity.ok(filmService.aggiornaFilm(id, modificato));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }


    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminaFilm(@PathVariable Long id) {
        try {
            filmService.eliminaFilm(id);
            return ResponseEntity.ok(new ResponseMessage("Film eliminato con successo."));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(new ResponseMessage(e.getMessage()));
        }
    }

    @GetMapping("/search/nome/{nome}")
    public ResponseEntity<?> getFilmByTitolo(@PathVariable String nome)  {
        List<Film> ret = filmRepository.findByTitoloContainingIgnoreCase(nome);
        if(ret.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseMessage("Nessun film trovato"));
        }
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/search/autore/{autore}")
    public ResponseEntity<?> getFilmByAutore(@PathVariable String autore)  {
        List<Film> ret = filmRepository.findByRegistaContainingIgnoreCase(autore);
        if(ret.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseMessage("Nessun film trovato"));
        }
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/search/genere/{genere}")
    public ResponseEntity<?> getFilmByGenere(@PathVariable String genere)  {
        List<Film> ret = filmRepository.findByGenereIgnoreCase(genere);
        if(ret.isEmpty()) {
            return ResponseEntity.status(404).body(new ResponseMessage("Nessun film trovato"));
        }
        return ResponseEntity.ok(ret);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFilmById(@PathVariable Long id) {
        return ResponseEntity.ok(filmService.getFilmById(id));
    }
}
