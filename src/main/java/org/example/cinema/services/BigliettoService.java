package org.example.cinema.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.example.cinema.entities.*;
import org.example.cinema.exceptions.BigliettoNotFoundException;
import org.example.cinema.exceptions.ClienteNotFoundException;
import org.example.cinema.exceptions.PostoNotFoundException;
import org.example.cinema.plus.StatoBiglietto;
import org.example.cinema.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BigliettoService {

    @Autowired
    private BigliettoRepository bigliettoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProiezioneRepository proiezioneRepository;

    @Autowired
    private PostoRepository postoRepository;

    @Autowired
    private TransazioneRepository transazioneRepository;

    @Autowired
    private EntityManager entityManager;


    @Transactional
    public List<Biglietto> acquistaBiglietti(String emailCliente, Long idProiezione, List<Long> idPosti)
            throws Exception {

        Cliente cliente = clienteRepository.findByEmail(emailCliente);
        if (cliente == null) {
            throw new ClienteNotFoundException("Cliente non trovato!");
        }

        Proiezione proiezione = proiezioneRepository.findById(idProiezione)
                .orElseThrow(() -> new RuntimeException("Proiezione non trovata"));


        for (Long postoId : idPosti) {
            Posto p = entityManager.find(Posto.class, postoId, LockModeType.PESSIMISTIC_WRITE);
            //Posto p = postoRepository.findById(postoId).orElseThrow(() -> new PostoNotFoundException("Il posto non è valido"));
            //entityManager.lock(p, LockModeType.PESSIMISTIC_WRITE);
            if (p.isOccupato()) {
                throw new RuntimeException("Il posto con ID " + postoId + " è già prenotato.");
            }
        }

        List<Biglietto> biglietti = new ArrayList<>();
        Transazione transazione = new Transazione();

        for (Long postoID : idPosti) {
            Biglietto b = new Biglietto();
            b.setCliente(cliente);
            b.setProiezione(proiezione);
            b.setPosto(postoRepository.findById(postoID).orElse(null));
            b.setStato(StatoBiglietto.ATTIVO);
            b.setTransazione(transazione);
            transazione.getBiglietti().add(b);
            b.setPrezzo(new Random().nextDouble(3,15));
            //bigliettoRepository.save(b);
            biglietti.add(b);
        }

        transazione.setData(Calendar.getInstance());
        transazione.setCliente(cliente);
        transazione.setImporto(calcolaImporto(biglietti));

        if (!processaPagamento(transazione.getImporto())) {
            throw new RuntimeException("Pagamento fallito");
        }

        boolean esitoPagamento = processaPagamento(transazione.getImporto());



        if(esitoPagamento){
            transazione.setEsito(true);
            for(Biglietto biglietto : biglietti){
                bigliettoRepository.save(biglietto);
                entityManager.lock(biglietto.getPosto(), LockModeType.PESSIMISTIC_WRITE);
                Posto p = biglietto.getPosto();
                p.setOccupato(true);
                postoRepository.save(p);

            }
        }else{
            transazione.setEsito(false);
            for(Biglietto biglietto : biglietti){
                biglietto.setStato(StatoBiglietto.ANNULLATO);
                bigliettoRepository.save(biglietto);
                entityManager.lock(biglietto.getPosto(), LockModeType.PESSIMISTIC_WRITE);
                Posto p = biglietto.getPosto();
                //p.setOccupato(false);
                //postoRepository.save(p);
            }

            throw new RuntimeException("Il pagamento è fallito, riprovare.");

        }

        transazioneRepository.save(transazione);
        return biglietti;
    }

    private boolean processaPagamento(double importo) {
        if(importo <= 0){
            System.out.println("Importo non valido");
            return false;
        }
        System.out.println("Pagamento in corso");
        System.out.println("Importo da pagare: " + importo);

        boolean ok = new Random().nextInt(100) < 80;

        if(ok){
            System.out.println("Pagamento effettuato con successo");
            return true;
        }else{
            System.out.println("Pagamento fallito");
            return false;
        }
    }

    private double calcolaImporto(List<Biglietto> biglietti) {
        double ret = 0;
        for(Biglietto b : biglietti) {
            ret += b.getPrezzo();
        }
        return ret;
    }

    @Transactional(readOnly = true)
    public List<Biglietto> getBigliettiCliente(String email) throws ClienteNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email);
        if(cliente == null)
            throw new ClienteNotFoundException("Cliente non trovato!");
        return bigliettoRepository.findByCliente_Id(cliente.getId());
    }

    @Transactional(readOnly = true)
    public List<Biglietto> getBigliettiClientePeriodo(String email, Calendar da, Calendar a)
            throws ClienteNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email);
        if(cliente == null)
            throw new ClienteNotFoundException("Cliente non trovato!");
        return bigliettoRepository.findByClienteIdAndDataAcquistoBetween(cliente.getId(), da, a);
    }

    @Transactional(readOnly = true)
    public List<Biglietto> getTuttiIBiglietti() {
        return bigliettoRepository.findAll();
    }

    @Transactional
    public void annullaBiglietto(Long idBiglietto) throws BigliettoNotFoundException {
        Biglietto biglietto = bigliettoRepository.findById(idBiglietto)
                .orElseThrow(() -> new BigliettoNotFoundException("Biglietto non trovato."));

        Calendar oraCorrente = Calendar.getInstance();
        if (biglietto.getProiezione().getDataOra().before(oraCorrente)) {
            throw new IllegalStateException("La proiezione è già iniziata. Non è possibile annullare.");
        }
        if (biglietto.getStato() != StatoBiglietto.ATTIVO) {
            throw new IllegalStateException("Solo i biglietti acquistati possono essere annullati.");
        }

        biglietto.setStato(StatoBiglietto.ANNULLATO);
        bigliettoRepository.save(biglietto);
        // eventuale logica per rimborso monetario qui

        biglietto.setStato(StatoBiglietto.RIMBORSATO);
        bigliettoRepository.save(biglietto);
    }

    @Transactional(readOnly = true)
    public List<Posto> getPostiOccupati(Proiezione proiezione) {
        return bigliettoRepository.findPostiOccupatiByProiezione(proiezione);
    }


}
