package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;
    private final EventStorage eventStorage;

    public User findUser(long id) {
        return userStorage.findUser(id);
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        userValidation(user);

        Event event = new Event();
        event.setUserId(user.getId());
        event.setEventType("USER");
        event.setOperation("ADD");
        event.setEntityId(user.getId());
        event.setTimeCreate(LocalDateTime.now());

        eventStorage.addEvent(event);

        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        userValidation(user);
        userStorage.findUser(user.getId());

        Event event = new Event();
        event.setUserId(user.getId());
        event.setEventType("USER");
        event.setOperation("UPDATE");
        event.setEntityId(user.getId());
        event.setTimeCreate(LocalDateTime.now());

        eventStorage.addEvent(event);

        return userStorage.updateUser(user);
    }

    public User addFriend(long id, long friendId) {
        userStorage.findUser(id);
        userStorage.findUser(friendId);
        if (id == friendId) {
            log.error("Нельзя добавить себя в друзья");
            throw new ValidationExceptions("Нельзя добавить себя в друзья");
        }
        friendshipStorage.addFriend(id, friendId);

        Event event = new Event();
        event.setUserId(id);
        event.setEventType("FRIEND");
        event.setOperation("ADD");
        event.setEntityId(friendId);
        event.setTimeCreate(LocalDateTime.now());

        eventStorage.addEvent(event);

        return userStorage.findUser(id);
    }

    public User removeFriend(long id, long friendId) {
        if (id == friendId) {
            log.error("Нельзя удалить себя из друзей");
            throw new ValidationExceptions("Нельзя удалить себя из друзей");
        }
        userStorage.findUser(id);
        userStorage.findUser(friendId);
        friendshipStorage.removeFriend(id, friendId);

        Event event = new Event();
        event.setUserId(id);
        event.setEventType("FRIEND");
        event.setOperation("REMOVE");
        event.setEntityId(friendId);
        event.setTimeCreate(LocalDateTime.now());

        eventStorage.addEvent(event);

        return userStorage.findUser(id);
    }

    public List<User> getFriends(long id) {
        userStorage.findUser(id);
        return friendshipStorage.getFriends(id);
    }

    public List<User> getCommonFriends(long id, long friendId) {
        userStorage.findUser(id);
        userStorage.findUser(friendId);
        return friendshipStorage.getCommonFriends(id, friendId);
    }

    private void userValidation(User newUser) {
        if (newUser == null) {
            log.error("Пользователь не указан");
            throw new ValidationExceptions("Пользователь не указан");
        }
        if (newUser.getEmail().isEmpty()) {
            log.error("Пользователь не указал email");
            throw new ValidationExceptions("Емэйл не указан");
        }
        if (!newUser.getEmail().contains("@")) {
            log.error("Пользователь указал неверный email");
            throw new ValidationExceptions("Емэйл не содержит @");
        }
        if (newUser.getLogin().isEmpty() || newUser.getLogin().contains(" ")) {
            log.error("Пользователь казал не верный логин");
            throw new ValidationExceptions("Логин не может быть пустым и содержать пробелы");
        }
        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.error("Пользователь указал неверную дату рождения");
            throw new ValidationExceptions("Дата рождения не может быть в будущем");
        }
    }
}
