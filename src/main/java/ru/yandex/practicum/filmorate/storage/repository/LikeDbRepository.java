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
    private static final String CHECK_LIKE_OR_DISLIKE_TO_FILM_EXISTS = "SELECT COUNT(*) FROM USER_LIKES WHERE user_id = ? AND film_id = ?";

    public LikeDbRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void setLikeToMovie(long movieId, long userId) {
        insert(SET_LIKE_TO_MOVIE, movieId, userId);
    }

    @Override
    public void removeLikeFromMovie(long movieId, long userId) {
        delete(REMOVE_LIKE_FROM_MOVIE, movieId, userId);
    }

    @Override
    public boolean isLikeToFilmExists(long filmId, long userId) {
        Integer count = jdbc.queryForObject(CHECK_LIKE_OR_DISLIKE_TO_FILM_EXISTS, Integer.class, userId, filmId);
        return count != null && count > 0;
    }
}

