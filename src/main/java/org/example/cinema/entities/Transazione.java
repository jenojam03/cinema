package org.example.cinema.entities;
import jakarta.persistence.*;
import lombok.*;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter @Setter @EqualsAndHashCode @ToString
@Table(name = "transazione")
public class Transazione {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="transazione_id", nullable = false)
    private Long id;

    @Column(name = "data", nullable = false)
    private Calendar data;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "transazione", cascade = CascadeType.ALL)
    private List<Biglietto> biglietti = new LinkedList<>();

    @Column(name = "importo", nullable = false)
    private double importo;

    @Column(name = "esito")
    private boolean esito;
}
