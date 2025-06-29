package org.example.cinema.services;

import org.example.cinema.entities.Biglietto;
import org.example.cinema.entities.Cliente;
import org.example.cinema.entities.Posto;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.exceptions.PostoNotFoundException;
import org.example.cinema.exceptions.ProiezioneNotFoundException;
import org.example.cinema.exceptions.UserNotFoundException;
import org.example.cinema.repositories.BigliettoRepository;
import org.example.cinema.repositories.ClienteRepository;
import org.example.cinema.repositories.PostoRepository;
import org.example.cinema.repositories.ProiezioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PrenotazioneService {

    @Autowired
    private BigliettoRepository bigliettoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProiezioneRepository proiezioneRepository;

    @Autowired
    private PostoRepository postoRepository;

    @Transactional
    public Biglietto prenotaPosto(Biglietto biglietto) {
        Cliente cliente = clienteRepository.findById(biglietto.getCliente().getId()).orElse(null);
        if (cliente == null) throw new UserNotFoundException("Cliente non trovato!");

        Proiezione proiezione = proiezioneRepository.findById(biglietto.getProiezione().getId());
        if(proiezione == null) throw new ProiezioneNotFoundException("Proiezione non trovata");

        Posto posto = postoRepository.findById(biglietto.getPosto().getId()).orElse(null);
        if(posto == null) throw new PostoNotFoundException("Posto non trovato");

        if (bigliettoRepository.existsByProiezione_IdAndPosto_Id(biglietto.getProiezione().getId(), biglietto.getPosto().getId())) {
            throw new IllegalStateException("Posto già prenotato per questa proiezione.");
        }

        return bigliettoRepository.save(biglietto);
    }

    //prendi i biglietti di un utente
    //tutti i biglietti
    //annulla biglietto con rimborso

    @Transactional
    public void annullaPrenotazione(Long bigliettoId) {
        if (!bigliettoRepository.existsById(bigliettoId)) {
            throw new NoSuchElementException("Biglietto non trovato.");
        }
        bigliettoRepository.deleteById(bigliettoId);
    }

    /**
     * Marca un biglietto come rimborsato.
     */
    @Transactional
    public void rimborsaBiglietto(Long bigliettoId) {
        Biglietto biglietto = bigliettoRepository.findById(bigliettoId)
                .orElseThrow(() -> new NoSuchElementException("Biglietto non trovato"));

        if (biglietto.isRimborsato()) {
            throw new IllegalStateException("Il biglietto è già stato rimborsato.");
        }

        biglietto.setRimborsato(true);
        bigliettoRepository.save(biglietto);
    }



}
