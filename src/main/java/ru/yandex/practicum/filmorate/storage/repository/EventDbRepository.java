package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.EventStorage;

import java.util.List;

@Component
@Repository
public class EventDbRepository extends BaseRepository<Event> implements EventStorage {

    private static final String CREATE_EVENT = "INSERT INTO events " +
            "(USER_ID, ENTITY_ID, EVENT_TYPE, OPERATION, TIME_CREATE) " +
            "VALUES(?, ?, ?, ?, ?)";

    private static final String FIND_ALL_EVENTS = "SELECT * FROM EVENTS WHERE USER_ID = ?";

    private static final String DELETE_EVENTS_BY_USER = "DELETE FROM EVENTS WHERE USER_ID = ?";

    public EventDbRepository(JdbcTemplate jdbc, RowMapper<Event> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Event addEvent(Event event) {
        Long id = insert(CREATE_EVENT,
                event.getUserId(),
                event.getEntityId(),
                event.getEventType(),
                event.getOperation(),
                event.getTimestamp()
        );
        event.setEventId(id);
        return event;
    }

    @Override
    public List<Event> getEventsByUserId(Long userId) {
        return findMany(FIND_ALL_EVENTS, userId);
    }

    @Override
    public void deleteEventsByUserId(Long userId) {
        jdbc.update(DELETE_EVENTS_BY_USER, userId);
    }
}

