package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.SortType;

import java.util.List;

@Service
public interface FilmStorage {

    Film findFilm(long id);

    List<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getPopularFilms(int count);

    List<Film> getFilmsByDirector(long directorId, SortType sortType);
}
