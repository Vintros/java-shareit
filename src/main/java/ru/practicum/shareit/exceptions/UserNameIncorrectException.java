package ru.practicum.shareit.exceptions;

public class UserNameIncorrectException extends RuntimeException {

    public UserNameIncorrectException(String message) {
        super(message);
    }
}
