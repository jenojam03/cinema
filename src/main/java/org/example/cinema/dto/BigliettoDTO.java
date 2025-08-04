package org.example.cinema.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.cinema.entities.Posto;
import org.example.cinema.entities.Proiezione;
import org.example.cinema.support.StatoBiglietto;

import java.util.Calendar;

@Getter
@Setter
public class BigliettoDTO {

    private Long idBiglietto;
    private Calendar dataAcquisto;
    private StatoBiglietto stato;
    private double prezzo;
    private Proiezione proiezione;
    private Posto posto;

}
