package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class Item {

    private Long id;
    private Long ownerId;
    private String name;
    private String description;
    private Boolean available;

}
