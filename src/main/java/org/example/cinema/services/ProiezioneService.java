package org.example.cinema.services;

import org.example.cinema.entities.Film;
import org.example.cinema.entities.Posto;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.exceptions.ProiezioneEsistenteException;
import org.example.cinema.repositories.BigliettoRepository;
import org.example.cinema.repositories.PostoRepository;
import org.example.cinema.repositories.ProiezioneRepository;
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
public class ProiezioneService {

    @Autowired
    private ProiezioneRepository proiezioneRepository;

    @Autowired
    private PostoRepository postoRepository;

    @Autowired
    private BigliettoRepository bigliettoRepository;


    public List<Posto> getPostiDisponibili(Long proiezioneId) {
        Proiezione proiezione = proiezioneRepository.findById(proiezioneId)
                .orElseThrow(() -> new RuntimeException("Proiezione non trovata"));

        List<Posto> tuttiIPosti = postoRepository.findAll(); // oppure solo quelli assegnati a quella proiezione
        List<Posto> occupati = bigliettoRepository.findPostiOccupatiByProiezione(proiezione);

        return tuttiIPosti.stream()
                .filter(p -> !occupati.contains(p))
                .toList();
    }

    @Transactional
    public void aggiungiProiezione(Proiezione proiezione) {
        if (proiezione.getId() != 0 && proiezioneRepository.existsById(proiezione.getId())) {
            throw new ProiezioneEsistenteException("La proiezione inserita esiste già");
        }

        proiezioneRepository.save(proiezione);
    }

    @Transactional(readOnly = true)
    public List<Proiezione> showAllProiezioni() {
        return proiezioneRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Proiezione> showProiezioniByFilm(Film film) {
        return proiezioneRepository.findByFilm_Id(film.getId());
    }

    @Transactional(readOnly = true)
    public List<Proiezione> showAllProiezioni(int pageNumber, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Proiezione> pagedResult = proiezioneRepository.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            return new ArrayList<>();
        }
    }
}
