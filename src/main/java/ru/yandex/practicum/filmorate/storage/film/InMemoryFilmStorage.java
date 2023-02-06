package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    Long id = 1L;
    Map<Long, Film> films = new HashMap<>();

    @Override
    public Film add(Film film) {
        if (!films.containsValue(film)) {
            film.setId(id++);
            films.put(film.getId(), film);
            log.info("Добавлен новый объект в базу данных");
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NoSuchElementException("Фильма с ID: " + film.getId() + " нет в базе данных.");
        }

        films.put(film.getId(), film);
        log.info("Фильм с ID {} обновлен", film.getId());
        return film;
    }

    @Override
    public Film delete(Film film) {
        films.remove(film.getId());
        log.info("Фильм с ID {} удален", film.getId());
        return film;
    }

    @Override
    public Set<Film> getAll() {
        return new HashSet<>(films.values());
    }

    @Override
    public Film getById(Long id) {
        if (!films.containsKey(id)) {
            throw new NoSuchElementException("Фильма с ID: " + id + " нет в базе данных");
        }

        return films.get(id);
    }
}
