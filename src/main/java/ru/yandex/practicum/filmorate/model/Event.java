package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.yandex.practicum.filmorate.model.enums.EventTypes;
import ru.yandex.practicum.filmorate.model.enums.OperationTypes;


@Data
@EqualsAndHashCode(of = { "eventId" })
public class Event {
    private Long eventId;
    private Long userId;
    private Long entityId;
    private EventTypes eventType;
    private OperationTypes operation;
    private Long timestamp;
}
