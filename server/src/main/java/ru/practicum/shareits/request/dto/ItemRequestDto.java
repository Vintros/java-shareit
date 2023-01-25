package ru.practicum.shareits.request.dto;

import lombok.Data;

import ru.practicum.shareits.item.model.Item;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ItemRequestDto {

    private Long id;
    private Long userId;
    private String description;
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
