package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Set;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public Set<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Long id) {
        return filmService.getById(id);
    }

    @GetMapping("/popular")
    public Set<Film> getPopular(@RequestParam(name = "count", defaultValue = "10", required = false) Long count) {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        validate(film);
        return filmService.add(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        validate(film);
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film likeFilm(@PathVariable("id") Long filmId,
                         @PathVariable("userId") Long userId) {
        return filmService.addLikeToFilm(filmId, userId);
    }


    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") Long filmId,
                           @PathVariable("userId") Long userId) {
        return filmService.removeLike(filmId, userId);
    }

    @DeleteMapping
    public Film delete(Film film) {
        return filmService.delete(film);
    }

    private void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше, чем 1895-12-28");
        }
    }
}
