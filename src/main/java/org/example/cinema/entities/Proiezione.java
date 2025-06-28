package org.example.cinema.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;

@Entity
@Table(name = "proiezione")
@Getter @Setter @EqualsAndHashCode @ToString
public class Proiezione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titoloFilm;

    @Column(nullable = false)
    private Calendar dataOra;

    @Column(nullable = false)
    private int numeroPosti; // totale posti disponibili
}

