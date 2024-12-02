package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    public void addEvent(Event event) {
        log.info("Получен запрос на добавление события {}", event);
        eventStorage.addEvent(event);
    }

    public List<Event> getUserEvents(Long userId) {
        userStorage.findUser(userId);
        log.info("Получен запрос на получение списка всех событий пользователя с {} ", userId);

        return eventStorage.getEventsByUserId(userId);
    }
}

