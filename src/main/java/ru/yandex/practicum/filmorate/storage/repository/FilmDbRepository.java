package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public class FilmDbRepository extends BaseRepository<Film> implements FilmStorage {

    private static final String FIND_FILM_BY_ID =
            "SELECT f.film_id AS ID, f.film_name AS NAME, f.film_description AS DESCRIPTION, " +
            "f.film_release_date AS RELEASE_DATE, f.film_duration AS DURATION, f.film_mpa AS MPA_ID, " +
            "MPA.NAME AS MPA_NAME " +
            "FROM films AS f " +
            "JOIN MPA ON f.FILM_MPA = MPA.ID " +
            "WHERE f.film_id = ?";
    private static final String FIND_ALL_FILMS =             "SELECT f.film_id AS ID, f.film_name AS NAME, f.film_description AS DESCRIPTION, " +
            "f.film_release_date AS RELEASE_DATE, f.film_duration AS DURATION, f.film_mpa AS MPA_ID, " +
            "MPA.NAME AS MPA_NAME " +
            "FROM films AS f " +
            "JOIN MPA ON f.FILM_MPA = MPA.ID ";
    private static final String CREATE_FILM = "INSERT INTO films " +
            "(film_name, film_description, film_release_date, film_duration, film_mpa) " +
            "VALUES(?, ?, ?, ?, ?)";
    private static final String INSERT_GENRES = "INSERT INTO FILM_GENRES (film_id, genre_id) VALUES(?, ?)";
    private static final String UPDATE_FILM = "UPDATE films SET " +
            "film_name = ?, film_description = ?, film_release_date = ?, film_duration = ?, film_mpa = ? \n" +
            "WHERE film_id = ?";
    private static final String DELETE_GENRES = "DELETE FROM FILM_GENRES WHERE film_id = ?";
    private static final String FIND_POPULAR_FILMS = "SELECT " +
            "f.film_id, film_name, film_description, film_release_date, film_duration, film_mpa AS MPA_ID, MPA.NAME AS MPA_NAME, COUNT(*)\n" +
            "FROM films AS f\n" +
            "JOIN MPA ON f.FILM_MPA = MPA.ID\n" +
            "JOIN USER_LIKES AS l ON f.film_id = l.film_id\n" +
            "GROUP BY f.film_id\n" +
            "ORDER BY COUNT(*) desc\n" +
            "LIMIT ?";

    public FilmDbRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Film findFilm(long id) {
        Optional<Film> film = findOne(FIND_FILM_BY_ID, id);
        if (film.isPresent()) {
            return film.get();
        } else {
            throw new NotFoundExceptions("Фильм с ID " + id + " не найден");
        }
    }

    @Override
    public List<Film> findAllFilms() {
        return findMany(FIND_ALL_FILMS);
    }

    @Override
    public Film createFilm(Film film) {
        long id = insert(CREATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            for (Genre genre : film.getGenres()) {
                insert(INSERT_GENRES, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        update(UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            delete(DELETE_GENRES, film.getId());
            for (Genre genre : film.getGenres()) {
                insert(INSERT_GENRES, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return findMany(FIND_POPULAR_FILMS, count);
    }
}
