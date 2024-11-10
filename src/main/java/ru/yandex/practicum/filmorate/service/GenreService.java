package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public Genre getGenre(long id) {
        return genreStorage.getGenre(id);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public List<Genre> getGenresForFilm(long filmId) {
        return genreStorage.getGenresForFilm(filmId);
    }
}
