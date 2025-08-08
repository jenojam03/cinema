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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void salvaTransazioneFallita(Transazione transazione, List<Biglietto> biglietti) {
        transazioneRepository.save(transazione);
        for (Biglietto b : biglietti) {
            bigliettoRepository.save(b);
            Posto p = b.getPosto();
            p.setDataBloccoTemporaneo(null);
            postoRepository.save(p);
        }
    }
}
