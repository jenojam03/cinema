package org.example.cinema.entities;

import jakarta.persistence.*;
import lombok.*;

    @Entity
    @Table(name = "posto")
    @Getter @Setter @EqualsAndHashCode @ToString
    public class Posto {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, length = 5)
        private String fila;

        @Column(nullable = false)
        private int numero;

        @ManyToOne(optional = false)
        @JoinColumn(name = "proiezione_id")
        private Proiezione proiezione;
    }


