package ru.practicum.shareit.exceptions;

public class EntityNotExistsException extends RuntimeException {

    public EntityNotExistsException(String message) {
        super(message);
    }
}
