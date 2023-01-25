package ru.practicum.shareits.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareits.booking.enums.Status;
import ru.practicum.shareits.booking.model.Booking;
import ru.practicum.shareits.item.model.Comment;
import ru.practicum.shareits.item.model.Item;
import ru.practicum.shareits.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ru.practicum.shareits.item.dto.ItemDto> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        LocalDateTime time = LocalDateTime.now();
        User user = new User(5L, "mail@ya.ru", "name");
        Item item = new Item(1L, user, "item", "description", true, List.of(), 10L);
        Comment comment = new Comment();
        comment.setId(20L);
        comment.setText("comment");
        comment.setItem(item);
        comment.setAuthor(user);
        comment.setCreated(time);

        var dto = new ItemDto();
        dto.setId(1L);
        dto.setName("item");
        dto.setDescription("description");
        dto.setAvailable(true);
        dto.setRequestId(2L);
        dto.setNextBooking(new Booking(3L, item, user, Status.APPROVED, time.minusDays(1), time.plusDays(2)));
        dto.setComments(List.of(comment));

        var result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).extractingJsonPathBooleanValue("$.available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(3);
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(5);
        assertThat(result).doesNotHaveJsonPath("$.lastBooking.id");
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(20);
    }
}