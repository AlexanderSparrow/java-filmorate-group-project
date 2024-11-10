package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public class MpaDbRepository extends BaseRepository<Mpa> implements MpaStorage {
    private static final String FIND_MPA_BY_ID = "SELECT * FROM mpa WHERE MPA_ID = ?";
    private static final String FIND_ALL_MPAS = "SELECT * FROM MPA ORDER BY MPA_ID";

    public MpaDbRepository(JdbcTemplate jdbc, RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Mpa findMpa(long id) {
        Optional<Mpa> mpa = findOne(FIND_MPA_BY_ID, id);
        if (mpa.isPresent()) {
            return mpa.get();
        } else {
            throw new NotFoundExceptions("MPA с ID " + id + " не найден");
        }
    }

    public List<Mpa> findAllMpas() {
        return findMany(FIND_ALL_MPAS);
    }
}
