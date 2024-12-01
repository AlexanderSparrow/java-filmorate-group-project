package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review getReview(long id);

    List<Review> getReviews();

    List<Review> getReviews(Long filmId, int count);

    Review createReview(Review review);

    Review updateReview(Review review);

    Review likeReview(long id, long userId);

    Review dislikeReview(long id, long userId);

    void deleteReview(long id);

    Review deleteDislikeReview(long id, long userId);

    Review deleteLikeReview(long id, long userId);

    boolean isReviewExists(long userId, long filmId);

    boolean isLikeOrDislikeExists(long id, long userId, boolean isLike);
}
