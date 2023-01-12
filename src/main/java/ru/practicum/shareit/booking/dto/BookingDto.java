package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingDto {

    private Long id;

    @NotNull
    private ItemDtoFofBooking item;

    @NotNull
    private UserDtoForBooking booker;

    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @NotNull
    private Status status;

    public void setBooker(User user) {
        this.booker = new UserDtoForBooking(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public void setItem(Item item) {
        this.item = new ItemDtoFofBooking(
                item.getId(),
                item.getName(),
                item.getDescription()
        );
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Data
    private class ItemDtoFofBooking {

        private final Long id;
        private final String name;
        private final String description;
    }

    @Data
    private class UserDtoForBooking {

        private final Long id;
        private final String email;
        private final String name;
    }
}
