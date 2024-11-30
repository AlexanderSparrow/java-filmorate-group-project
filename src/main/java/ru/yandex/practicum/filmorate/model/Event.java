package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(of = { "id" })
public class Event {
    private Long id;
    private Long userId;
    private String eventType;
    private String operation;
    private Long entityId;
    private Long timestamp;
}
