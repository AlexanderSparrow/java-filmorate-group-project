package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {

    void setLikeToMovie(long movieId, long userId);

    void removeLikeFromMovie(long movieId, long userId);

    boolean isLikeToFilmExists(long filmId, long userId);
}
