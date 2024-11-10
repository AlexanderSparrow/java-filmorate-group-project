package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;

import java.util.List;

@Component
@Repository
public class LikeDbRepository extends BaseRepository<Like> implements LikeStorage {
    private static final String FIND_LIKES_BY_FILM_ID = "SELECT * FROM USER_LIKES WHERE film_id = ?";
    private static final String SET_LIKE_TO_MOVIE = "INSERT INTO USER_LIKES (film_id, user_id) VALUES (?, ?)";
    private static final String REMOVE_LIKE_FROM_MOVIE = "DELETE FROM USER_LIKES WHERE film_id = ? AND user_id = ?";

    public LikeDbRepository(JdbcTemplate jdbc, RowMapper<Like> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Like> getFilmLikes(long filmId) {
        return findMany(FIND_LIKES_BY_FILM_ID, filmId);
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
