package ru.practicum.shareits.booking.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BookingDtoRequest {

    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;
}
