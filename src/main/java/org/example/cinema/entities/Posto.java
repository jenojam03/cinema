package org.example.cinema.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Calendar;

@Entity
    @Table(name = "posto",uniqueConstraints = {
            @UniqueConstraint(columnNames = {"fila", "numero"})
            })
    @Getter @Setter @EqualsAndHashCode @ToString
    public class Posto {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "posto_id")
        private Long id;

        @Column(name = "fila", nullable = false, length = 5)
        private String fila;

        @Column(name = "numero", nullable = false)
        private int numero;

        @ManyToOne
        @JoinColumn(name = "proiezione_id")
        private Proiezione proiezione;

        @Column(name = "occupato", nullable = false)
        private boolean occupato;

        @Column(name = "blocco temporaneo")
        private LocalDateTime dataBloccoTemporaneo;

    }


