package ru.yandex.practicum.filmorate.mappers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserMapper implements RowMapper<User> {

    private final FriendshipStorage friendshipStorage;

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("USER_ID"));
        user.setName(rs.getString("USER_NAME"));
        user.setEmail(rs.getString("USER_EMAIL"));
        user.setLogin(rs.getString("USER_LOGIN"));
        user.setBirthday(rs.getDate("USER_BIRTHDAY").toLocalDate());
        user.setFriends(friendshipStorage.getFriends(user.getId()).stream()
                .map(Friendship::getFriendId).collect(Collectors.toSet()));
        return user;
    }
}
