package org.example.cinema.entities;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "biglietto", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"proiezione_id", "posto_id"})
})
@Getter @Setter @EqualsAndHashCode @ToString
public class Biglietto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "proiezione_id")
    private Proiezione proiezione;

    @ManyToOne(optional = false)
    @JoinColumn(name = "posto_id")
    private Posto posto;

    @Column(nullable = false)
    private double prezzo;
}

