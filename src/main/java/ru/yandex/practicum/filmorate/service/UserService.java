package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

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
        return userStorage.updateUser(user);
    }

    public User addFriend(long id, long friendId) {
        User user = findUser(id);
        User friend = findUser(friendId);
        if (user == null) {
            log.error("Пользователь не найден");
            throw new NotFoundExceptions("Пользователь " + id + " не найден");
        }
        if (friend == null) {
            log.error("Пользователь не найден");
            throw new NotFoundExceptions("Пользователь " + friendId + "не найден");
        }
        if (user.getFriends() == null) {
            Set<Long> friendList = new HashSet<>();
            friendList.add(friendId);
            user.setFriends(friendList);
        } else {
            user.getFriends().add(friendId);
        }
        if (friend.getFriends() == null) {
            Set<Long> friendsList = new HashSet<>();
            friendsList.add(id);
            friend.setFriends(friendsList);
        } else {
            user.getFriends().add(friendId);
        }
        return user;
    }

    public User removeFriend(long id, long friendId) {
        User user = findUser(id);
        User friend = findUser(friendId);
        if (user == null) {
            log.error("Пользователь не найден");
            throw new NotFoundExceptions("Пользователь " + id + " не найден");
        }
        if(friend == null) {
            log.error("Пользователь не найден");
            throw new NotFoundExceptions("Пользователь " + friendId + "не найден");
        }
        if ((user.getFriends() != null) & (friend.getFriends() != null)) {
            if (user.getFriends().contains(friendId)) {
                user.getFriends().remove(friendId);
                friend.getFriends().remove(id);
                log.info("У пользователя {} удален друг  {}", user, friend);
                log.info("У пользователя {} удален друг  {}", friend, user);
            }
        }
        return user;
    }

     public List<User> getFriends(Long id) {
        User user = findUser(id);
        if (user == null) {
            log.error("Пользователь {} не найден", user.getId());
            throw new NotFoundExceptions("Пользователь c id " + id + " не найден");
        }
        List<User> friends = new ArrayList<>();
        if (user.getFriends() != null) {
            for (long idUser : findUser(id).getFriends()) {
                if (findUser(idUser) == null) {
                    log.error("Пользователь {} не найден", user.getId());
                    throw new NotFoundExceptions("Друг с id" + id + " не найден");
                } else {
                    friends.add(findUser(idUser));
                }
            }
        }
        return friends;
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        log.info("Получен запрос на получение общих друзей пользователя" + id + " " + otherId);
        User user = findUser(id);
        User other = findUser(otherId);
        if (user == null) {
            log.error("Пользователь не найден");
            throw new NotFoundExceptions("Пользователь " + id + " не найден");
        }
        if (other == null) {
            log.error("Пользователь не найден");
            throw new NotFoundExceptions("Пользователь " + otherId + "не найден");
        }
        List<User> friends = new ArrayList<>();
        for (long friendOfFriends: findUser(otherId).getFriends()) {
            if (findUser(friendOfFriends) != null) {
                if (user.getFriends().contains(friendOfFriends)) {
                    friends.add(findUser(friendOfFriends));
                }
            }
        }
        return friends;
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
