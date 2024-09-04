package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@EqualsAndHashCode(of = { "name", "releaseDate" })
public class Film {
    private int id;
    @NotBlank(message = "Название не может быть пустым")
    private String name;
    private String description;
    private LocalDate releaseDate;
    @Positive(message = "Длительность фильма не может быть отрицательной")
    private Long duration;
}
