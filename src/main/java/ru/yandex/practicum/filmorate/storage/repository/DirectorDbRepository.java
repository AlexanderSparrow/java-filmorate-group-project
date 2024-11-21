package ru.yandex.practicum.filmorate.storage.repository;

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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Repository
public class DirectorDbRepository extends BaseRepository<Director> implements DirectorStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM directors";
    private static final String FIND_DIRECTOR_BY_ID = "SELECT id, name FROM directors WHERE id = ?";
    private static final String DELETE_DIRECTOR_BY_ID = "DELETE FROM directors WHERE id = ?";
    private static final String INSERT_DIRECTOR = "INSERT INTO directors(name) VALUES(?)";
    private static final String UPDATE_DIRECTOR = "UPDATE directors SET name = ? WHERE id = ?";

    public DirectorDbRepository(JdbcTemplate jdbc, RowMapper<Director> mapper) {
        super(jdbc, mapper);
    }

    public Director findById(long id) {
        Optional<Director> director = findOne(FIND_DIRECTOR_BY_ID, id);
        if (director.isPresent()) {
            return director.get();
        } else {
            throw new NotFoundExceptions(String.format("Режиссер с ID %d не найден", id));
        }
    }

    public List<Director> findAll() {
        return findMany(FIND_ALL_QUERY);
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
}