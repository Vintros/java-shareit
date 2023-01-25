package ru.practicum.shareits.booking.dto;

import lombok.*;
import ru.practicum.shareits.booking.enums.Status;
import ru.practicum.shareits.item.model.Item;
import ru.practicum.shareits.user.model.User;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;


@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookingDto {

    private Long id;

    private ItemDtoFofBooking item;

    private UserDtoForBooking booker;

    private LocalDateTime start;

    private LocalDateTime end;

    @Enumerated(value = EnumType.STRING)
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
