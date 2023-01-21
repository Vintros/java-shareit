package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    private Boolean available;

    private Long requestId;

    private BookingDtoForItem lastBooking;

    private BookingDtoForItem nextBooking;

    private final List<CommentDtoForItem> comments = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public void setLastBooking(Booking lastBooking) {
        this.lastBooking = new BookingDtoForItem(
                lastBooking.getId(),
                lastBooking.getBooker().getId());
    }

    public void setNextBooking(Booking nextBooking) {
        this.nextBooking = new BookingDtoForItem(
                nextBooking.getId(),
                nextBooking.getBooker().getId());
    }

    public void setComments(List<Comment> comments) {
        for (Comment comment : comments) {
            this.comments.add(new CommentDtoForItem(
                    comment.getId(),
                    comment.getText(),
                    comment.getAuthor().getName(),
                    comment.getCreated()));
        }
    }

    @Data
    private class CommentDtoForItem {

        private final Long id;
        private final String text;
        private final String authorName;
        private final LocalDateTime created;
    }

    @Data
    private class BookingDtoForItem {

        private final Long id;
        private final Long bookerId;
    }
}
