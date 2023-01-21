package ru.practicum.shareit.common.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@RestControllerAdvice("ru.practicum.shareit")
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({UserExistsException.class})
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public ErrorResponse userAlreadyExists(final RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({EntityNotExistsException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotExists(final RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({ItemAccessErrorException.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorResponse incorrectOwner(final RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({EmailIncorrectException.class, UserNameIncorrectException.class,
            ItemNotAvailableException.class, BookingTimeNotAllowedException.class, BookingAccessErrorException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectEmail(final RuntimeException e) {
        log.error(e.getMessage());
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectValidation(final MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ErrorResponse(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorResponse incorrectStatus(final MethodArgumentTypeMismatchException e) {
        log.error(e.getMessage());
        return new ErrorResponse("Unknown state: " + e.getValue());
    }

    @ExceptionHandler({Throwable.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse unexpectedError(final Throwable e) {
        log.error(e.getMessage());
        return new ErrorResponse("unexpected error");
    }
}
