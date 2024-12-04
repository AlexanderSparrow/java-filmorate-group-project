package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.enums.EventTypes;
import ru.yandex.practicum.filmorate.model.enums.OperationTypes;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.Event;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;
    private final EventService eventService;

    public User findUser(long id) {
        return userStorage.findUser(id);
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        userValidation(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        userValidation(user);
        userStorage.findUser(user.getId());
        return userStorage.updateUser(user);
    }

    public void deleteUser(long id) {
        userStorage.findUser(id);
        userStorage.deleteUser(id);
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
        event.setEventType(EventTypes.FRIEND);
        event.setOperation(OperationTypes.ADD);
        event.setEntityId(friendId);
        event.setTimestamp(Instant.now().toEpochMilli());
        eventService.addEvent(event);
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
        event.setEventType(EventTypes.FRIEND);
        event.setOperation(OperationTypes.REMOVE);
        event.setEntityId(friendId);
        event.setTimestamp(Instant.now().toEpochMilli());

        eventService.addEvent(event);

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
