package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film add(Film film) {
        log.info("Добавление фильма с ID {}", film.getId());
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        log.info("Обновление фильма с ID {}", film.getId());
        return filmStorage.update(film);
    }

    public Film delete(Film film) {
        log.info("Удаление фильма с ID {}", film.getId());
        return filmStorage.delete(film);
    }

    public Set<Film> getAll() {
        log.info("Получение списка всех фильмов");
        return filmStorage.getAll();
    }

    public Film getById(Long id) {
        log.info("Получение фильма с ID {}", id);
        return filmStorage.getById(id);
    }

    public Film addLikeToFilm(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        film.getLikes().add(userStorage.getById(userId).getId());
        filmStorage.update(film);
        log.info("Добавление лайка фильму с ID {} от пользователя с ID {}", filmId, userId);
        return film;
    }

    public Film removeLike(Long filmId, Long userId) {
        Film film = filmStorage.getById(filmId);
        film.getLikes().remove(userStorage.getById(userId).getId());
        filmStorage.update(film);
        log.info("Удаление лайка из фильма с ID {} от пользователя с ID {}", filmId, userId);
        return film;
    }

    public Set<Film> getPopularFilms(Long count) {
        log.info("Получение списка популярных фильмов");
        return filmStorage.getAll().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(count)
                .collect(Collectors.toSet());
    }
}
