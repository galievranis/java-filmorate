package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor
public class FilmControllerTest {
    private final InMemoryFilmStorage inMemoryFilmStorage = new InMemoryFilmStorage();
    private final FilmService filmService = new FilmService();
    private final FilmController filmController = new FilmController(filmService);

    @Test
    public void shouldAddFilm() throws ValidationException {
        Film film = Film.builder()
                .releaseDate(LocalDate.of(2020, 12, 20))
                .name("film")
                .build();
        inMemoryFilmStorage.add(film);

        Set<Film> expectedResult = new HashSet<>();
        expectedResult.add(film);
        Set<Film> actualResult = inMemoryFilmStorage.getAll();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void shouldThrowExceptionWhenReleaseDateIsInvalid() {
        Film film = Film.builder()
                .releaseDate(LocalDate.of(1895, 12, 27))
                .name("film")
                .build();
        Throwable exception = assertThrows(ValidationException.class, () -> filmController.create(film));

        String expectedResult = "Дата релиза фильма не может быть раньше, чем 1895-12-28";
        String actualResult = exception.getMessage();

        assertEquals(expectedResult, actualResult);
    }
}
