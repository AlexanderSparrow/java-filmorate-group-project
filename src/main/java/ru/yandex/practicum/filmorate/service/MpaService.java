package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MpaService {

    private final MpaStorage mpaStorage;

    public Mpa findMpa(long id) {
        return mpaStorage.findMpa(id);
    }
    public List<Mpa> findAllMpas() {
        return mpaStorage.findAllMpas();
    }
}
