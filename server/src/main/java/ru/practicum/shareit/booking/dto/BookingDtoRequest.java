package ru.practicum.shareit.booking.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class BookingDtoRequest {

    @NotNull
    private Long itemId;

    @NotNull
    @Future
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;
}
