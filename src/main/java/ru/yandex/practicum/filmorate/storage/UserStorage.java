package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Service
public interface UserStorage {

    User findUser(Long id);

    List<User> findAllUsers();

    User createUser(User newUser);

    User updateUser(User newUser);

}
