package org.example.cinema.exceptions;

public class PostoNotFoundException extends RuntimeException {
    public PostoNotFoundException(String message) {
        super(message);
    }
}
