package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/directors")
@RequiredArgsConstructor
public class DirectorController {

    private final DirectorService directorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Director create(@Valid @RequestBody Director director) {
        log.info("Получен запрос на добавление режиссера {}.", director.getName());
        return directorService.create(director);
    }

    @GetMapping("/{id}")
    public Director findById(@PathVariable long id) {
        log.info("Получен запрос на получение режиссера с id = {}.", id);
        return directorService.findById(id); // Здесь вызывается сервис
    }

    @GetMapping
    public List<Director> findAll() {
        log.info("Получен запрос на получение списка всех режиссеров");
        return directorService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long id) {
        log.info("Получен запрос на удаление режиссера с id = {}.", id);
        directorService.delete(id);
    }

    @PutMapping
    public Director update(@Valid @RequestBody Director director) {
        log.info("Получен запрос на обновление режиссера {}.", director.getName());
        return directorService.update(director);
    }
}
