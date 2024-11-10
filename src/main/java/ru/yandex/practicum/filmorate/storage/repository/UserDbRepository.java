package ru.yandex.practicum.filmorate.storage.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundExceptions;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Component
@Repository
public class UserDbRepository extends BaseRepository<User> implements UserStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    private static final String INSERT_USER = "INSERT INTO users(user_email, user_login, user_name, user_birthday) VALUES(?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET user_email = ?, user_login = ?, user_name = ?, user_birthday = ? WHERE user_id = ?";

    public UserDbRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public User findUser(long userId) {
        Optional<User> user = findOne(FIND_USER_BY_ID, userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundExceptions("Пользователь с ID " + userId + " не найден");
        }
    }

    public List<User> findAllUsers() {
        return findMany(FIND_ALL_QUERY);
    }

    public User createUser(User user) {
        long id = insert(INSERT_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        user.setId(id);
        return user;
    }

    public User updateUser(User user) {
        update(UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user;
    }
}