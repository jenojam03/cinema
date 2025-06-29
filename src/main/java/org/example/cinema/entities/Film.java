package org.example.cinema.entities;

import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import jakarta.persistence.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "film")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "film_id")
    private Long id;

    @Column(name = "titolo", nullable = false, length = 100)
    private String titolo;

    @Column(name = "durata", nullable = false)
    private int durata; //in minuti

    @Column(name = "genere", length = 50)
    private String genere;

    @Column(name = "regista", length = 100)
    private String regista;

    @OneToMany(mappedBy = "film")
    private List<Proiezione> proiezioni;
}
