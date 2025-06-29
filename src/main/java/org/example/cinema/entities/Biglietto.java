package org.example.cinema.entities;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "biglietto", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"proiezione_id", "posto_id"})
        //non possono esistere due biglietti che hanno lo stesso posto nella stessa proiezione
})
@Getter @Setter @EqualsAndHashCode @ToString
public class Biglietto {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "biglietto_id")
    private Long id;

    @Column(nullable = false)
    private double prezzo;

    //relazione con CLIENTE
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    //relazione con PROIEZIONE
    @ManyToOne
    @JoinColumn(name = "proiezione_id")
    private Proiezione proiezione;

    //relazione con POSTO
    @ManyToOne
    @JoinColumn(name = "posto_id")
    private Posto posto;


}

