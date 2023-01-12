package ru.practicum.shareit.exceptions;

public class BookingTimeNotAllowedException extends RuntimeException {

    public BookingTimeNotAllowedException(String message) {
        super(message);
    }
}
