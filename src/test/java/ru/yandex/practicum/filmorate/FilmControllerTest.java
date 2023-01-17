package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    public void shouldAddFilm() throws ValidationException {
        Film film = new Film();
        film.setName("film");
        film.setReleaseDate(LocalDate.of(2020, 12, 20));
        filmController.create(film);

        Set<Film> expectedResult = new HashSet<>();
        expectedResult.add(film);
        Set<Film> actualResult = filmController.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldThrowExceptionWhenReleaseDateIsInvalid() {
        Film film = new Film();
        film.setName("film");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.create(film));

        String expectedResult = "Дата релиза фильма не может быть раньше, чем 1895-12-28";
        String actualResult = exception.getMessage();

        assertEquals(expectedResult, actualResult);
    }
}
