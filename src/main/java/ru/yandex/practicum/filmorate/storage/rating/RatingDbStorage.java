package ru.yandex.practicum.filmorate.storage.rating;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Set;

@Repository("ratingDbStorage")
@RequiredArgsConstructor
public class RatingDbStorage implements RatingStorage {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Rating add(Rating rating) {
        return null;
    }

    @Override
    public Rating update(Rating rating) {
        return null;
    }

    @Override
    public Rating delete(Rating rating) {
        return null;
    }

    @Override
    public Set<Rating> getAll() {
        return null;
    }

    @Override
    public Rating getById(Long id) {
        return null;
    }
}
