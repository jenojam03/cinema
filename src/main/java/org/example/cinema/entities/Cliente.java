package org.example.cinema.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name ="cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;


    @Column(name = "nome", nullable = false, length = 50)
    private String nome;

    @Column(name = "cognome", nullable = false, length = 50)
    private String cognome;


    @Column(name = "telefono", nullable = true, length = 20, unique = true)
    private String telefono;


    @Column(name= "email",nullable = false, length = 90, unique = true)
    private String email;
}
