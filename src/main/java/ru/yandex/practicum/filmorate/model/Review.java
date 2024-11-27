package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(of = { "reviewId" })
public class Review {
    long reviewId;
    String content;
    Boolean isPositive;
    @NotNull(message = "Не указан пользователь")
    Long userId;
    @NotNull(message = "Не указан фильм")
    Long filmId;
    int useful = 0;
}
