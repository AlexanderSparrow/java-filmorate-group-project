package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;

    public Film findFilm(Long id) {
        Film film = filmStorage.findFilm(id);
        film.setGenres(new LinkedHashSet<>(genreStorage.getGenresForFilm(id)));
        return film;
    }

    public List<Film> findAllFilms() {
        List<Film> films = filmStorage.findAllFilms();
        films.forEach(film -> film.setGenres(new LinkedHashSet<>(genreStorage.getGenresForFilm(film.getId()))));
        return films;
    }

    public Film createFilm(Film newFilm) {
        filmValidation(newFilm);
        return filmStorage.createFilm(newFilm);
    }

    public Film updateFilm(Film newFilm) {
        final Film film = filmStorage.findFilm(newFilm.getId());
        filmValidation(newFilm);
        final Mpa mpa = mpaStorage.findMpa(newFilm.getMpa().getId());
        final LinkedHashSet<Genre> genres = new LinkedHashSet<>(genreStorage.getGenresForFilm(newFilm.getId()));
        film.setName(newFilm.getName());
        film.setDescription(newFilm.getDescription());
        film.setReleaseDate(newFilm.getReleaseDate());
        film.setDuration(newFilm.getDuration());
        film.setMpa(mpa);
        film.setGenres(genres);
        filmStorage.updateFilm(film);
        return film;
    }

    public Film setLikeToMovie(long filmId, long userId) {
        filmStorage.findFilm(filmId);
        userStorage.findUser(userId);
        likeStorage.setLikeToMovie(filmId, userId);
        return filmStorage.findFilm(filmId);
    }

    public Film removeLikeFromMovie(long filmId, long userId) {
        filmStorage.findFilm(filmId);
        userStorage.findUser(userId);
        likeStorage.removeLikeFromMovie(filmId, userId);
        return filmStorage.findFilm(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    public List<Genre> getGenresForFilm(long filmId) {
        return genreStorage.getGenresForFilm(filmId);
    }

    private void filmValidation(Film newFilm) {
        if (newFilm == null) {
            log.error("Пользователь попытался создать новый фильм с пустым объектом");
            throw new ValidationExceptions("Необходимо заполнить все поля");
        }
        if (newFilm.getName().isEmpty()) {
            log.error("Пользователь попытался создать новый фильм с пустым названием");
            throw new ValidationExceptions("Название не должно быть пустым");
        }
        if (newFilm.getDescription().isEmpty() || newFilm.getDescription().length() > 200) {
            log.error("Пользователь попытался создать новый фильм с пустым описанием или длинной более 200 символов");
            throw new ValidationExceptions("Введите описание должной не более 200 символов");
        }
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.error("Пользователь попытался создать новый фильм с датой релиза ранее 28.12.1895 года");
            throw new ValidationExceptions("Дата выxода не может быть раньше 28.12.1895 года");
        }
        if (newFilm.getDuration() <= 0) {
            log.error("Пользователь попытался создать новый фильм с длительностью меньше 1 минуты");
            throw new ValidationExceptions("Длительность не может быть меньше 1 минуты");
        }
        if (newFilm.getMpa() == null) {
            log.error("Пользователь попытался создать новый фильм без рейтинга");
            throw new ValidationExceptions("Необходимо указать рейтинг");
        }


    }
}
