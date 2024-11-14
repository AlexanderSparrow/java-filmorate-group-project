package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.List;

@Component
@Repository
public class FriendshipDbRepository extends BaseRepository<User> implements FriendshipStorage {
    private static final String ADD_FRIEND = "INSERT INTO FRIENDSHIP (FRIENDSHIP_USER_ID, FRIENDSHIP_FRIEND_ID) " +
            "values (?, ?)";
    private static final String DELETE_FRIEND = "DELETE FROM FRIENDSHIP WHERE FRIENDSHIP_USER_ID = ? \n" +
            "AND FRIENDSHIP_FRIEND_ID = ?";
    private static final String FIND_COMMON_FRIENDS = "select * from USERS u, FRIENDSHIP f, FRIENDSHIP o where u.USER_ID = f.FRIENDSHIP_FRIEND_ID AND u.USER_ID = o.FRIENDSHIP_FRIEND_ID AND f.FRIENDSHIP_USER_ID = ? AND o.FRIENDSHIP_USER_ID = ?";
    private static final String FIND_FRIENDS = "SELECT * FROM USERS u, FRIENDSHIP f WHERE u.USER_ID = f.FRIENDSHIP_USER_ID AND f.FRIENDSHIP_USER_ID = ?;";

    public FriendshipDbRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public void addFriend(long id, long friendId) {
        insert(ADD_FRIEND, id, friendId);
    }

    @Override
    public void removeFriend(long id, long friendId) {
        delete(DELETE_FRIEND, id, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        return findMany(FIND_FRIENDS, id);
    }

    @Override
    public List<User> getCommonFriends(long id, long friendId) {
        return findMany(FIND_COMMON_FRIENDS, id, friendId);
    }
}
