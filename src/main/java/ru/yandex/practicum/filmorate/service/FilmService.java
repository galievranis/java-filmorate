package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    @Autowired
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film delete(Film film) {
        return filmStorage.delete(film);
    }

    public Set<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
      return filmStorage.getById(id);
    }

    public Film addLikeToFilm(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        film.getLikes().add(userStorage.getById(userId).getId());
        filmStorage.update(film);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        film.getLikes().remove(userStorage.getById(userId).getId());
        filmStorage.update(film);
        return film;
    }

    public Set<Film> getPopularFilms(String count) {
        return filmStorage.getAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(Long.parseLong(count))
                .collect(Collectors.toSet());
    }
}
