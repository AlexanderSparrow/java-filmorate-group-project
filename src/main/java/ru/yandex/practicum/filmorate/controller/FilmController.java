package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.SortType;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @GetMapping(value = "/{id}")
    public Film findFilm(@PathVariable long id) {
        log.info("Получен запрос на поиск фильма по id {}", id);
        return filmService.findFilm(id);
    }

    @GetMapping
    public List<Film> findAllFilms() {
        log.info("Получение pапроса всех фильмов");
        return filmService.findAllFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film newFilm) {
        log.info("Получен запрос на создание нового фильма " + newFilm.getName());
        return filmService.createFilm(newFilm);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма " + newFilm.getName());
        return filmService.updateFilm(newFilm);
    }

    @PutMapping (value = "/{id}/like/{userId}")
    public Film setLikeToMovie(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, id);
        return filmService.setLikeToMovie(id, userId);
    }

    @DeleteMapping (value = "/{id}/like/{userId}")
    public Film removeLikeFromMovie(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id {} удалил лайк фильму с id {}.", userId, id);
        return filmService.removeLikeFromMovie(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение запроса популярных фильмов");
        return filmService.getPopularFilms(count);
    }

    @GetMapping(value = "/genre/{id}")
    public List<Genre> getGenresForFilm(@PathVariable long id) {
        log.info("Запрос на получение жанров для фильма с id {}", id);
        return filmService.getGenresForFilm(id);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteFilm(@PathVariable long id) {
        log.info("Запрос на удаление фильма с id {},", id);
        filmService.deleteFilm(id);
    }
    
    @GetMapping(value = "/directors/{id}")
    public List<Director> getDirectorsForFilm(@PathVariable long id) {
        log.info("Запрос на получение режиссера(ов) для фильма с id {}", id);
        return filmService.getDirectorsForFilm(id);
    }

    @GetMapping(value = "/director/{directorId}")
    public List<Film> getDFilmsForDirector(@PathVariable long directorId, @RequestParam("sortBy") SortType sortBy) {
        log.info("Запрос на получение фильмов для режиссера с id {} с сортировкой", directorId);
        return filmService.getFilmByDirector(directorId, sortBy);
    }

    @GetMapping(value = "/common")
    public List<Film> getCommonFilms(@RequestParam long userId, @RequestParam long friendId) {
        log.info("Запрос на получение общих филмов у пользователя {} и {}", userId, friendId);
        return filmService.getCommonFilms(userId, friendId);
    }
}
