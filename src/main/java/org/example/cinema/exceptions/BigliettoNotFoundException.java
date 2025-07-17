package org.example.cinema.exceptions;

public class BigliettoNotFoundException extends RuntimeException {
    public BigliettoNotFoundException(String message) {
        super(message);
    }
}
