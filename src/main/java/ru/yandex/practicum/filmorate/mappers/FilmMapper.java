package ru.yandex.practicum.filmorate.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class FilmMapper implements RowMapper<Film> {

    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("FILM_NAME"));
        film.setDescription(rs.getString("FILM_DESCRIPTION"));
        film.setReleaseDate(rs.getDate("FILM_RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getLong("FILM_DURATION"));
        Mpa Mpa = mpaStorage.findMpa(rs.getLong("FILM_MPA"));
        film.setMpa(Mpa);
        LinkedHashSet<Genre> genres = new LinkedHashSet<>(genreStorage.getGenresForFilm(film.getId()));
        Set<Long> likes = likeStorage.getFilmLikes(film.getId()).stream().map(Like::getUserId).collect(Collectors.toSet());
        film.setGenres(genres);
        film.setLikes(likes);
        return film;
    }
}
