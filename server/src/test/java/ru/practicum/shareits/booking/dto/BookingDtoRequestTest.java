package ru.practicum.shareits.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingDtoRequestTest {

    @Autowired
    private JacksonTester<BookingDtoRequest> json;

    @SneakyThrows
    @Test
    void testSerialize() {
        LocalDateTime time = LocalDateTime.of(2022, 1, 2, 3, 4, 5);
        var dto = new BookingDtoRequest();
        dto.setItemId(1L);
        dto.setStart(time.plusDays(1));
        dto.setEnd(time.plusDays(2));

        var result = json.write(dto);

        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathValue("$.start").isEqualTo(time.plusDays(1).toString());
        assertThat(result).extractingJsonPathValue("$.end").isEqualTo(time.plusDays(2).toString());
    }

}