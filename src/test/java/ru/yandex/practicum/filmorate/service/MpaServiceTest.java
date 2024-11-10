package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.FilmorateApplication;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = FilmorateApplication.class)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MpaServiceTest {
    private final MpaService mpaService;

    @Test
    @DisplayName("Поиск рейтиннга по id")
    void findMpa() {
        Mpa mpa = mpaService.findMpa(1);
        assertThat(mpa).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G");
    }

    @Test
    @DisplayName("Поиск всех рейтиннгов")
    void findAllMpas() {
        List <Mpa> mpas = mpaService.findAllMpas();
        assertThat(mpas.size()).isEqualTo(5);
    }
}