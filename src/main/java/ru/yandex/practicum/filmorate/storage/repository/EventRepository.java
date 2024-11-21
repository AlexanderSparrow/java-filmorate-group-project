package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EventRepository implements EventStorage {

    private final List<Event> events = new ArrayList<>();

    @Override
    public void addEvent(Event event) {
        event.setId((long) (events.size() + 1));
        events.add(event);
    }

    @Override
    public List<Event> getEventsByUserId(Long userId) {
        return events.stream()
                .filter(event -> event.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}

