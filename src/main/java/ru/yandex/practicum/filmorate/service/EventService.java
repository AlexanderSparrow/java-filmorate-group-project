package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventStorage eventStorage;

    public void addEvent(Event event) {
        eventStorage.addEvent(event);
    }

    public List<Event> getUserEvents(Long userId) {
        return eventStorage.getEventsByUserId(userId);
    }
}

