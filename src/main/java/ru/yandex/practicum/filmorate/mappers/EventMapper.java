package ru.yandex.practicum.filmorate.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.enums.EventTypes;
import ru.yandex.practicum.filmorate.model.enums.OperationTypes;

import java.sql.ResultSet;
import java.sql.SQLException;

@RequiredArgsConstructor
@Slf4j
@Component
public class EventMapper implements RowMapper<Event> {

    @Override
    public Event mapRow(ResultSet rs, int rowNum) throws SQLException {
        Event event = new Event();
        event.setEventId(rs.getLong("ID"));
        event.setUserId(rs.getLong("USER_ID"));
        event.setEntityId(rs.getLong("ENTITY_ID"));
        event.setEventType(EventTypes.valueOf(rs.getString("EVENT_TYPE")));
        event.setOperation(OperationTypes.valueOf(rs.getString("OPERATION")));
        event.setTimestamp(rs.getLong("TIME_CREATE"));
        return event;
    }
}