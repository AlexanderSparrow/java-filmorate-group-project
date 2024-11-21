package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationExceptions;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class DirectorService {

    private final DirectorStorage directorStorage;

    public Director findById(long id) {
        return directorStorage.findById(id);
    }

    public List<Director> findAll() {
        return directorStorage.findAll();
    }

    public Director create(Director director) {
        directorValidation(director);
        return directorStorage.create(director);
    }

    public Director update(Director director) {
        directorValidation(director);
        directorStorage.findById(director.getId());
        return directorStorage.update(director);
    }

    public boolean delete(long id) {
        return directorStorage.delete(id);
    }

    private void directorValidation(Director newDirector) {
        if (newDirector == null) {
            log.error("Ошибка при добавлении режиссера: ");
            throw new ValidationExceptions("Режиссер не указан.");
        }
        if (newDirector.getName().isEmpty()) {
            log.error("Ошибка при добавлении режиссера: ");
            throw new ValidationExceptions("Имя режиссера не указано.");
        }
    }
}
