package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {

    private Long id;
    private String email;
    private String name;

}
