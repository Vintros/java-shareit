package ru.practicum.shareit.user;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class User {

    private Long id;

    @NonNull
    private String email;

    @NonNull
    private String name;

}
