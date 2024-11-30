package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Component
public class ReviewMapper implements RowMapper<Review> {

    @Override
    public Review mapRow(ResultSet rs, int rowNum) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getLong("ID"));
        review.setContent(rs.getString("CONTENT"));
        review.setIsPositive(rs.getBoolean("IS_POSITIVE"));
        review.setUserId(rs.getLong("USER_ID"));
        review.setFilmId(rs.getLong("FILM_ID"));
        review.setUseful(rs.getInt("USEFUL"));
        return review;
    }
}
