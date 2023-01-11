package ru.practicum.shareit.user.storage;

import lombok.NonNull;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;

public interface UserRepositoryCustom {

    @Transactional
    @NonNull User updateUserById(Long id, UserDto userDto);
}
