package ru.practicum.shareit.common.exceptions;

public class UserNameIncorrectException extends RuntimeException {

    public UserNameIncorrectException(String message) {
        super(message);
    }
}
