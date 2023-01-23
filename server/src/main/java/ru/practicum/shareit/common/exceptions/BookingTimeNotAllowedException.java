package ru.practicum.shareit.common.exceptions;

public class BookingTimeNotAllowedException extends RuntimeException {

    public BookingTimeNotAllowedException(String message) {
        super(message);
    }
}
