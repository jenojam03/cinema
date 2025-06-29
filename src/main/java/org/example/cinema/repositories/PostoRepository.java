package org.example.cinema.repositories;

import org.example.cinema.entities.Posto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostoRepository extends JpaRepository<Posto, Long> {

    boolean existsByAndFilaAndNumero(Long proiezioneId, String fila, int numero);


}
