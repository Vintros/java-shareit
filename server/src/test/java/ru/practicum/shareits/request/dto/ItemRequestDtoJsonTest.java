package ru.practicum.shareits.request.dto;

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

@JsonTest
class ItemRequestDtoJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        LocalDateTime time = LocalDateTime.now();
        User user = new User(5L, "mail@ya.ru", "name");
        Item item = new Item(1L, user, "item", "useful", true, List.of(), 10L);
        var dto = new ItemRequestDto();
        dto.setId(10L);
        dto.setUserId(2L);
        dto.setDescription("description");
        dto.setCreated(time);
        dto.setItems(List.of(item));

        var result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(10);
        assertThat(result).extractingJsonPathNumberValue("$.userId").isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("description");
        assertThat(result).hasJsonPath("$.created");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo("item");
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo("useful");
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isTrue();
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(10);
    }


}