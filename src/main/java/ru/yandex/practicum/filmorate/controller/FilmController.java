package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping
    public Set<Film> getAll() {
        log.info("Количество фильмов на данный момент: {}", films.size());
        return new HashSet<>(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        validateDescription(film.getDescription());
        validateReleaseDate(film.getReleaseDate());

        if (!films.containsValue(film)) {
            film.setId(id++);
            films.put(film.getId(), film);
            log.info("Добавлен новый фильм: {}", film.getName());
        }

        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Такого фильма не существует");
        }

        films.put(film.getId(), film);
        log.info("Фильм '{}' обновлен", film.getName());

        return film;
    }

    public void validateReleaseDate(LocalDate releaseDate) throws ValidationException {
        LocalDate validReleaseDate = LocalDate.of(1895, 12, 28);

        if (releaseDate.isBefore(validReleaseDate)) {
            throw new ValidationException("Дата релиза фильма не может быть раньше, чем 1895-12-28");
        }
    }

    public void validateDescription(String description) throws ValidationException {
        if (description != null && description.length() > 200) {
            throw new ValidationException("Максимальная длина описания не должна превышать 200 символов");
        }
    }
}
