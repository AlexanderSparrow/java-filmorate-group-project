package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = { "id" })
public class Like {
    private long id;
    private long userId;
    private long filmId;
}
