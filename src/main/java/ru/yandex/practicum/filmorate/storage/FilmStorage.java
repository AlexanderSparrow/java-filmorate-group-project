package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
public interface FilmStorage {

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film findFilm(long id);

    List<Film> findAllFilms();
}
