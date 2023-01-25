package ru.practicum.shareits.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareits.item.model.Item;
import ru.practicum.shareits.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.practicum.shareits.booking.enums.Status.APPROVED;

@JsonTest
class BookingDtoJsonTest {

    @Autowired
    private JacksonTester<BookingDto> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        LocalDateTime time = LocalDateTime.of(2022, 1, 2, 3, 4, 5);
        User user = new User(5L, "mail@ya.ru", "name");
        Item item = new Item(1L, user, "item", "useful", true, List.of(), 10L);
        var dto = new BookingDto();
        dto.setId(2L);
        dto.setItem(item);
        dto.setBooker(user);
        dto.setStart(time.plusDays(1));
        dto.setEnd(time.plusDays(2));
        dto.setStatus(APPROVED);

        var result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(2);
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.item.description").isEqualTo("useful");
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(5);
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo("mail@ya.ru");
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo("name");
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo(time.plusDays(1).toString());
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo(time.plusDays(2).toString());
        assertThat(result).extractingJsonPathValue("$.status").isEqualTo(APPROVED.toString());
    }
}