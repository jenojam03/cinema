package org.example.cinema.entities;

import jakarta.persistence.*;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Entity
@Table(name = "proiezione")
@Getter @Setter @EqualsAndHashCode @ToString
public class Proiezione {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "proiezione_id")
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar dataOra;

    @Column(nullable = false)
    private int numeroPosti;

    @OneToMany(mappedBy = "proiezione", cascade = CascadeType.ALL)
    private List<Posto> posti = new LinkedList<>();

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;


}

