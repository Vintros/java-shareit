package ru.practicum.shareitg.common.exception;

public class BookingTimeNotAllowedException extends RuntimeException {

    public BookingTimeNotAllowedException(String message) {
        super(message);
    }
}
