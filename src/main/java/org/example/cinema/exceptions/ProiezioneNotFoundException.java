package org.example.cinema.exceptions;

public class ProiezioneNotFoundException extends RuntimeException {
    public ProiezioneNotFoundException(String message) {
        super(message);
    }
}
