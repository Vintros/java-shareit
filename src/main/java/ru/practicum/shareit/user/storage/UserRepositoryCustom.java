package ru.practicum.shareit.user.storage;

import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserRepositoryCustom {

    @Transactional
    @NonNull User updateUserById(Long id, UserDto userDto);
}
