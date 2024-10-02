package ru.yandex.practicum.filmorate.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{

    @Getter
    private final Map<Long, User> users = new HashMap<>();
    private Long userId = 0L;

    private Long getNextId() {
        return ++userId;
    }

    @Override
    public User findUser(Long id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }else {
            throw new NotFoundExceptions("Пользователь с идентификатором " + id + " не найден");
        }
    }

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User createUser(User newUser) {
        newUser.setId(getNextId());
        if (newUser.getName() == null) {
            newUser.setName(newUser.getLogin());
        }
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User newUser) {
        if (users.get(newUser.getId()) != null) {
            User updatedUser = users.get(newUser.getId());
            updatedUser.setEmail(newUser.getEmail());
            updatedUser.setLogin(newUser.getLogin());
            if (newUser.getName() == null) {
                updatedUser.setName(newUser.getLogin());
            } else {
                updatedUser.setName(newUser.getName());
            }
            updatedUser.setBirthday(newUser.getBirthday());
            return updatedUser;
        } else {
        throw new NotFoundExceptions(" Пользователь с id " + newUser.getId() + " не найден");
        }
    }
}
