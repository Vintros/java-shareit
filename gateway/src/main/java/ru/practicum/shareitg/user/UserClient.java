package ru.practicum.shareitg.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareitg.common.client.BaseClient;
import ru.practicum.shareitg.user.dto.UserRequestDto;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getDtoUsers() {
        return get();
    }

    public ResponseEntity<Object> getUserById(long id) {
        return get("/" + id);
    }

    public ResponseEntity<Object> deleteUserById(Long id) {
        return delete("/" + id);
    }

    public ResponseEntity<Object> createUser(UserRequestDto userRequestDto) {
        return post("", userRequestDto);
    }

    public ResponseEntity<Object> updateUser(Long id, UserRequestDto userRequestDto) {
        return patch("/" + id, userRequestDto);
    }
}

