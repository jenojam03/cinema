package org.example.cinema.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import org.example.cinema.dto.BigliettoDTO;
import org.example.cinema.entities.*;
import org.example.cinema.exceptions.*;
import org.example.cinema.support.StatoBiglietto;
import org.example.cinema.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static final Double PREZZO_BASE = 8.50;


    @Transactional(noRollbackFor = PagamentoFallitoException.class)
    public List<Biglietto> acquistaBiglietti(String emailCliente, Long idProiezione, List<Long> idPosti) {

        Cliente cliente = clienteRepository.findByEmail(emailCliente);
        if (cliente == null) {
            throw new ClienteNotFoundException("Cliente non trovato!");
        }

        Proiezione proiezione = proiezioneRepository.findById(idProiezione)
                .orElseThrow(() -> new ProiezioneNotFoundException("Proiezione non trovata"));

        Map<Long, Posto> postiBloccati = new HashMap<>();
        for (Long postoId : idPosti) {
            Posto p = entityManager.find(Posto.class, postoId, LockModeType.PESSIMISTIC_WRITE);

            //Posto p = postoRepository.findById(postoId).orElseThrow(() -> new PostoNotFoundException("Il posto non è valido"));
            //entityManager.lock(p, LockModeType.PESSIMISTIC_WRITE);

            if(p == null) {
                throw new PostoNotFoundException("Posto non trovato!");
            }
            if(!p.getProiezione().equals(proiezione)) {
                throw new RuntimeException("Il posto non appartiene alla proiezione corretta");
            }
            if(p.getProiezione().getDataOra().after(LocalDate.now())) {
                throw new RuntimeException("La proiezione è già iniziata");
            }
            if (p.isOccupato()) {
                throw new RuntimeException("Il posto con ID " + postoId + " è già prenotato.");
            }
            if(p.getDataBloccoTemporaneo() != null && p.getDataBloccoTemporaneo().isAfter(LocalDateTime.now().minusMinutes(5))){
                throw new RuntimeException("Posto temporaneamente riservato. Riprova tra qualche minuto");
            }

            //blocco temporaneamente la prenotazione
            p.setDataBloccoTemporaneo(LocalDateTime.now());
            postiBloccati.put(postoId, p);

        }

        List<Biglietto> biglietti = new ArrayList<>();
        Transazione transazione = new Transazione();

        for (Long postoID : idPosti) {
            Biglietto b = new Biglietto();
            b.setCliente(cliente);
            //aggiungo biglietto anche al cliente
            cliente.getBiglietti().add(b);
            b.setProiezione(proiezione);
            b.setPosto(postiBloccati.get(postoID));
            b.setStato(StatoBiglietto.ATTIVO);
            b.setTransazione(transazione);
            b.setDataAcquisto(Calendar.getInstance());
            transazione.getBiglietti().add(b);

            //PREZZO
            b.setPrezzo(PREZZO_BASE); //potrei avere un campo prezzo in Proiezione e scrivere quindi proiezione.getPrezzo()
            bigliettoRepository.save(b);
            biglietti.add(b);
        }

        transazione.setData(Calendar.getInstance());
        transazione.setCliente(cliente);
        //aggiungo la transazione anche al cliente
        cliente.getTransazioni().add(transazione);
        transazione.setImporto(calcolaImporto(biglietti));
        transazione.setEsito(false);
        transazioneRepository.save(transazione);
        clienteRepository.save(cliente);

        //ESITO POSITIVO CON 80% DI PROBABILITA' PER SIMULARE IMPREVISTI
        boolean esitoPagamento = processaPagamento(transazione.getImporto());

        if(esitoPagamento){
            transazione.setEsito(true);
            transazioneRepository.save(transazione);//aggiorna esito

            for(Biglietto biglietto : biglietti){
                bigliettoRepository.save(biglietto);
                Posto p = biglietto.getPosto();
                p.setOccupato(true);
                p.setDataBloccoTemporaneo(null);
                postoRepository.save(p);

            }
        }else{
            for(Biglietto biglietto : biglietti){
                biglietto.setStato(StatoBiglietto.ANNULLATO);
                bigliettoRepository.save(biglietto);
                //entityManager.lock(biglietto.getPosto(), LockModeType.PESSIMISTIC_WRITE);
                Posto p = biglietto.getPosto();
                p.setDataBloccoTemporaneo(null);
                p.setOccupato(false);
                postoRepository.save(p);
            }

            throw new PagamentoFallitoException("Il pagamento è fallito, riprovare.");

        }

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
            System.out.println("Pagamento fallito. Riprovare");
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
    public List<BigliettoDTO> getBigliettiCliente(String email) throws ClienteNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email);
        if(cliente == null)
            throw new ClienteNotFoundException("Cliente non trovato!");

        System.out.println("Cliente trovato: " + cliente.getEmail());

        //recupero gli ordini del cliente
        List<Biglietto> biglietti = bigliettoRepository.findByCliente_IdOrderByDataAcquistoDesc(cliente.getId());
        if (biglietti.isEmpty()) {
            System.out.println("Nessun biglietto trovato per il cliente con email: " + email);
        }

        //creazione della lista di DTO per gli ordini
        List<BigliettoDTO> bigliettiDTO = new ArrayList<>();
        for (Biglietto biglietto : biglietti) {
            BigliettoDTO dto = new BigliettoDTO();

            dto.setIdBiglietto(biglietto.getId());
            dto.setDataAcquisto(biglietto.getDataAcquisto());
            dto.setStato(biglietto.getStato());
            dto.setProiezione(biglietto.getProiezione());
            dto.setPosto(biglietto.getPosto());
            dto.setPrezzo(biglietto.getPrezzo());
            bigliettiDTO.add(dto);


            //log per ogni biglietto trovato
            System.out.println("Biglietto trovato: ID = " + biglietto.getId() + ", Stato = " + biglietto.getStato());
        }
        return bigliettiDTO;
    }



    @Transactional(readOnly = true)
    public List<Biglietto> getBigliettiClientePeriodo(String email, Calendar da, Calendar a)
            throws ClienteNotFoundException, DateWrongRangeException {
        Cliente cliente = clienteRepository.findByEmail(email);
        if(cliente == null)
            throw new ClienteNotFoundException("Cliente non trovato!");
        if ( da.compareTo(a) >= 0 ) {
            throw new DateWrongRangeException();
        }
        return bigliettoRepository.findByClienteIdAndDataAcquistoBetween(cliente.getId(), da, a);
    }

    @Transactional(readOnly = true)
    public List<Biglietto> getTuttiIBiglietti() {
        return bigliettoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Biglietto> getTuttiIBigliettiOrdered() {
        return bigliettoRepository.findAllByOrderByDataAcquistoDesc();
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

        //annullamento
        biglietto.setStato(StatoBiglietto.ANNULLATO);
        bigliettoRepository.save(biglietto);

        //libera il posto
        Posto posto = biglietto.getPosto();
        posto.setOccupato(false);
        postoRepository.save(posto);

        //rimborso
        rimborsoUtente(biglietto);
        }


    private boolean rimborsoUtente(Biglietto biglietto) {
        boolean ret = false;
        long minutiDifferenza = (biglietto.getProiezione().getDataOra().getTimeInMillis() - Calendar.getInstance().getTimeInMillis()) / (1000 * 60);

        if (minutiDifferenza >= 60) {
            double importoRimborso = biglietto.getPrezzo();
            boolean ok = eseguiRimborso(biglietto.getCliente(), importoRimborso);

            if (ok) {
                biglietto.setStato(StatoBiglietto.RIMBORSATO);
                bigliettoRepository.save(biglietto);
            } else {
                throw new RuntimeException("Errore durante il rimborso.");
            }
        } else {
            //niente rimborso
            System.out.println("Nessun rimborso: meno di 60 minuti alla proiezione.");
        }
        return ret;
    }

    private boolean eseguiRimborso(Cliente cliente, double importo) {
        System.out.println("Rimborso in corso per " + cliente.getEmail() + " di " + importo + "€");
        double d = new Random().nextDouble();
        return d >= 0.2;
    }

    @Transactional(readOnly = true)
    public List<Posto> getPostiOccupati(Proiezione proiezione) {
        return postoRepository.findPostiOccupatiByProiezione(proiezione);
    }

    public Biglietto getBigliettoById(Long id) {
        return bigliettoRepository.findById(id).orElse(null);
    }


}
