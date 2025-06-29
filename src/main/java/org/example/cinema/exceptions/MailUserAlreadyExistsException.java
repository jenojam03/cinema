package org.example.cinema.exceptions;

public class MailUserAlreadyExistsException extends RuntimeException {
    public MailUserAlreadyExistsException(String message) {
        super(message);
    }
}
