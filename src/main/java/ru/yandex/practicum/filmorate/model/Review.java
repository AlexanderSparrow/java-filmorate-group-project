package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotNull;


@Data
@EqualsAndHashCode(of = { "reviewId" })
public class Review {
    private long reviewId;
    @NotBlank
    private String content;
    @NotBlank
    private Boolean isPositive;
    @NotNull(message = "Не указан пользователь")
    private Long userId;
    @NotNull(message = "Не указан фильм")
    private Long filmId;
    private int useful = 0;
}
