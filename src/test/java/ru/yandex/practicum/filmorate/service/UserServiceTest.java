package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserServiceTest {

    private final UserService userService;

    private User user;
    private User user2;
    private User user3;
    private User firstUser;
    private User secondUser;
    private User thirdUser;

    @BeforeEach
    public void addContext() {
        user = new User();
        user.setName("firstUser");
        user.setLogin("Login1");
        user.setEmail("Email1@mail.ru");
        user.setBirthday(LocalDate.now());
        firstUser = userService.createUser(user);

        user2 = new User();
        user2.setName("secondUser");
        user2.setLogin("Login2");
        user2.setEmail("Email2@mail.ru");
        user2.setBirthday(LocalDate.now());
        secondUser = userService.createUser(user2);

        user3 = new User();
        user3.setName("thirdUser");
        user3.setLogin("Login3");
        user3.setEmail("Email3@mail.ru");
        user3.setBirthday(LocalDate.now());
        thirdUser = userService.createUser(user3);

    }

    @Test
    @DisplayName("Создание пользователя")
    void createUserTest() {
        assertThat(firstUser).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(firstUser).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(firstUser).hasFieldOrPropertyWithValue("login", user.getLogin());
        assertThat(firstUser).hasFieldOrPropertyWithValue("email", user.getEmail());
        assertThat(firstUser).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
    }

    @Test
    @DisplayName("Поиск пользователя")
    void findUserTest() {
        User userDb = userService.findUser(firstUser.getId());
        assertThat(userDb).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(userDb).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(userDb).hasFieldOrPropertyWithValue("login", user.getLogin());
        assertThat(userDb).hasFieldOrPropertyWithValue("email", user.getEmail());
        assertThat(userDb).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
    }

    @Test
    @DisplayName("Поиск всех пользователей")
    void findAllUsers() {
        List<User> users = userService.findAllUsers();
        assertEquals(users.size(), 3);
    }

    @Test
    @DisplayName("Обновление пользователя")
    void updateUser() {
        user.setName("Обновленное имя");
        firstUser = userService.updateUser(user);
        assertThat(firstUser).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(firstUser).hasFieldOrPropertyWithValue("name", user.getName());
        assertThat(firstUser).hasFieldOrPropertyWithValue("login", user.getLogin());
        assertThat(firstUser).hasFieldOrPropertyWithValue("email", user.getEmail());
        assertThat(firstUser).hasFieldOrPropertyWithValue("birthday", user.getBirthday());
    }

    @Test
    @DisplayName("Добавление друзей")
    void addFriend() {
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        List<User> friends = userService.getFriends(firstUser.getId());
        assertEquals(friends.size(), 2);
    }

    @Test
    @DisplayName("Удаление друзей")
    void removeFriend() {
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.removeFriend(firstUser.getId(), secondUser.getId());
        List<User> friends = userService.getFriends(firstUser.getId());
        assertEquals(friends.size(), 1);
    }

    @Test
    @DisplayName("Получение друзей")
    void getFriends() {
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        List<User> friends = userService.getFriends(firstUser.getId());
        assertEquals(friends.size(), 2);
    }

    @Test
    @DisplayName("Получение общих друзей")
    void getCommonFriends() {
        userService.addFriend(firstUser.getId(), secondUser.getId());
        userService.addFriend(firstUser.getId(), thirdUser.getId());
        userService.addFriend(secondUser.getId(), thirdUser.getId());
        List<User> commonFriends = userService.getCommonFriends(firstUser.getId(), secondUser.getId());
        assertEquals(commonFriends.size(), 1);
    }
}