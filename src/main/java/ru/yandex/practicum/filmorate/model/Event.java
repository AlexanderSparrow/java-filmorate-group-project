package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = { "id" })
public class Event {
    private Long id;            // Уникальный идентификатор события
    private Long userId;        // Идентификатор пользователя, связанного с событием
    private String eventType;   // Тип события (например, "LIKE", "REVIEW", "FRIEND")
    private String operation;   // Операция ("ADD", "REMOVE", "UPDATE")
    private Long entityId;      // Идентификатор сущности, связанной с событием (фильм, отзыв, друг)
    private Long timestamp; // Время события
}
