package org.example.cinema.repositories;

import org.example.cinema.entities.Posto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostoRepository extends JpaRepository<Posto, Long> {

    boolean existsByFilaAndNumero(String fila, int numero);
    Posto findByFilaAndNumero(String fila, int numero);
    Posto findById(long id);


}
