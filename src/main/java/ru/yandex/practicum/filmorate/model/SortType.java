package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SortType {
    year("film_release_date"), likes("count(ul.id) desc");

    private final String dbFieldName;
}
