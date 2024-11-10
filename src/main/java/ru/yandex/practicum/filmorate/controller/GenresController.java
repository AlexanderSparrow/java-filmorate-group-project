package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenresController {

    private final GenreService genreService;

    @GetMapping(value = "/{id}")
    public Genre getGenre(@PathVariable long id) {
       log.info("Запрос на получение жанра с id {}", id);
        return genreService.getGenre(id);
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        log.info("Запрос на получение всех жанров");
        return genreService.getAllGenres();
    }

    @GetMapping(value = "/film/{id}")
    public List<Genre> getGenresForFilm(long filmId) {
        log.info("Запрос на получение жанров для фильма с id {}", filmId);
        return genreService.getGenresForFilm(filmId);
    }
}
