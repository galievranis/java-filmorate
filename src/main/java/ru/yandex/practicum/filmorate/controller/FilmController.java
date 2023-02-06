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

@Slf4j
@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {
    @Autowired
    private final FilmService filmService;

    @GetMapping
    public Set<Film> getAll() {
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable String id) {
        return filmService.getById(Long.parseLong(id));
    }

    @GetMapping("/popular")
    public Set<Film> getPopular(@RequestParam(name = "count", defaultValue = "10", required = false) String count) {
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
    public Film likeFilm(@PathVariable("id") String filmId,
                         @PathVariable("userId") String userId) {
        return filmService.addLikeToFilm(Long.parseLong(filmId), Long.parseLong(userId));
    }


    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable("id") String filmId,
                           @PathVariable("userId") String userId) {
        return filmService.removeLike(Long.parseLong(filmId), Long.parseLong(userId));
    }

    @DeleteMapping
    public Film delete(Film film) {
        return filmService.delete(film);
    }

    public void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза фильма не может быть раньше, чем 1895-12-28");
        }
    }
}
