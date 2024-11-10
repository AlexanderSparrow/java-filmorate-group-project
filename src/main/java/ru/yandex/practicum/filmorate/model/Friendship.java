package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Friendship {
    private long friendshipId;
    private long userid;
    private long friendId;
}
