package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {

    Event addEvent(Event event); // Добавить новое событие

    List<Event> getEventsByUserId(Long userId); // Получить события по ID пользователя
}
