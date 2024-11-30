package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventStorage eventStorage;
    private final UserStorage userStorage;

    public void addEvent(Event event) {
        eventStorage.addEvent(event);
    }

    public List<Event> getUserEvents(Long userId) {
        userStorage.findUser(userId);
        return eventStorage.getEventsByUserId(userId);
    }
}

