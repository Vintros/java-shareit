package ru.practicum.shareits.user.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareits.user.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {

}
