package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public class ReviewDbRepository extends BaseRepository<Review> implements ReviewStorage {
     private static final String FIND_BY_ID = "SELECT r.*, " +
         "SUM(CASE WHEN rr.IS_LIKE = true THEN 1 WHEN rr.IS_LIKE = false THEN -1 END) AS useful " +
         "FROM " +
         "reviews r "+
         "LEFT JOIN REVIEWS_REACTIONS rr ON r.ID = rr.review_ID " +
         "WHERE r.id= ? " +
         "GROUP BY r.ID, r.content, r.is_Positive, r.user_id, r.film_id";
     private static final String FIND_BY_FILM_ID = "SELECT r.*, " +
         "SUM(CASE WHEN rr.IS_LIKE = true THEN 1 WHEN rr.IS_LIKE = false THEN -1 END) AS useful " +
         "FROM " +
         "reviews r " +
         "LEFT JOIN REVIEWS_REACTIONS rr ON r.ID = rr.review_ID " +
         "WHERE r.film_id = ? " +
         "GROUP BY r.ID, r.content, r.is_Positive, r.user_id, r.film_id " +
         "LIMIT ?";
    private static final String CREATE_REVIEW = "INSERT INTO REVIEWS\n" +
         "(CONTENT, IS_POSITIVE, USER_ID, FILM_ID)\n" +
         "VALUES(?, ?, ?, ?);";
    private static final String UPDATE_REVIEW = "UPDATE REVIEWS\n" +
         "SET CONTENT=?, IS_POSITIVE=?, USER_ID=?, FILM_ID=?\n" +
         "WHERE ID=?";
    private static final String DELETE_REVIEW = "DELETE FROM REVIEWS\n" +
         "WHERE ID=?";
    private static final String INSERT_REVIEW_REACTION_QUERY = "INSERT INTO REVIEWS_REACTIONS\n" +
         "(REVIEW_ID, USER_ID, IS_LIKE)\n" +
         "VALUES (?, ?, ?)";
    private static final String DELETE_REVIEW_REACTION_QUERY = "DELETE FROM REVIEWS_REACTIONS\n" +
         "WHERE REVIEW_ID=? AND  USER_ID=? AND IS_LIKE=?";
    private static final String CHECK_LIKE_OR_DISLIKE_EXISTS = "SELECT COUNT(*) FROM reviews_reactions " +
         "WHERE review_id=? and user_id=? and is_like = ?";
    private static final String CHECK_REVIEW_EXISTS = "SELECT COUNT(*) FROM reviews WHERE film_id =? and user_id=?";

    public ReviewDbRepository(JdbcTemplate jdbc, RowMapper<Review> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Review getReview(long id) {
        Optional<Review> review = findOne(FIND_BY_ID, id);
        if (review.isPresent()) {
            return review.get();
        } else {
            throw new NotFoundExceptions("Ревью с ID " + id + " не найден");
        }
    }

    @Override
    public List<Review> getReviews(long filmId, int count) {
        return findMany(FIND_BY_FILM_ID, filmId, count);
    }

    @Override
    public Review createReview(Review review) {
        long id = insert(CREATE_REVIEW,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId()
        );
        review.setReviewId(id);
        return getReview(id);
    }

    @Override
    public void deleteReview(long id) {
        delete(DELETE_REVIEW, id);
    }

    @Override
    public Review updateReview(Review review) {
        update(UPDATE_REVIEW,
                review.getContent(),
                review.getIsPositive(),
                review.getUserId(),
                review.getFilmId(),
                review.getReviewId()
        );
        return getReview(review.getReviewId());
    }

    @Override
    public Review likeReview(long id, long userId) {
        update(INSERT_REVIEW_REACTION_QUERY,  id, userId, true);
        return getReview(id);
    }

    @Override
    public Review dislikeReview(long id, long userId) {
        update(INSERT_REVIEW_REACTION_QUERY, id, userId, false);
        return getReview(id);
    }

    @Override
    public Review deleteDislikeReview(long id, long userId) {
        delete(DELETE_REVIEW_REACTION_QUERY,  id, userId, false);
        return getReview(id);
    }

    @Override
    public Review deleteLikeReview(long id, long userId) {
        delete(DELETE_REVIEW_REACTION_QUERY, id, userId, true);
        return getReview(id);
    }

    @Override
    public boolean isReviewExists(long filmId, long userId) {
        Integer count = jdbc.queryForObject(CHECK_REVIEW_EXISTS, Integer.class, filmId, userId);
        return count != null && count > 0;
    }

    @Override
    public boolean isLikeOrDislikeExists(long reviewId, long userId, boolean isLike) {
        Integer count = jdbc.queryForObject(CHECK_LIKE_OR_DISLIKE_EXISTS, Integer.class, reviewId, userId, isLike);
        return count != null && count > 0;
    }

}
