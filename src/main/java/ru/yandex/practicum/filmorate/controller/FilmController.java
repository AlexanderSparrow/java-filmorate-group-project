package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

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

    @PutMapping(value = "{id}/like/{userId}")
    public Film setLikeToMovie(@PathVariable Long id, @PathVariable Long userId) {
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, id);
        return filmService.setLikeToMovie(id, userId);
    }

    @DeleteMapping(value = "{id}/like/{userId}")
    public Film removeLikeFromMovie(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id {} удалил лайк фильму с id {}", userId, id);
        return filmService.removeLikeFromMovie(id, userId);
    }

    @GetMapping(value = "/genre/{id}")
    public List<Genre> getGenresForFilm(@PathVariable long id) {
        log.info("Запрос на получение жанров для фильма с id {}", id);
        return filmService.getGenresForFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Integer year
    ) {
        log.info("Получение запроса популярных фильмов с параметрами count={}, genreId={}, year={}", count, genreId, year);
        return filmService.getPopularFilms(count, genreId, year);
    }


}
