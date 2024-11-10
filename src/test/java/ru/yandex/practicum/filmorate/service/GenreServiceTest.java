package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GenreServiceTest {

    private final GenreService genreService;
    private final FilmService filmService;
    private final MpaService mpaService;

    @BeforeEach
    public void createContext() {
        Film film = new Film();
        film.setName("Криминальное чтиво");
        film.setDescription("Криминальное чтиво");
        film.setDuration(100L);
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setMpa(mpaService.findMpa(1L));
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();
        genres.add(genreService.getGenre(2));
        genres.add(genreService.getGenre(3));
        film.setGenres(genres);
        filmService.createFilm(film);
    }

    @Test
    void getGenre() {
        Genre genre = genreService.getGenre(1);
        assertThat(genre).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия");
    }

    @Test
    void getAllGenres() {
        List<Genre> genres = genreService.getAllGenres();
        assertThat(genres.getFirst().getClass().equals(Genre.class));
    }

    @Test
    void getGenresForFilm() {
        List<Genre> genres = genreService.getGenresForFilm(1);
        assertThat(genres.size()).isEqualTo(2);
    }
}