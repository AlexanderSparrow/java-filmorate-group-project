package ru.yandex.practicum.filmorate.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

@RequiredArgsConstructor
@Slf4j
@Component
public class FilmMapper implements RowMapper<Film> {

    private final DirectorStorage directorStorage;
    private final GenreStorage genreStorage;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILM_ID"));
        film.setName(rs.getString("FILM_NAME"));
        film.setDescription(rs.getString("FILM_DESCRIPTION"));
        film.setReleaseDate(rs.getDate("FILM_RELEASE_DATE").toLocalDate());
        film.setDuration(rs.getLong("FILM_DURATION"));
        Mpa mpa = new Mpa();
        mpa.setId(rs.getLong("MPA_ID"));
        mpa.setName(rs.getString("MPA_NAME"));
        film.setMpa(mpa);
        film.setGenres(new LinkedHashSet<>(genreStorage.getGenresForFilm(film.getId())));
        film.setDirectors(directorStorage.getDirectorsForFilm(film.getId()));
        return film;
    }
}
