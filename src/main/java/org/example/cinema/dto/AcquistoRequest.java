package org.example.cinema.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class AcquistoRequest {

    private String emailCliente;

    private Long idProiezione;

    private List<Long> idPosti;


}

