package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmServiceTest {

    private final FilmService filmService;
    private final MpaService mpaService;
    private final GenreService genreService;
    private final  UserService userService;
    private Film film;
    private Film newFilm;
    private Film newFilm2;
    private User user;

    @BeforeEach
    public void addContext() {
        film = new Film();
        film.setName("Test");
        film.setDescription("Test");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100L);
        film.setMpa(mpaService.findMpa(1L));
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(genreService.getGenre(1L));
        film.setGenres(genres);

        newFilm = filmService.createFilm(film);

        user = new User();
        user.setName("Test");
        user.setEmail("test@test.ru");
        user.setBirthday(LocalDate.now());
        user.setLogin("Login");
        userService.createUser(user);
    }


    @Test
    @DisplayName(value = "Создание фильма")
    public void createFilmTest() {
        assertThat(newFilm).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(newFilm).hasFieldOrPropertyWithValue("name", film.getName());
        assertThat(newFilm).hasFieldOrPropertyWithValue("description", film.getDescription());
        assertThat(newFilm).hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
        assertThat(newFilm).hasFieldOrPropertyWithValue("duration", film.getDuration());
        assertEquals(newFilm.getGenres().size(), 1);
        assertEquals(new ArrayList<>(newFilm.getGenres()).getFirst().getName(), "Комедия");
        assertEquals(newFilm.getMpa().getId(), film.getMpa().getId());
        assertEquals(newFilm.getMpa().getName(), "G");
    }

    @Test
    @DisplayName(value = "Получение фильма по id")
    public void getFilmByIdTest() {
        Film filmDb = filmService.findFilm(film.getId());
        assertThat(filmDb).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(filmDb).hasFieldOrPropertyWithValue("name", film.getName());
        assertThat(filmDb).hasFieldOrPropertyWithValue("description", film.getDescription());
        assertThat(filmDb).hasFieldOrPropertyWithValue("releaseDate", film.getReleaseDate());
        assertThat(filmDb).hasFieldOrPropertyWithValue("duration", film.getDuration());
        assertEquals(filmDb.getGenres().size(), 1);
        assertEquals(new ArrayList<>(filmDb.getGenres()).getFirst().getName(), "Комедия");
        assertEquals(filmDb.getMpa().getId(), film.getMpa().getId());
        assertEquals(filmDb.getMpa().getName(), "G");
    }

    @Test
    @DisplayName(value = "Получение всех фильмов")
    public void getAllFilmsTest() {
        Collection<Film> films = filmService.findAllFilms();
        assertEquals(films.size(), 1);
    }

    @Test
    @DisplayName(value = "Обновление фильма")
    public void updateFilmTest() {
        newFilm.setName("Updated name");
        Film updatedFilm = filmService.updateFilm(newFilm);

        assertThat(updatedFilm).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("name", newFilm.getName());
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("description", newFilm.getDescription());
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("releaseDate", newFilm.getReleaseDate());
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("duration", newFilm.getDuration());
        assertEquals(updatedFilm.getGenres().size(), 1);
        assertEquals(new ArrayList<>(updatedFilm.getGenres()).getFirst().getName(), "Комедия");
        assertEquals(updatedFilm.getMpa().getId(), newFilm.getMpa().getId());
        assertEquals(updatedFilm.getMpa().getName(), "G");
    }

    @Test
    @DisplayName(value = "Получение лайка")
    public void getPopularFilmsTest() {
        filmService.setLikeToMovie(1, 1);
        List<Film> populrFilm  = filmService.getPopularFilms(1);
        assertEquals(populrFilm.size(), 1, "Получено не верное количество фильмов с лайками");
    }
}