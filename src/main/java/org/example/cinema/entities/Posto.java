package org.example.cinema.entities;

import jakarta.persistence.*;
import lombok.*;

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


    }


