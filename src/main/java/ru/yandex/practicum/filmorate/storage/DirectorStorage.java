package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

@Service
public interface DirectorStorage {

    Director findById(long id);

    List<Director> findAll();

    boolean delete (long id);

    Director create (Director director);

    Director update (Director newDirector);
}
