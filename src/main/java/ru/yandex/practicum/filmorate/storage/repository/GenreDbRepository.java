package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public class GenreDbRepository extends BaseRepository<Genre> implements GenreStorage {
    private static final String FIND_GENRE = "SELECT * FROM GENRES WHERE id = ?";
    private static final String FIND_ALL_GENRES = "SELECT * FROM GENRES ORDER BY ID";
    private static final String FIND_GENRES_FOR_FILM = "SELECT g.id, g.name FROM FILM_GENRES AS FG \n" +
            "JOIN GENRES AS g ON g.id = fg.GENRE_id WHERE fg.film_id = ?";

    public GenreDbRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Genre getGenre(long id) {
        Optional<Genre> genre = findOne(FIND_GENRE, id);
        if (genre.isPresent()) {
            return genre.get();
        } else {
            throw new NotFoundExceptions("Жанр с ID " + id + " не найден");
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        return findMany(FIND_ALL_GENRES);
    }

    @Override
    public List<Genre> getGenresForFilm(long filmId) {
        return findMany(FIND_GENRES_FOR_FILM, filmId);
    }
}
