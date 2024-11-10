package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Friendship {
    private long friendshipId;
    private long userid;
    private long friendId;
}
