package ru.practicum.shareit.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice("ru.practicum.shareit")
public class ErrorHandler {

    @ExceptionHandler({UserExistsException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse userAlreadyExists(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
    @ExceptionHandler({UserNotExistsException.class, ItemNotExistsException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotExists(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ItemIncorrectOwnerException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse incorrectOwner(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({EmailIncorrectException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectEmail(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
