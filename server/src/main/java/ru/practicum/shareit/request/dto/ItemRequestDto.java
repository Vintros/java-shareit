package ru.practicum.shareit.request.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;

import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Validated
public class ItemRequestDto {

    private Long id;

    @NotNull
    private Long userId;

    @NotBlank(groups = IncomeRequest.class)
    private String description;

    @NotNull
    private LocalDateTime created;

    private final List<ItemDtoForRequest> items = new ArrayList<>();

    public void setItems(List<Item> items) {
        for (Item item : items) {
            this.items.add(new ItemDtoForRequest(
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getAvailable(),
                    item.getRequestId()
            ));
        }
    }

    @Data
    private class ItemDtoForRequest {

        private final Long id;
        private final String name;
        private final String description;
        private final Boolean available;
        private final Long requestId;
    }


}
