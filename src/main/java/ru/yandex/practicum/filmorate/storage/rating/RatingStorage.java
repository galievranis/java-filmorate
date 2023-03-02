package ru.yandex.practicum.filmorate.storage.rating;

import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Set;

public interface RatingStorage {
    Rating add(Rating rating);

    Rating update(Rating rating);

    Rating delete(Rating rating);

    Set<Rating> getAll();

    Rating getById(Long id);
}
