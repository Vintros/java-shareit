package ru.practicum.shareit.exceptions;

public class EmailIncorrectException extends RuntimeException {

    public EmailIncorrectException(String message) {
        super(message);
    }
}
