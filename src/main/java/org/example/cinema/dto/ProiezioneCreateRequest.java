package org.example.cinema.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.List;

@Getter
@Setter
public class ProiezioneCreateRequest {

    private Calendar dataOra;
    private int numeroPosti;
    private Long filmId;
    private List<PostoCreateRequest> posti;
}
