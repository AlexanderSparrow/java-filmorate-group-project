package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(of = { "reviewId" })
public class Review {
    private Long reviewId;
    @NotNull(message = "Не указан текст отзыва")
    private String content;
    @NotNull(message = "Не указан характер отзыва")
    private Boolean isPositive;
    @NotNull(message = "Не указан пользователь")
    private Long userId;
    @NotNull(message = "Не указан фильм")
    private Long filmId;
    private int useful = 0;
}
