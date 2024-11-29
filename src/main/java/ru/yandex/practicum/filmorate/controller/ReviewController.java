package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping(value = "/{id}")
    public Review getReview(@PathVariable long id) {
        log.info("Получен запрос на поиск ревью по id {}", id);
        return reviewService.getReview(id);
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(defaultValue = "0") Long filmId, @RequestParam(defaultValue = "10") int count) {
        log.info("Получен запрос на поиск ревью по фильму {}", filmId);
        return reviewService.getReviews(filmId, count);
    }

    @PostMapping
    public Review createReview(@Valid @RequestBody Review review) {
        log.info("Получен запрос на создание ревью");
        return reviewService.createReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("Получен запрос на обновление ревью с id {}", review.getReviewId());
        return reviewService.updateReview(review);
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public Review likeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Получен запрос на лайк ревью с id {} от пользователя {}.", id, userId);
        return reviewService.likeReview(id, userId);
    }

    @PutMapping(value = "/{id}/dislike/{userId}")
    public Review dislikeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Получен запрос на дизлайк ревью с id {} от пользователя {}.", id, userId);
        return reviewService.dislikeReview(id, userId);
    }

    @DeleteMapping(value = "/{id}")
    public void deleteReview(@PathVariable long id) {
        log.info("Получен запрос на удаление ревью с id {}.", id);
        reviewService.deleteReview(id);
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public Review deleteLikeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Получен запрос на удаление лайка ревью с id {} от пользователя {}.", id, userId);
        return reviewService.deleteLikeReview(id, userId);
    }

    @DeleteMapping(value = "/{id}/dislike/{userId}")
    public Review deleteDislikeReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Получен запрос на удаление дизлайка ревью с id {} от пользователя {}.", id, userId);
        return reviewService.deleteDislikeReview(id, userId);
    }
}
