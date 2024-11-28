package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortType;

import java.util.Collection;
import java.util.List;

@Service
public interface FilmStorage {

    Film findFilm(long id);

    List<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getPopularFilms(int count, Long genreId, Integer year);

    void deleteFilm(long id);

    List<Film> getFilmsByDirector(long directorId, SortType sortType);

    List<Film> getCommonFilms(long userId, long friendId);

    List<Film> getFavoriteMovies(long userId);

    List<Film> findFilmIntersections(long userId, Collection<Long> filmsId);
}
