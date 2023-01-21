package ru.practicum.shareit.common.exceptions;

public class EmailIncorrectException extends RuntimeException {

    public EmailIncorrectException(String message) {
        super(message);
    }
}
