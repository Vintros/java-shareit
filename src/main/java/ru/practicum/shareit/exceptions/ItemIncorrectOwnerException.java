package ru.practicum.shareit.exceptions;

public class ItemIncorrectOwnerException extends RuntimeException {

    public ItemIncorrectOwnerException(String message) {
        super(message);
    }
}
