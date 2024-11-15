package ru.yandex.practicum.filmorate.exception;

public class InternalServerExceptions extends RuntimeException {
    public InternalServerExceptions(String message) {
        super(message);
    }
}
