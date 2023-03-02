package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreStorage {
    Genre add(Genre genre);

    Genre update(Genre genre);

    Genre delete(Genre genre);

    Set<Genre> getAll();

    Genre getById(Long id);
}
