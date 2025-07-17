package org.example.cinema.services;

import org.example.cinema.entities.Posto;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.repositories.BigliettoRepository;
import org.example.cinema.repositories.PostoRepository;
import org.example.cinema.repositories.ProiezioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
