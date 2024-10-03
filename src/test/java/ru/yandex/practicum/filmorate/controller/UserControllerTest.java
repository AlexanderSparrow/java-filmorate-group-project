package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Тестирование пользователя")
class UserControllerTest {
    private UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
    private User user;

    @BeforeEach
    public void beforeEach() {
        user = new User();
        user.setEmail("11@email");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());
    }

    @Test
    @DisplayName("Если почта не содерджит собаку, то ошибка")
    void ifNotContainsDogError() {
        user.setEmail("11email");
        assertThrows(ValidationExceptions.class, () -> userController.createUser(user));
    }

    @Test
    @DisplayName("Если указана пустая почта, то ошибка")
    void ifEmptyEmailError() {
        user.setEmail("");
        assertThrows(ValidationExceptions.class, () -> userController.createUser(user));
    }

    @Test
    @DisplayName("Если указана пустой логин, то ошибка")
    void ifEmptyLoginError() {
        user.setLogin("");
        assertThrows(ValidationExceptions.class, () -> userController.createUser(user));
    }

    @Test
    @DisplayName("Если Логин содерджит пробел, то ошибка")
    void ifContainsSpaceLoginError() {
        user.setLogin("login login");
        assertThrows(ValidationExceptions.class, () -> userController.createUser(user));
    }

    @Test
    @DisplayName("Если дата рождени позже текущей то ошибка")
    void ifBirthdayAfterNowError() {
        user.setBirthday(LocalDate.now().plusDays(1));
        assertThrows(ValidationExceptions.class, () -> userController.createUser(user));
    }

}
