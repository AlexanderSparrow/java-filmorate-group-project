package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;
    private final EventService eventService;
    private final DirectorStorage directorStorage;

    public Film findFilm(Long id) {
        Film film = filmStorage.findFilm(id);
        film.setGenres(new LinkedHashSet<>(genreStorage.getGenresForFilm(id)));
        film.setDirectors(directorStorage.getDirectorsForFilm(id));
        return film;
    }

    public List<Film> findAllFilms() {
        List<Film> films = filmStorage.findAllFilms();
        films.forEach(film -> film.setGenres(new LinkedHashSet<>(genreStorage.getGenresForFilm(film.getId()))));
        films.forEach(film -> film.setDirectors(directorStorage.getDirectorsForFilm(film.getId())));
        return films;
    }

    public Film createFilm(Film newFilm) {
        filmValidation(newFilm);
        return filmStorage.createFilm(newFilm);
    }

    public List<Film> searchFilms(String query, String... by) {
        List<Film> films = filmStorage.searchFilms(query, by);
        films.forEach(film -> film.setGenres(new LinkedHashSet<>(genreStorage.getGenresForFilm(film.getId()))));
        films.forEach(film -> film.setDirectors(directorStorage.getDirectorsForFilm(film.getId())));
        return films;
    }

    public Film updateFilm(Film newFilm) {
        final Film film = filmStorage.findFilm(newFilm.getId());
        filmValidation(newFilm);
        mpaStorage.findMpa(newFilm.getMpa().getId());
        for (Genre genre : newFilm.getGenres()) {
            genreStorage.getGenre(genre.getId());
        }
        for (Director director : newFilm.getDirectors()) {
            directorStorage.getDirector(director.getId());
        }
        film.setName(newFilm.getName());
        film.setDescription(newFilm.getDescription());
        film.setReleaseDate(newFilm.getReleaseDate());
        film.setDuration(newFilm.getDuration());
        film.setMpa(newFilm.getMpa());
        film.setGenres(newFilm.getGenres());
        film.setDirectors(newFilm.getDirectors());
        System.out.println("В базу передается пустой жанр?                 " + film);
        filmStorage.updateFilm(film);
        return filmStorage.findFilm(newFilm.getId());
    }

    public void deleteFilm(long id) {
        filmStorage.findFilm(id);
        filmStorage.deleteFilm(id);
    }

    public Film setLikeToMovie(long filmId, long userId) {
        filmStorage.findFilm(filmId);
        userStorage.findUser(userId);
        likeStorage.setLikeToMovie(filmId, userId);

        Event event = new Event();
        event.setUserId(userId);
        event.setEventType("LIKE");
        event.setOperation("ADD");
        event.setEntityId(filmId);
        event.setTimestamp(Instant.now().toEpochMilli());

        eventService.addEvent(event);

        return filmStorage.findFilm(filmId);
    }

    public Film removeLikeFromMovie(long filmId, long userId) {
        filmStorage.findFilm(filmId);
        userStorage.findUser(userId);
        likeStorage.removeLikeFromMovie(filmId, userId);

        Event event = new Event();
        event.setUserId(userId);
        event.setEventType("LIKE");
        event.setOperation("REMOVE");
        event.setEntityId(filmId);
        event.setTimestamp(Instant.now().toEpochMilli());

        eventService.addEvent(event);

        return filmStorage.findFilm(filmId);
    }

    public List<Film> getPopularFilms(int count, Long genreId, Integer year) {
        List<Film> films = filmStorage.getPopularFilms(count, genreId, year);
        films.forEach(film -> film.setGenres(new LinkedHashSet<>(genreStorage.getGenresForFilm(film.getId()))));
        return films;
    }

    public List<Genre> getGenresForFilm(long filmId) {
        return genreStorage.getGenresForFilm(filmId);
    }

    public List<Director> getDirectorsForFilm(long filmId) {
        return directorStorage.getDirectorsForFilm(filmId);
    }

    public List<Film> getFilmByDirector(long directorId, SortType sortType) {
        return filmStorage.getFilmsByDirector(directorId, sortType)
                .stream()
                .peek(p -> p.setDirectors(directorStorage.getDirectorsForFilm(p.getId())))
                .toList();
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        List<Film> films = filmStorage.getCommonFilms(userId, friendId);
        films.forEach(film -> film.setGenres(new LinkedHashSet<>(genreStorage.getGenresForFilm(film.getId()))));
        return films;
    }

    public List<Film> getFilmRecommendations(long userId) {
        List<Film> favoriteMovies = filmStorage.getFavoriteMovies(userId);

        if (favoriteMovies.isEmpty())
            return new ArrayList<>();

        List<Film> filmIntersections = filmStorage
                .findFilmIntersections(userId, favoriteMovies.stream().map(Film::getId).collect(Collectors.toList()));

        if (filmIntersections.isEmpty())
            return new ArrayList<>();

        List<Film> recommendedFilms = new ArrayList<>();

        for (Film film : filmIntersections) {
            boolean shouldRecommended = favoriteMovies.stream().noneMatch(f -> f.getId().equals(film.getId()));

            if (shouldRecommended) {
                recommendedFilms.add(film);
            }
        }
        return recommendedFilms;
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
        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
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
        if (!mpaStorage.findAllMpas().contains(newFilm.getMpa())) {
            log.error("Пользователь попытался создать фильм c несуществующим рейтингом");
            throw new ValidationExceptions("Необходимо указать корректный рейтинг");
        }
        for (Genre genre : newFilm.getGenres()) {
            if (!genreStorage.getAllGenres().contains(genre)) {
                log.error("Пользователь попытался создать фильм с несуществующим жанром");
                throw new ValidationExceptions("Необходимо указать корректный жанр");
            }
        }
    }
}
