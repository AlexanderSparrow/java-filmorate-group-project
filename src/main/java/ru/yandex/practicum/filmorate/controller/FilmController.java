package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping(value = "/{id}")
    public Film findFilm(@PathVariable long id) {
        log.info("Поиск фильма по id {}", id);
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
    public Film updateFilm(@Valid  @RequestBody Film newFilm) {
        log.info("Получен запрос на обновление фильма " + newFilm.getName());
        return filmService.updateFilm(newFilm);
    }

    @PutMapping (value = "{id}/like/{userId}")
    public Film setLikeToMovie(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь с id " + userId + " поставил лайк фильму с id " + id);
        return filmService.setLikeToMovie(id, userId);
    }

    @DeleteMapping (value = "{id}/like/{userId}")
    public Film removeLikeFromMovie(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь с id " + userId + " удалил лайк фильму с id " + id);
        return filmService.removeLikeFromMovie(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.info("Получение pапроса популярных фильмов");
        return filmService.getPopularFilms(count);
    }

}
