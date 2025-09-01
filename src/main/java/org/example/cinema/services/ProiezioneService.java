package org.example.cinema.services;

import org.example.cinema.dto.ProiezioneCreateRequest;
import org.example.cinema.entities.Film;
import org.example.cinema.entities.Posto;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.exceptions.ProiezioneEsistenteException;
import org.example.cinema.exceptions.ProiezioneNotFoundException;
import org.example.cinema.repositories.BigliettoRepository;
import org.example.cinema.repositories.FilmRepository;
import org.example.cinema.repositories.PostoRepository;
import org.example.cinema.repositories.ProiezioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProiezioneService {

    @Autowired
    private ProiezioneRepository proiezioneRepository;

    @Autowired
    private PostoRepository postoRepository;

    @Autowired
    private BigliettoRepository bigliettoRepository;

    @Autowired
    private FilmRepository filmRepository;


    public List<Posto> getPostiDisponibili(Long proiezioneId) {
        Proiezione proiezione = proiezioneRepository.findById(proiezioneId)
                .orElseThrow(() -> new RuntimeException("Proiezione non trovata"));

        List<Posto> tuttiIPosti = postoRepository.findAll(); // oppure solo quelli assegnati a quella proiezione
        List<Posto> occupati = bigliettoRepository.findPostiOccupatiByProiezione(proiezione);

        return tuttiIPosti.stream()
                .filter(p -> !occupati.contains(p))
                .toList();
    }

    /*@Transactional
    public void aggiungiProiezione(Proiezione proiezione) {
        if (proiezione.getId() != 0 && proiezioneRepository.existsById(proiezione.getId())) {
            throw new ProiezioneEsistenteException("La proiezione inserita esiste già");
        }

        proiezioneRepository.save(proiezione);
    }*/

    @Transactional
    public void aggiungiProiezione(ProiezioneCreateRequest request) {
        Optional<Film> film = filmRepository.findById(request.getFilmId());
        if (film.isEmpty()) {
            throw new IllegalArgumentException("Non esiste nessun film con questo ID.");
        }

        Proiezione proiezione = new Proiezione();
        proiezione.setDataOra(request.getDataOra());
        proiezione.setNumeroPosti(request.getNumeroPosti());
        proiezione.setFilm(film.get());
        film.get().getProiezioni().add(proiezione);

        //salvo l'id della proiezione
        Proiezione savedProiezione = proiezioneRepository.save(proiezione);

        List<Posto> posti = request.getPosti().stream().map(postoDto -> {
            Posto posto = new Posto();
            posto.setProiezione(savedProiezione);
            posto.setFila(postoDto.getFila());
            posto.setNumero(postoDto.getNumero());
            posto.setOccupato(false);
            return posto;
        }).toList();

        postoRepository.saveAll(posti);
        savedProiezione.setPosti(posti);
        filmRepository.save(film.get());

    }

    @Transactional(readOnly = true)
    public List<Proiezione> showAllProiezioni() {
        return proiezioneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Proiezione> showAllProiezioniOrdered() {
        return proiezioneRepository.findAllByOrderByDataOraAsc();
    }

    @Transactional(readOnly = true)
    public Optional<Proiezione> getProiezioneById(Long id) {
        return proiezioneRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Proiezione> showProiezioniByFilm(Long filmId) {
        return proiezioneRepository.findByFilm_Id(filmId);
    }

    @Transactional(readOnly = true)
    public List<Proiezione> showProiezioniByFilmOrdered(Long filmId) {
        return proiezioneRepository.findByFilm_IdOrderByDataOraAsc(filmId);
    }

    @Transactional(readOnly = true)
    public List<Proiezione> showAllProiezioni(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Proiezione> pagedResult = proiezioneRepository.findAll(paging);
        if (pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional
    public void deleteProiezione(Long id) {
        Proiezione proiezione = proiezioneRepository.findById(id)
                .orElseThrow(() -> new ProiezioneNotFoundException("Proiezione non trovata"));
        Film film = proiezione.getFilm();
        film.getProiezioni().remove(proiezione);
        filmRepository.save(film);
        proiezioneRepository.delete(proiezione);
    }

    //posso modificare solo la data di una proiezione già immessa nel db
    @Transactional
    public void aggiornaProiezione(Long id, Calendar nuovaDataOra) {
        Proiezione existingProiezione = proiezioneRepository.findById(id)
                .orElseThrow(() -> new ProiezioneNotFoundException("Proiezione non trovata con ID: " + id));

        if (nuovaDataOra != null) {
            existingProiezione.setDataOra(nuovaDataOra);
        }

        proiezioneRepository.save(existingProiezione);
    }

    @Transactional(readOnly = true)
    public Object getPosti(Long proiezioneId) {
        Proiezione p = proiezioneRepository.findProiezioneById(proiezioneId);
        return p.getPosti();
    }


    //FUTURE
    @Transactional(readOnly = true)
    public List<Proiezione> showAllProiezioniOrderedFuture() {
        return proiezioneRepository.findByDataOraAfterOrderByDataOraAsc(Calendar.getInstance());
    }

    @Transactional(readOnly = true)
    public List<Proiezione> getFutureProiezioniByFilmId(Long filmId) {
        return proiezioneRepository.findByFilmIdAndDataOraAfterOrderByDataOraAsc(filmId, Calendar.getInstance());
    }

    //PAGINATE
    @Transactional(readOnly = true)
    public Page<Proiezione> getFutureProiezioni(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataOra").ascending());
        Page<Proiezione> pagedResult = proiezioneRepository.findByDataOraAfter(Calendar.getInstance(), pageable);
        /*if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }*/
        return pagedResult;

    }

    @Transactional(readOnly = true)
    public Page<Proiezione> getFutureProiezioniByFilm(Long filmId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataOra").ascending());
        Page<Proiezione> pagedResult = proiezioneRepository.findByFilmIdAndDataOraAfter(filmId, Calendar.getInstance(), pageable);
        /*if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }*/
        return pagedResult;
    }

    @Transactional(readOnly = true)
    public Page<Proiezione> showProiezioniBetween(Calendar inizio, Calendar fine, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataOra").ascending());
        Page<Proiezione> pagedResult = proiezioneRepository.findByDataOraBetween(inizio, fine, pageable);
        /*if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }*/
        return pagedResult;
    }

    @Transactional(readOnly = true)
    public Page<Proiezione> showProiezioniByData(Calendar data, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("dataOra").ascending());
        Page<Proiezione> pagedResult = proiezioneRepository.findByDataOra(data, pageable);
        /*if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }*/
        return pagedResult;
    }


    @Transactional(readOnly = true)
    public List<Proiezione> getProiezioneByData(Calendar dataCercata) {
        return proiezioneRepository.findByDataOra(dataCercata);
    }

    @Transactional(readOnly = true)
    public List<Proiezione> getProiezioneBetween(Calendar inizio, Calendar fine){
        return proiezioneRepository.findByDataOraBetween(inizio,fine);
    }
}
