package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {

    void addFriend(long id, long friendId);

    void removeFriend(long id, long friendId);

    List<Friendship> getFriends(long id);

    List<Friendship> getCommonFriends(long id, long friendId);
}
