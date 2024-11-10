package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Component
public class LikeMapper implements RowMapper<Like>{

    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        Like like = new Like();
        like.setId(rs.getLong("id"));
        like.setUserId(rs.getLong("USER_ID"));
        like.setFilmId(rs.getLong("FILM_ID"));
        return like;
    }
}
