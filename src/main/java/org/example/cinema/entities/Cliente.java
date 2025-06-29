package org.example.cinema.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;



@Entity
@Table(name ="cliente")
@Getter @Setter @EqualsAndHashCode @ToString
public class Cliente {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cliente_id", nullable = false)
    private Long id;


    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "cognome", nullable = false, length = 50)
    private String cognome;


    @Column(name= "email",nullable = true, length = 90, unique = true)
    private String email;


    @OneToMany(mappedBy = "cliente")
    private List<Biglietto> biglietti;
}
