package ru.yandex.practicum.filmorate.mappers;

import lombok.AccessLevel;
import org.springframework.jdbc.core.RowMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;

import java.sql.ResultSet;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
@Component
public class FriendshipMapper implements RowMapper<Friendship> {

    @Override
    public Friendship mapRow(ResultSet rs, int rowNum) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setFriendshipId(rs.getLong("FRIENDSHIP_ID"));
        friendship.setUserid(rs.getLong("FRIENDSHIP_USER_ID"));
        friendship.setFriendId(rs.getLong("FRIENDSHIP_FRIEND_ID"));
        return friendship;
    }
}
