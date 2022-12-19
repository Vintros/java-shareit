package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.EmailIncorrectException;
import ru.practicum.shareit.exceptions.UserExistsException;
import ru.practicum.shareit.exceptions.UserNotExistsException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InMemoryUserStorage implements UserStorage {

    private Long id = 0L;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        checkUserNotExistsByEmail(user.getEmail());
        Long id = getNewId();
        user.setId(id);
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(Long id, UserDto userDto) {
        checkUserExistsById(id);
        checkUserNotExistsByEmail(userDto.getEmail());
        User oldUser = users.get(id);
        if (userDto.getEmail() != null) {
            checkCorrectEmail(userDto.getEmail());
            oldUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }
        return oldUser;
    }


    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        checkUserExistsById(id);
        return users.get(id);
    }

    @Override
    public void deleteUserById(Long id) {
        checkUserExistsById(id);
        users.remove(id);
    }

    private Long getNewId() {
        return ++id;
    }

    public void checkUserExistsById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotExistsException("such user not registered");
        }
    }

    private void checkUserNotExistsByEmail(String email) {
        if (users.values().stream()
                .map(User::getEmail)
                .anyMatch(e -> e.equals(email))
        ) {
            throw new UserExistsException("a user with such an email is already registered");
        }
    }

    private void checkCorrectEmail(String email) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        if (!email.matches(regexPattern)) {
            throw new EmailIncorrectException("incorrect email");
        }
    }

}
