package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {

    Genre getGenre(long id);

    List<Genre> getAllGenres();

    List<Genre> getGenresForFilm(long filmId);
}
