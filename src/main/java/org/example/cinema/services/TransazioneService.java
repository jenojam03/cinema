package org.example.cinema.services;

import org.example.cinema.entities.Biglietto;
import org.example.cinema.entities.Posto;
import org.example.cinema.entities.Transazione;
import org.example.cinema.repositories.BigliettoRepository;
import org.example.cinema.repositories.PostoRepository;
import org.example.cinema.repositories.TransazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TransazioneService {

    @Autowired
    private TransazioneRepository transazioneRepository;

    @Autowired
    private BigliettoRepository bigliettoRepository;

    @Autowired
    private PostoRepository postoRepository;


    @Transactional(readOnly = true)
    public List<Transazione> getTransazioni() {
        return transazioneRepository.findAllByOrderByDataDesc();
    }

    @Transactional(readOnly = true)
    public List<Transazione> getTransazioniByEsito(boolean esito) {
        return transazioneRepository.findByEsitoOrderByDataDesc(esito);
    }

    @Transactional(readOnly = true)
    public List<Transazione> getTransazioniByEmail(String email) {
        return transazioneRepository.findByClienteEmailOrderByDataDesc(email);
    }

    //metodo che riceve sia mail ed esito e gestisce casi nulli
}
