package org.example.cinema.support;


import org.example.cinema.entities.*;
import org.example.cinema.repositories.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.*;
/*
@Component
public class DataLoader implements CommandLineRunner {

    private final FilmRepository filmRepo;
    private final ClienteRepository clienteRepo;
    private final ProiezioneRepository proiezioneRepo;
    private final PostoRepository postoRepo;

    public DataLoader(FilmRepository filmRepo, ClienteRepository clienteRepo,
                      ProiezioneRepository proiezioneRepo, PostoRepository postoRepo) {
        this.filmRepo = filmRepo;
        this.clienteRepo = clienteRepo;
        this.proiezioneRepo = proiezioneRepo;
        this.postoRepo = postoRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // FILM
        Film film1 = new Film();
        film1.setTitolo("Il Signore degli Anelli");
        film1.setDurata(178);
        film1.setGenere("Fantasy");
        film1.setRegista("Peter Jackson");

        Film film2 = new Film();
        film2.setTitolo("Inception");
        film2.setDurata(148);
        film2.setGenere("Sci-Fi");
        film2.setRegista("Christopher Nolan");

        Film film3 = new Film();
        film3.setTitolo("La Vita è Bella");
        film3.setDurata(116);
        film3.setGenere("Drammatico");
        film3.setRegista("Roberto Benigni");

        filmRepo.saveAll(List.of(film1, film2, film3));

        // CLIENTI
        Cliente c1 = new Cliente();
        c1.setNome("Giada");
        c1.setCognome("Rossi");
        c1.setEmail("giada@example.com");

        Cliente c2 = new Cliente();
        c2.setNome("Luca");
        c2.setCognome("Bianchi");
        c2.setEmail("luca@example.com");

        Cliente c3 = new Cliente();
        c3.setNome("Marco");
        c3.setCognome("Verdi");
        c3.setEmail("marco@example.com");

        clienteRepo.saveAll(List.of(c1, c2, c3));

        // PROIEZIONI + POSTI
        for (Film film : List.of(film1, film2, film3)) {
            for (int i = 0; i < 2; i++) {
                Proiezione p = new Proiezione();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, i + 1);
                p.setDataOra(calendar);
                p.setNumeroPosti(10);
                p.setFilm(film);
                proiezioneRepo.save(p);

                // POSTI
                List<Posto> posti = new ArrayList<>();
                for (int j = 1; j <= 10; j++) {
                    Posto posto = new Posto();
                    posto.setFila("A");
                    posto.setNumero(j);
                    posto.setProiezione(p);
                    posto.setOccupato(false);
                    posto.setDataBloccoTemporaneo(null);
                    posti.add(posto);
                }
                postoRepo.saveAll(posti);
            }
        }
    }
}*/
