package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DisplayName("Тестирование контроллера фильмов")
class FilmControllerTest {
    private final FilmController filmController;

    private Film film;

    @BeforeEach
    public void beforeEach() {
        film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(1L);
    }

    @Test
    @DisplayName("Если имя фильма пустое, то ошибка")
    void ifNameEmptyError() {
        film.setName("");
        assertThrows(ValidationExceptions.class, () -> filmController.createFilm(film));
    }

    @Test
    @DisplayName("Если описание фильма пустое, то ошибка")
    void ifDescriptionEmptyError() {
        film.setDescription("");
        assertThrows(ValidationExceptions.class, () -> filmController.createFilm(film));
    }

    @Test
    @DisplayName("Если описание фильма более 200 символов, то ошибка")
    void ifDescriptionMoreThan200Error() {
        film.setDescription("a".repeat(201));
        assertThrows(ValidationExceptions.class, () -> filmController.createFilm(film));
    }

    @Test
    @DisplayName("Если дата фильма ранее 28.12.1985, то ошибка")
    void ifReleaseDateBefore28121985Error() {
        film.setReleaseDate(LocalDate.of(1800,1,1));
        assertThrows(ValidationExceptions.class, () -> filmController.createFilm(film));
    }

    @Test
    @DisplayName("Если длительность фильма меньше 0, то ошибка")
    void ifDurationLessThan0Error() {
        film.setDuration((long) -1);
        assertThrows(ValidationExceptions.class, () -> filmController.createFilm(film));
    }
}