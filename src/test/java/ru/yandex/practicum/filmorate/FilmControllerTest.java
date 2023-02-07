package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FilmControllerTest {
    @Autowired
    private final FilmController filmController ;
    @Autowired
    private final Validator validator;

    @Test
    public void shouldAddFilm() {
        Film film = Film.builder()
                .name("film")
                .releaseDate(LocalDate.of(2020, 12, 20))
                .description("film description")
                .duration(180)
                .build();
        filmController.create(film);

        Set<Film> expectedResult = new HashSet<>();
        expectedResult.add(film);
        Set<Film> actualResult = filmController.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldThrowExceptionWhenReleaseDateIsInvalid() {
        Film film = Film.builder()
                .name("film")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .description("film description")
                .duration(180)
                .build();
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.create(film));

        String expectedResult = "Дата релиза фильма не может быть раньше, чем 1895-12-28";
        String actualResult = exception.getMessage();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldNotAddFilmWhenNameIsNull() {
        Film film = Film.builder()
                .releaseDate(LocalDate.of(1994, 3, 30))
                .description("film description")
                .duration(180)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        int expectedResult = 1;
        int actualResult = violations.size();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldNotAddFilmWhenDescriptionIsMoreThan200Characters() {
        Film film = Film.builder()
                .name("film")
                .releaseDate(LocalDate.of(1994, 3, 30))
                .description("description".repeat(201))
                .duration(180)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        int expectedResult = 1;
        int actualResult = violations.size();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldNotAddFilmWhenDurationIsNegative() {
        Film film = Film.builder()
                .name("film")
                .releaseDate(LocalDate.of(1994, 3, 30))
                .description("film description")
                .duration(-180)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        int expectedResult = 1;
        int actualResult = violations.size();

        assertEquals(expectedResult, actualResult);
    }
}
