package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.EventService;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FilmService filmService;
    private final EventService eventService;


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

    @GetMapping("/{userId}/feed")
    public List<Event> getUserEvents(@PathVariable Long userId) {
        return eventService.getUserEvents(userId);
    }


    @GetMapping (value = "/{id}/friends")
    @GetMapping(value = "/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        log.info("Получен запрос на получение друзей пользователя " + id);
        return userService.getFriends(id);
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

    @PutMapping(value = "/{id}/friends/{friendId}")
    public User addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Получен запрос на добавление друга от {} к {}", id, friendId);
        return userService.addFriend(id, friendId);
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public User removeFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Получен запрос на удаление друга {} у {}", friendId, id);
        return userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long friendId) {
        log.info("Получен запрос на получение общих друзей пользователя {} и {}", id, friendId);
        return userService.getCommonFriends(id, friendId);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteUser(@PathVariable long id) {
        log.info("Получен запрос на удаление пользователя {}", id);
        userService.deleteUser(id);
    }

    @GetMapping ("/{userId}/recommendations")
    public List<Film> getFilmRecommendations(@PathVariable long userId) {
        log.info("Получен запрос на получение рекомендаци для пользователя {}", userId);
        return filmService.getFilmRecommendations(userId);
    }
}

