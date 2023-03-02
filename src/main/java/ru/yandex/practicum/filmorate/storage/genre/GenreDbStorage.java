package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

@Repository("genreDbStorage")
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Genre add(Genre genre) {
        return null;
    }

    @Override
    public Genre update(Genre genre) {
        return null;
    }

    @Override
    public Genre delete(Genre genre) {
        return null;
    }

    @Override
    public Set<Genre> getAll() {
        return null;
    }

    @Override
    public Genre getById(Long id) {
        return null;
    }
}
