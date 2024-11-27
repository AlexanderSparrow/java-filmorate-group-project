package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.LinkedHashSet;
import java.util.List;

@Service
public interface DirectorStorage {

    Director getDirector(long id);

    List<Director> getAllDirectors();

    boolean delete(long id);

    Director create(Director director);

    Director update(Director newDirector);

    List<Director> getDirectorsForFilm(long filmId);

    void addDirectorsToFilm(Long filmId, LinkedHashSet<Director> directors);

}
