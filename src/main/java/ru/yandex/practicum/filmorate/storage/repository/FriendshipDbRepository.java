package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.List;

@Component
@Repository
public class FriendshipDbRepository extends BaseRepository<Friendship> implements FriendshipStorage {
    private static final String ADD_FRIEND = "insert into friendship (FRIENDSHIP_USER_ID, FRIENDSHIP_FRIEND_ID) " +
            "values (?, ?);";
    private static final String DELETE_FRIEND = "DELETE FROM friendship WHERE FRIENDSHIP_USER_ID = ? \n" +
            "AND FRIENDSHIP_FRIEND_ID = ?";
    private static final String FIND_COMMON_FRIENDS = "SELECT F1.friendship_id, F1.friendship_user_id, F1.friendship_friend_id \n" +
            "FROM (\n" +
            "    SELECT * \n" +
            "    FROM friendship \n" +
            "    WHERE friendship_user_id = ? \n" +
            ") AS F1\n" +
            "INNER JOIN (\n" +
            "    SELECT * \n" +
            "    FROM friendship \n" +
            "    WHERE friendship_user_id = ? \n" +
            ") AS F2 \n" +
            "ON F1.friendship_friend_id = F2.friendship_friend_id;";
    private static final String FIND_FRIENDS = "SELECT * FROM friendship WHERE FRIENDSHIP_USER_ID = ?";

    public FriendshipDbRepository(JdbcTemplate jdbc, RowMapper<Friendship> mapper) {
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
    public List<Friendship> getFriends(long id) {
        return  findMany(FIND_FRIENDS, id);
    }

    @Override
    public List<Friendship> getCommonFriends(long id, long friendId) {
        return findMany(FIND_COMMON_FRIENDS, id, friendId);
    }
}
