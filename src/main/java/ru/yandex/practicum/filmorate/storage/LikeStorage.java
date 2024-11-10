package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeStorage {

    List<Like> getFilmLikes(long filmId);

    void setLikeToMovie(long movieId, long userId);

    void removeLikeFromMovie(long movieId, long userId);
}
