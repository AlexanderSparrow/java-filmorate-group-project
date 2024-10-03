package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/{id}")
    public User findUser(@PathVariable long id) {
        log.info("Поиск пользователя по id {}", id);
        return userService.findUser(id);
    }

    @GetMapping
    public List<User> findAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return userService.findAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        log.info("Получен запрос на создание пользователя " + newUser.getLogin());
        return userService.createUser(newUser);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User newUser) {
        log.info("Получен запрос на обновление пользователя " + newUser.getLogin());
        return userService.updateUser(newUser);
    }

    @PutMapping (value = "/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Получен запрос на добавление друга {} к {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping (value = "/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Получен запрос на удаление друга {} у {}", friendId, id);
        return userService.removeFriend(id, friendId);
    }

    @GetMapping (value = "/{id}/friends")
    public List<User> getFriends(@PathVariable Long id) {
        log.info("Получен запрос на получение друзей пользователя " + id);
        return userService.getFriends(id);
    }

    @GetMapping ("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        log.info("Получен запрос на получение общих друзей пользователя {} и {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}
