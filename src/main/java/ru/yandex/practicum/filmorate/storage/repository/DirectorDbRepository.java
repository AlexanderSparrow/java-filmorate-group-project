package ru.yandex.practicum.filmorate.storage.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.sql.PreparedStatement;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Slf4j
@Component
@Repository
public class DirectorDbRepository extends BaseRepository<Director> implements DirectorStorage {

    private static final String FIND_ALL_DIRECTORS = "SELECT id, name FROM DIRECTORS ORDER BY ID";
    private static final String FIND_DIRECTOR_BY_ID = "SELECT id, name FROM directors WHERE id = ?";

    private static final String DELETE_DIRECTOR_BY_ID = "DELETE FROM directors WHERE id = ?";
    private static final String INSERT_DIRECTOR = "INSERT INTO directors(name) VALUES(?)";
    private static final String UPDATE_DIRECTOR = "UPDATE directors SET name = ? WHERE id = ?";

    private static final String FIND_DIRECTORS_FOR_FILM = "SELECT d.id, d.name FROM FILM_DIRECTORS AS FD \n" +
                                                          "JOIN DIRECTORS AS d ON d.id = fd.DIRECTOR_id WHERE fd.film_id = ?";

    private static final String INSERT_DIRECTOR_FOR_FILM = "INSERT INTO FILM_DIRECTORS (FILM_ID, DIRECTOR_ID) VALUES (?, ?)";


    public DirectorDbRepository(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public Director getDirector(long id) {
        Optional<Director> director = findOne(FIND_DIRECTOR_BY_ID, id);
        if (director.isPresent()) {
            return director.get();
        } else {
            throw new NotFoundExceptions(String.format("Режиссер с ID %d не найден", id));
        }
    }

    public List<Director> getAllDirectors() {
        return findMany(FIND_ALL_DIRECTORS);
    }

    public Director create(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_DIRECTOR, new String[]{"id"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        director.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return director;
    }

    public Director update(Director director) {
        update(UPDATE_DIRECTOR, director.getName(), director.getId());
        return director;
    }

    public boolean delete(long id) {
        return delete(DELETE_DIRECTOR_BY_ID, id);
    }

    public List<Director> getDirectorsForFilm(long filmId) {
        return findMany(FIND_DIRECTORS_FOR_FILM, filmId);
    }

    public void addDirectorsToFilm(Long filmId, LinkedHashSet<Director> directors) {
        jdbc.batchUpdate(INSERT_DIRECTOR_FOR_FILM, directors, directors.size(), (ps, director) -> {
            ps.setLong(1, filmId);
            ps.setLong(2, director.getId());
        });
    }
}
