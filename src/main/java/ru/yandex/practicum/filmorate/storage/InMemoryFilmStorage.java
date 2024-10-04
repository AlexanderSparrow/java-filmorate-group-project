package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private Long filmId = 0L;

    private long getNextId() {
        return ++filmId;
    }

    @Override
    public Film findFilm(long id) {
        if (films.containsKey(id)) {
            return films.get(id);
        } else {
            throw new NotFoundExceptions("Фильм не найден");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film createFilm(Film newFilm) {
        newFilm.setId(getNextId());
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film != null) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                return film;
            } else {
                throw new NotFoundExceptions("Фильм не найден, не удалось обновить");
            }
        } else {
            throw new ValidationExceptions("Не переданы данные для обновления фильма");
        }
    }
}
