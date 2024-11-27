package ru.yandex.practicum.filmorate.storage.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.SortType;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
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
    private static final String FIND_ALL_FILMS = "SELECT f.film_id AS ID, f.film_name AS NAME, f.film_description AS DESCRIPTION, " +
            "f.film_release_date AS RELEASE_DATE, f.film_duration AS DURATION, f.film_mpa AS MPA_ID, " +
            "MPA.NAME AS MPA_NAME " +
            "FROM films AS f " +
            "JOIN MPA ON f.FILM_MPA = MPA.ID ";
    private static final String CREATE_FILM = "INSERT INTO films " +
            "(film_name, film_description, film_release_date, film_duration, film_mpa) " +
            "VALUES(?, ?, ?, ?, ?)";
    private static final String INSERT_GENRES = "INSERT INTO FILM_GENRES (film_id, genre_id) VALUES(?, ?)";
    private static final String INSERT_DIRECTOR = "INSERT INTO FILM_DIRECTORS (film_id, director_id) VALUES(?, ?)";
    private static final String UPDATE_FILM = "UPDATE films SET " +
            "film_name = ?, film_description = ?, film_release_date = ?, film_duration = ?, film_mpa = ? \n" +
            "WHERE film_id = ?";
    private static final String DELETE_GENRES = "DELETE FROM FILM_GENRES WHERE film_id = ?";
    private static final String DELETE_DIRECTOR = "DELETE FROM FILM_DIRECTORS WHERE film_id = ?";
    private static final String FIND_FILMS_FOR_DIRECTOR = "SELECT f.*, f.film_mpa AS MPA_ID, " +
            "MPA.NAME AS MPA_NAME, count(ul.id) as likes " +
            "FROM FILM_DIRECTORS AS FD " +
            "JOIN FILMS AS f ON f.film_id = fd.film_id JOIN MPA ON f.FILM_MPA = MPA.ID " +
            "LEFT JOIN USER_LIKES ul on f.film_id=ul.film_id " +
            "WHERE fd.director_id = ? " +
            "group by f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.FILM_RELEASE_DATE, f.FILM_DURATION, " +
            "f.FILM_MPA, f.film_mpa, MPA.NAME";
    private static final String FIND_POPULAR_FILMS = "SELECT " +
            "f.film_id, film_name, film_description, film_release_date, film_duration, film_mpa AS MPA_ID, MPA.NAME AS MPA_NAME, COUNT(*)\n" +
            "FROM films AS f\n" +
            "JOIN MPA ON f.FILM_MPA = MPA.ID\n" +
            "JOIN USER_LIKES AS l ON f.film_id = l.film_id\n" +
            "GROUP BY f.film_id\n" +
            "ORDER BY COUNT(*) desc\n" +
            "LIMIT ?";
    private static final String FIND_FAVORITE_MOVIES =
            "SELECT " +
                "f.film_id AS id, f.film_name AS name, " +
                "f.film_description AS description, " +
                "f.film_release_date AS release_date, " +
                "f.film_duration AS duration, " +
                "f.film_mpa AS mpa_id, mpa.name AS mpa_name " +
             "FROM USER_LIKES ul " +
             "INNER JOIN FILMS f ON ul.film_id = f.film_id " +
             "INNER JOIN MPA mpa ON mpa.id = f.film_mpa " +
             "WHERE ul.user_id = ?";
    private static final String GET_FILM_RECOMMENDATIONS =
            "SELECT " +
                "f.film_id AS id, f.film_name AS name, " +
                "f.film_description AS description, " +
                "f.film_release_date AS release_date, " +
                "f.film_duration AS duration, " +
                "f.film_mpa AS mpa_id, mpa.name AS mpa_name " +
             "FROM USER_LIKES ul " +
             "INNER JOIN FILMS f ON ul.film_id = f.film_id " +
             "INNER JOIN MPA mpa ON mpa.id = f.film_mpa " +
             "WHERE ul.user_id IN " +
                "(SELECT ul2.user_id FROM USER_LIKES ul2 " +
                "WHERE ul2.user_id <> ? AND ul2.film_id = ANY(?) " +
                "GROUP BY ul2.user_id ORDER BY COUNT(*) DESC LIMIT 1)";
    private static final String FIND_COMMON_FILM =
            "SELECT f.film_id AS id, f.film_name AS name, f.film_description AS description, " +
                    "f.film_release_date AS release_date, f.film_duration AS duration, f.film_mpa AS mpa_id, " +
                    "mpa.name AS mpa_name " +
            "FROM USER_LIKES ul " +
            "INNER JOIN USER_LIKES fl ON fl.film_id = ul.film_id " +
            "INNER JOIN FILMS f ON ul.film_id = f.film_id " +
            "INNER JOIN MPA mpa ON mpa.id = f.film_mpa " +
            "WHERE ul.user_id = ? AND fl.user_id = ?";

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

        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            for (Director director : film.getDirectors()) {
                insert(INSERT_DIRECTOR, film.getId(), director.getId());
            }
        }

        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            log.debug("Deleting old directors for film {}", film.getId());
            delete(DELETE_DIRECTOR, film.getId());
            for (Director director : film.getDirectors()) {
                log.debug("Inserting director {} for film {}", director.getId(), film.getId());
                insert(INSERT_DIRECTOR, film.getId(), director.getId());
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

        if (film.getDirectors() != null && !film.getDirectors().isEmpty()) {
            delete(DELETE_DIRECTOR, film.getId());
            for (Director director : film.getDirectors()) {
                insert(INSERT_DIRECTOR, film.getId(), director.getId());
            }
        }

        return film;
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return findMany(FIND_POPULAR_FILMS, count);
    }

    @Override
    public List<Film> getFavoriteMovies(long userId) {
        return findMany(FIND_FAVORITE_MOVIES, userId);
    }

    @Override
    public List<Film> findFilmIntersections(long userId, Collection<Long> filmsId) {
        return findMany(GET_FILM_RECOMMENDATIONS, userId, filmsId.toArray());

    @Override  
    public List<Film> getFilmsByDirector(long directorId, SortType sortType) {
        return findMany(FIND_FILMS_FOR_DIRECTOR + " ORDER BY " + sortType.getDbFieldName(), directorId);
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return findMany(FIND_COMMON_FILM, userId, friendId);
    }
}
