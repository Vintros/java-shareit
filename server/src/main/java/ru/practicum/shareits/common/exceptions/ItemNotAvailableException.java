package ru.practicum.shareits.common.exceptions;

public class ItemNotAvailableException extends RuntimeException {

    public ItemNotAvailableException(String message) {
        super(message);
    }
}
