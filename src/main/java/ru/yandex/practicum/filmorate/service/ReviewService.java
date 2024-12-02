package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewStorage reviewStorage;
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final EventService eventService;

    public Review getReview(long id) {
        return reviewStorage.getReview(id);
    }


    public List<Review> getReviews(Long filmId, int count) {
        if (filmId == null) {
            return reviewStorage.getReviews();
        } else {
            filmStorage.findFilm(filmId);
            return reviewStorage.getReviews(filmId, count);
        }
    }

    public Review createReview(Review review) {
        validateReview(review);
        if (!reviewStorage.isReviewExists(review.getUserId(), review.getFilmId())){
            Review result = reviewStorage.createReview(review);

            Event event = new Event();
            event.setUserId(result.getUserId());
            event.setEventType("REVIEW");
            event.setOperation("ADD");
            event.setEntityId(result.getReviewId());
            event.setTimestamp(Instant.now().toEpochMilli());

            eventService.addEvent(event);
            return result;
        } else {
            throw new ValidationExceptions("Отзыв уже опубликован");
        }
    }

    public Review updateReview(Review review) {
        validateReview(review);
        getReview(review.getReviewId());
       Review obj = getReview(review.getReviewId());

            Event event = new Event();
            event.setUserId(obj.getUserId());
            event.setEventType("REVIEW");
            event.setOperation("ADD");
            event.setEntityId(obj.getReviewId());
            event.setTimestamp(Instant.now().toEpochMilli());

            eventService.addEvent(event);
            return reviewStorage.updateReview(review);

    }

    public void deleteReview(long id) {
        Review review = getReview(id);
        if ((reviewStorage.isReviewExists(review.getFilmId(), review.getUserId()))) {

            Event event = new Event();
            event.setUserId(review.getUserId());
            event.setEventType("REVIEW");
            event.setOperation("REMOVE");
            event.setEntityId(review.getReviewId());
            event.setTimestamp(Instant.now().toEpochMilli());

            eventService.addEvent(event);

            reviewStorage.deleteReview(id);
        } else {
            throw new ValidationExceptions("Нельзя удалить несуществующее отзыв");
        }
    }

    public Review likeReview(long id, long userId) {
        getReview(id);
        if (reviewStorage.isLikeOrDislikeExists(id, userId, false)) {
            deleteDislikeReview(id, userId);
        }
        if (!reviewStorage.isLikeOrDislikeExists(id, userId, true)) {
            Event event = new Event();
            event.setUserId(userId);
            event.setEventType("LIKE");
            event.setOperation("ADD");
            event.setEntityId(id);
            event.setTimestamp(Instant.now().toEpochMilli());

            eventService.addEvent(event);
            return reviewStorage.likeReview(id, userId);
        } else {
            throw new ValidationExceptions("Вы уже поставили лайк");
        }
    }

    public Review dislikeReview(long id, long userId) {
        getReview(id);
        if (reviewStorage.isLikeOrDislikeExists(id, userId, true)) {
            deleteLikeReview(id, userId);
        }
        if (!reviewStorage.isLikeOrDislikeExists(id, userId, false)) {
            Event event = new Event();
            event.setUserId(userId);
            event.setEventType("LIKE");
            event.setOperation("REMOVE");
            event.setEntityId(id);
            event.setTimestamp(Instant.now().toEpochMilli());

            eventService.addEvent(event);
            return reviewStorage.dislikeReview(id, userId);
        } else {
            throw new ValidationExceptions("Вы уже поставили дизлайк этому ревью");
        }
    }

    public Review deleteDislikeReview(long id, long userId) {
        if (reviewStorage.isLikeOrDislikeExists(id, userId, false)) {
            return reviewStorage.deleteDislikeReview(id, userId);
        } else {
            throw new ValidationExceptions("Нельзя удалить несуществующий лайк");
        }
    }

    public Review deleteLikeReview(long id, long userId) {
        if (reviewStorage.isLikeOrDislikeExists(id, userId, true)) {
            return reviewStorage.deleteLikeReview(id, userId);
        } else {
            throw new ValidationExceptions("Нельзя удалить несуществующий лайк");
        }
    }

    public void validateReview(Review review) {
        try {
            userStorage.findUser(review.getUserId());
        } catch (NotFoundExceptions e) {
            throw new NotFoundExceptions("Указан не верный Пользователь");
        }
        try {
            filmStorage.findFilm(review.getFilmId());
        } catch (NotFoundExceptions e) {
            throw new NotFoundExceptions("Указан не верный Фильм");
        }

    }
}
