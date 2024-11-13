package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {

    void addFriend(long id, long friendId);

    void removeFriend(long id, long friendId);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long id, long friendId);
}
