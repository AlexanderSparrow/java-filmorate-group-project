package ru.yandex.practicum.filmorate.exception;

public class NotFoundExceptions extends RuntimeException {
    public NotFoundExceptions(String message) {
        super(message);
    }
}