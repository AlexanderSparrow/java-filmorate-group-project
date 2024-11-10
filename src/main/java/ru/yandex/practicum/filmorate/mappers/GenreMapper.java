package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Component
public class GenreMapper implements RowMapper<Genre>{

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getInt("GENRE_ID"));
        genre.setName(rs.getString("GENRE_NAME"));
        return genre;
    }
}
