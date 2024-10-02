package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    public Film findFilm(Long id) {
        return filmStorage.findFilm(id);
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film newFilm) {
        filmValidation(newFilm);
        return filmStorage.createFilm(newFilm);
    }

    public Film updateFilm(Film newFilm) {
        filmValidation(newFilm);
        return filmStorage.updateFilm(newFilm);
    }

    public Film setLikeToMovie(Long id, Long userId) {
        if (userService.findUser(userId) != null) {
            if (findFilm(id) != null) {
                Film film = findFilm(id);
                if (film.getLikes() != null) {
                    if (!film.getLikes().contains(userId)) {
                        film.getLikes().add(userId);
                    }
                } else {
                    Set<Long> likeList = new HashSet<>();
                    likeList.add(userId);
                    film.setLikes(likeList);
                }
                return film;
            } else {
                log.error("Пользователь попытался добавить лайк к фильму с несуществующим id");
                throw new NotFoundExceptions("Необходимо указать корректный id фильма");
            }
        } else {
            log.error("Пользователь попытался добавить лайк к фильму, но не авторизован");
            throw new NotFoundExceptions("Необходимо авторизоваться");
        }
    }

    public Film removeLikeFromMovie(Long id, Long userId) {
        if (userService.findUser(userId) != null) {
            if (findFilm(id) != null) {
                Film film = findFilm(id);
                if (film.getLikes().contains(userId)) {
                    film.getLikes().remove(userId);
                }
                return film;
            } else {
                log.error("Пользователь попытался удалить лайк с фильма с несуществующим id");
                throw new NotFoundExceptions("Необходимо указать корректный id фильма");
            }
        } else {
            log.error("Пользователь попытался удалить лайк с фильма, но не авторизован");
            throw new NotFoundExceptions("Необходимо авторизоваться");
        }
    }

    public List<Film> getPopularFilms(int count) {
        return findAllFilms().stream()
                .filter(movie -> movie.getLikes() != null)
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
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
    }
}
