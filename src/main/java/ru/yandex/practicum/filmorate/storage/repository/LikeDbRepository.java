package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

@Component
@Repository
public class LikeDbRepository extends BaseRepository<Film> implements LikeStorage {
    private static final String SET_LIKE_TO_MOVIE = "INSERT INTO USER_LIKES (film_id, user_id) VALUES (?, ?)";
    private static final String REMOVE_LIKE_FROM_MOVIE = "DELETE FROM USER_LIKES WHERE film_id = ? AND user_id = ?";

    public LikeDbRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void setLikeToMovie(long movieId, long userId) {
        insert(SET_LIKE_TO_MOVIE, movieId, userId);
    }

    @Override
    public void removeLikeFromMovie(long userId, long movieId) {
        delete(REMOVE_LIKE_FROM_MOVIE, movieId, userId);
    }
}
