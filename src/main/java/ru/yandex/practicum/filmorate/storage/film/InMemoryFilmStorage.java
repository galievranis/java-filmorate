package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component("memFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private Long id = 1L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Film create(Film film) {
        if (!films.containsValue(film)) {
            film.setId(id++);
            films.put(film.getId(), film);
        }

        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NoSuchElementException("Фильма с ID " + film.getId() + " нет в базе данных.");
        }

        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film delete(Film film) {
        films.remove(film.getId());
        return film;
    }

    @Override
    public Set<Film> getAll() {
        return new HashSet<>(films.values());
    }

    @Override
    public Film getById(Long id) {
        if (!films.containsKey(id)) {
            throw new NoSuchElementException("Фильма с ID " + id + " нет в базе данных");
        }

        return films.get(id);
    }
}
