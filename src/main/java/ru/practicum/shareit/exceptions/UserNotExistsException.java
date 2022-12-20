package ru.practicum.shareit.exceptions;

public class UserNotExistsException extends RuntimeException {

    public UserNotExistsException(String message) {
        super(message);
    }
}
